package com.corp.pixelro.user.service;

import com.corp.pixelro.check.dto.AmslerCheckDetailResponse;
import com.corp.pixelro.check.dto.MChartCheckDetailResponse;
import com.corp.pixelro.check.dto.PresbyopiaCheckResponse;
import com.corp.pixelro.check.dto.SightCheckResponse;
import com.corp.pixelro.check.repository.*;
import com.corp.pixelro.check.repository.projection.RecentExaminationRawDto;
import com.corp.pixelro.check.type.AmslerStatus;
import com.corp.pixelro.check.type.StatusType;
import com.corp.pixelro.exercise.repository.EyeExerciseRecordRepository;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.BusinessException;
import com.corp.pixelro.global.util.HashingUtils;
import com.corp.pixelro.global.util.PhoneUtils;
import com.corp.pixelro.report.repository.ReportRepository;
import com.corp.pixelro.survey.entity.Survey;
import com.corp.pixelro.survey.repository.SurveyRepository;
import com.corp.pixelro.survey.type.Gender;
import com.corp.pixelro.survey.type.SurgeryType;
import com.corp.pixelro.survey.util.SurveyProcessor;
import com.corp.pixelro.user.dto.*;
import com.corp.pixelro.user.entity.User;
import com.corp.pixelro.user.repository.UserAgreeRepository;
import com.corp.pixelro.user.repository.UserRepository;
import com.corp.pixelro.webauthn.repository.WebAuthnCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CheckRepositoryCustom checkRepository;
    private final AmslerCheckRepository amslerCheckRepository;
    private final MChartCheckRepository mChartCheckRepository;
    private final PresbyopiaCheckRepository presbyopiaCheckRepository;
    private final SightCheckRepository sightCheckRepository;
    private final SurveyRepository surveyRepository;
    private final UserAgreeRepository userAgreeRepository;
    private final EyeExerciseRecordRepository eyeExerciseRecordRepository;
    private final WebAuthnCredentialRepository webAuthnCredentialRepository;
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * <p>더티체킹으로 처리하는 건 단일 요청에도 2~3초 정도 걸림</p>
     * <p>user 연관 엔티티들 N+1 문제도 있고, 더티 체킹 시점에 각 객체마다 update 쿼리 날려서 성능 저하 있음</p>
     * <p>각 레포지토리마다 벌크 업데이트문 만든 다음 그거 써서 성능 최적화함</p>
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUser(userId);

        // 컬렉션 명시적 초기화 (Lazy 로딩으로 된 필드 프록시로 로딩되는거 방지)
        Hibernate.initialize(user.getWebAuthnCredentials());

        // external Key 생성 (탈퇴 후에도 기록 열람 위함)
        String externalKey = generateUserExternalKey(user);
        user.performWithdrawalActions(externalKey);

        // 연관엔티티 삭제
        amslerCheckRepository.softDeleteAllByUser(user);
        mChartCheckRepository.softDeleteAllByUser(user);
        presbyopiaCheckRepository.softDeleteAllByUser(user);
        sightCheckRepository.softDeleteAllByUser(user);
        surveyRepository.softDeleteAllByUser(user);
        userAgreeRepository.softDeleteAllByUser(user);
        eyeExerciseRecordRepository.softDeleteAllByUser(user);
        reportRepository.softDeleteAllByUser(user);
        webAuthnCredentialRepository.deleteAll(user.getWebAuthnCredentials());
        user.getWebAuthnCredentials().clear();
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        User user = getUser(userId);

        // 설문 정보 조회 - 없을 경우 예외 발생하지 않고 Optional로 처리
        Optional<Survey> surveyOpt = surveyRepository.findTopByUser_IdOrderByCreatedAtDesc(user.getId());

        // 최신 검사일 계산
        int daysSinceCheck = getDaysSinceCheck(userId);

        // 설문 정보가 있으면 기존 방식대로 응답 생성
        // 설문 정보가 없으면 기본 정보만 포함한 응답 생성
        return surveyOpt
                .map(survey -> UserProfileResponse.from(user, survey, daysSinceCheck))
                .orElseGet(() -> UserProfileResponse.fromUserOnly(user, daysSinceCheck));
    }

    @Transactional
    @Override
    public UserProfileResponse updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        User user = getUser(userId);

        if (request.name() != null && StringUtils.hasText(request.name())
                && request.phoneNumber() != null && StringUtils.hasText(request.phoneNumber())) {
            user.updateProfile(request.name(), request.phoneNumber());
        }

        // 업데이트된 프로필 정보를 위해 최신 설문 정보를 다시 조회합니다.
        Optional<Survey> latestSurveyOpt = surveyRepository.findTopByUser_IdOrderByCreatedAtDesc(user.getId());

        // 최신 검사일로부터 경과일 재계산 (getUserProfile 로직과 동일)
        int daysSinceCheck = getDaysSinceCheck(userId);

        return latestSurveyOpt.map(survey -> UserProfileResponse.from(user, survey, daysSinceCheck)).orElseGet(() -> UserProfileResponse.fromUserOnly(user, daysSinceCheck));
    }

    @Transactional
    @Override
    public UserProfileResponse updateUserPassword(Long userId, UserPasswordUpdateRequest request) {
        User user = getUser(userId);

        // 현재 비밀번호 확인
        if (request.currentPassword() == null || request.currentPassword().equals(request.newPassword()) || !passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호가 유효한지 확인
        if (request.newPassword() == null || !StringUtils.hasText(request.newPassword())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 비밀번호 업데이트
        user.updatePassword(passwordEncoder.encode(request.newPassword()));

        // 업데이트된 프로필 정보 반환
        Optional<Survey> latestSurveyOpt = surveyRepository.findTopByUser_IdOrderByCreatedAtDesc(user.getId());

        int daysSinceCheck = getDaysSinceCheck(userId);

        // 설문 정보가 있으면 기존 방식대로 응답 생성
        // 설문 정보가 없으면 기본 정보만 포함한 응답 생성
        return latestSurveyOpt.map(survey -> UserProfileResponse.from(user, survey, daysSinceCheck)).orElseGet(() -> UserProfileResponse.fromUserOnly(user, daysSinceCheck));
    }

    @Override
    public RecentExaminationResponse getRecentExaminationResults(Long userId) {
        List<RecentExaminationRawDto> results = checkRepository.findLatestExaminations(userId);

        String age = "-", leftSight = "-", rightSight = "-", amslerStatus = "-";
        String leftEyeVer = "-", rightEyeVer = "-", leftEyeHor = "-", rightEyeHor = "-";

        for (RecentExaminationRawDto dto : results) {
            switch (dto.type()) {
                case "presbyopia" -> {
                    age = dto.age() != null ? String.valueOf(dto.age()) : "-";
                }
                case "sight" -> {
                    leftSight = dto.leftSight() != null ? String.valueOf(dto.leftSight()) : "-";
                    rightSight = dto.rightSight() != null ? String.valueOf(dto.rightSight()) : "-";
                }
                case "amsler" -> {
                    amslerStatus = AmslerStatus.inferAmslerStatus(
                            dto.leftMacularLoc(), dto.rightMacularLoc()
                    ).getDesc();
                }
                case "mchart" -> {
                    leftEyeVer = dto.leftEyeVer() != null ? String.valueOf(dto.leftEyeVer()) : "-";
                    rightEyeVer = dto.rightEyeVer() != null ? String.valueOf(dto.rightEyeVer()) : "-";
                    leftEyeHor = dto.leftEyeHor() != null ? String.valueOf(dto.leftEyeHor()) : "-";
                    rightEyeHor = dto.rightEyeHor() != null ? String.valueOf(dto.rightEyeHor()) : "-";
                }
            }
        }

        return new RecentExaminationResponse(
                age, leftSight, rightSight,
                amslerStatus,
                leftEyeVer, rightEyeVer,
                leftEyeHor, rightEyeHor
        );
    }

    @Override
    public Integer getRecentDate(Long userId) {
        User user = getUser(userId);

        return getDaysSinceCheck(user.getId());
    }

    @Override
    public TotalUserInfoResponse getTotalUserInfo(Long userId) {
        User user = getUser(userId);
        String name = user.getName();
        String phoneNumber = PhoneUtils.formatWithHyphen(user.getPhoneNumber());

        Optional<Survey> survey = surveyRepository.findTopByUser_IdOrderByCreatedAtDesc(userId);
        Gender gender = survey.map(Survey::getGender).orElse(null);
        String age = SurveyProcessor.estimateFirstAgeFromGroup(survey.map(Survey::getAge).orElse(null));
        Boolean glasses = survey.map(Survey::isGlasses).orElse(null);
        SurgeryType surgery = survey.map(Survey::getSurgery).orElse(null);
        Boolean pastSmoking = survey.map(Survey::isPastSmoking).orElse(null);
        Boolean currentSmoking = survey.map(Survey::isCurrentSmoking).orElse(null);
        Boolean diabetes = survey.map(Survey::isDiabetes).orElse(null);

        List<RecentExaminationRawDto> results = checkRepository.findLatestExaminations(userId);
        SightCheckResponse sightCheck = new SightCheckResponse(null, null, null, null, null, null, null, null);
        AmslerCheckDetailResponse amslerCheck = new AmslerCheckDetailResponse(null, null, null, null, null);
        MChartCheckDetailResponse mChartCheck = new MChartCheckDetailResponse(null, null, null, null, null, null, null);
        PresbyopiaCheckResponse presbyopiaCheck = new PresbyopiaCheckResponse(null, null, null, null, null, null);

        for (RecentExaminationRawDto dto : results) {
            switch (dto.type()) {
                case "sight" -> {
                    sightCheck = new SightCheckResponse(
                            dto.id(),
                            dto.leftSight(),
                            dto.rightSight(),
                            dto.leftSightPrediction(),
                            dto.rightSightPrediction(),
                            dto.aiResult(),
                            dto.status() != null ? StatusType.valueOf(dto.status()) : null,
                            dto.createdAt()
                    );
                }
                case "amsler" -> {
                    amslerCheck = new AmslerCheckDetailResponse(
                            dto.id(),
                            dto.rightMacularLoc(),
                            dto.leftMacularLoc(),
                            dto.aiResult(),
                            dto.createdAt()
                    );
                }
                case "mchart" -> {
                    mChartCheck = new MChartCheckDetailResponse(
                            dto.id(),
                            dto.leftEyeVer(),
                            dto.rightEyeVer(),
                            dto.leftEyeHor(),
                            dto.rightEyeHor(),
                            dto.aiResult(),
                            dto.createdAt()
                    );
                }
                case "presbyopia" -> {
                    presbyopiaCheck = new PresbyopiaCheckResponse(
                            dto.id(),
                            dto.age(),
                            dto.agePrediction(),
                            dto.aiResult(),
                            dto.status() != null ? StatusType.valueOf(dto.status()) : null,
                            dto.createdAt()
                    );
                }
            }
        }

        String latestTestDate = results.stream()
                .map(RecentExaminationRawDto::createdAt)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .map(LocalDateTime::toLocalDate) // 날짜만 추출
                .map(LocalDate::toString)
                .orElse(null);

        return new TotalUserInfoResponse(
                name,
                latestTestDate,
                phoneNumber,
                gender != null ? gender.getLabel() : null,
                age,
                glasses,
                surgery != null ? surgery.getLabel() : null,
                pastSmoking,
                currentSmoking,
                diabetes,
                sightCheck,
                amslerCheck,
                mChartCheck,
                presbyopiaCheck
        );
    }

    private int getDaysSinceCheck(Long userId) {
        LocalDateTime latestCheckDateTime = checkRepository.findLatestCheckDateByUserId(userId);
        LocalDate latestCheckDate = latestCheckDateTime != null ? latestCheckDateTime.toLocalDate() : null;
        return latestCheckDate != null ?
                (int) ChronoUnit.DAYS.between(latestCheckDate, LocalDate.now()) :
                -1;
    }

    /**
     * 사용자의 원본 정보와 생성일시를 조합하여 externalKey를 생성
     */
    private String generateUserExternalKey(User user) {
        String originalName = user.getName();
        String originalPhoneNumber = user.getPhoneNumber();
        LocalDateTime createdAt = user.getCreatedAt();

        if (originalName == null || originalPhoneNumber == null || createdAt == null) {
            // 실제 프로덕션에서는 더 구체적인 예외 타입이나 로깅 필요
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String createdAtString = createdAt.format(formatter);

        String dataToHash = originalName + originalPhoneNumber + createdAtString;

        try {
            return HashingUtils.generateSHA256(dataToHash);
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(ErrorCode.HASH_GENERATION_FAILED, e.getMessage());
        }
    }
}
