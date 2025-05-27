package com.corp.pixelro.check.service;

import com.corp.pixelro.check.entity.AmslerCheck;
import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.check.entity.PresbyopiaCheck;
import com.corp.pixelro.check.dto.*;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.check.repository.*;
import com.corp.pixelro.check.type.StatusType;
import com.corp.pixelro.check.util.AmslerCheckProcessor;
import com.corp.pixelro.external.dto.*;
import com.corp.pixelro.external.service.FastApiService;
import com.corp.pixelro.external.util.AiTextSanitizer;
import com.corp.pixelro.global.entity.BaseEntity;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.BusinessException;
import com.corp.pixelro.survey.dto.SurveyRequest;
import com.corp.pixelro.survey.entity.Survey;
import com.corp.pixelro.survey.service.SurveyService;
import com.corp.pixelro.user.entity.User;
import com.corp.pixelro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CheckServiceImpl implements CheckService {

    // repositories
    private final CheckRepositoryCustom checkRepository;
    private final AmslerCheckRepository amslerCheckRepository;
    private final PresbyopiaCheckRepository presbyopiaCheckRepository;
    private final MChartCheckRepository mChartCheckRepository;
    private final SightCheckRepository sightCheckRepository;

    // FastAPI 통한 AI 결과 이식
    private final FastApiService fastApiService;
    private final UserService userService;
    private final SurveyService surveyService;
    private final AmslerDataService amslerDataService;
    private final MChartDataService mChartDataService;

    @Override
    public Slice<CheckSummary> selectTotalCheckList(Long userId, int page, int size) {
        try {
            List<CheckSummary> checkSummaries = checkRepository.findTotalChecks(userId, page, size + 1); // size + 1로 조회
            boolean hasNext = checkSummaries.size() > size;
            List<CheckSummary> content = hasNext ? checkSummaries.subList(0, size) : checkSummaries;

            return new SliceImpl<>(content, PageRequest.of(page, size), hasNext);
        } catch (Exception e) {
            log.error("CheckServiceImpl.selectTotalCheckList: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void createSightCheck(SightCheckRequest sightCheck, Long userId, SurveyData surveyData) {
        try {
            User user = userService.getUser(userId);
            List<SightCheck> recent = sightCheckRepository.findTop5ByUser_IdOrderByCreatedAtDesc(userId);

            SightCheck request = new SightCheck();

            request = SightCheck.builder()
                    .user(user)
                    .leftSight(sightCheck.leftSight())
                    .rightSight(sightCheck.rightSight())
                    .createdAt(LocalDateTime.now())
                    .build();

            if (recent.size() == 5) {
                List<SightHistoryItem> history = new ArrayList<>();
                for (int i = 4; i >= 0; i--) {
                    SightCheck check = recent.get(i);
                    history.add(new SightHistoryItem(
                            check.getCreatedAt().toLocalDate().toString(),
                            check.getLeftSight(),
                            check.getRightSight()
                    ));
                }
                history.add(new SightHistoryItem(
                        LocalDate.now().toString(),
                        sightCheck.leftSight(),
                        sightCheck.rightSight()
                ));

                SightPredictResponse prediction = fastApiService.predictSight(userId, history);
                if (prediction == null) {
                    throw new BusinessException(ErrorCode.CHECK_RESULT_SAVE_FAILED);
                }

                // ai 판정 결과 처리
                recent.add(request);
                SightAiResponse sightAiResponse = fastApiService.getSightAiResponse(userId, recent, surveyData);

                request = SightCheck.builder()
                        .user(user)
                        .leftSight(sightCheck.leftSight())
                        .rightSight(sightCheck.rightSight())
                        .leftSightPrediction(prediction.next_left_sight())
                        .rightSightPrediction(prediction.next_right_sight())
                        .aiResult(AiTextSanitizer.sanitize(sightAiResponse.comment()))
                        .status(StatusType.from(AiTextSanitizer.sanitize(sightAiResponse.sightStatus())))
                        .build();
            }

            sightCheckRepository.save(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.CHECK_RESULT_SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public void createPresbyopiaCheck(PresbyopiaCheckRequest presbyopiaCheck, Long userId, SurveyData surveyData) {
        try {
            User user = userService.getUser(userId);
            List<PresbyopiaCheck> recent = presbyopiaCheckRepository.findTop5ByUser_IdOrderByCreatedAtDesc(userId);

            PresbyopiaCheck request = new PresbyopiaCheck();

            request = PresbyopiaCheck.builder()
                    .user(user)
                    .firstDistance(presbyopiaCheck.firstDistance())
                    .secondDistance(presbyopiaCheck.secondDistance())
                    .thirdDistance(presbyopiaCheck.thirdDistance())
                    .avgDistance(presbyopiaCheck.avgDistance())
                    .age(presbyopiaCheck.age())
                    .createdAt(LocalDateTime.now())
                    .build();

            if (recent.size() == 5) {
                List<Integer> history = recent.stream()
                        .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                        .map(PresbyopiaCheck::getAge)
                        .collect(Collectors.toList());
                history.add(presbyopiaCheck.age());

                EyeAgePredictResponse prediction = fastApiService.predictEyeAge(userId, history);
                if (prediction == null) {
                    throw new BusinessException(ErrorCode.CHECK_RESULT_SAVE_FAILED);
                }

                // ai 판정 결과 처리
                recent.add(request);
                PresbyopiaAiResponse presbyopiaAiResponse = fastApiService.getPresbyopiaAiResponse(userId, recent, surveyData);

                log.info("AI 응답값: {}", presbyopiaAiResponse);

                request = PresbyopiaCheck.builder()
                        .user(user)
                        .firstDistance(presbyopiaCheck.firstDistance())
                        .secondDistance(presbyopiaCheck.secondDistance())
                        .thirdDistance(presbyopiaCheck.thirdDistance())
                        .avgDistance(presbyopiaCheck.avgDistance())
                        .age(presbyopiaCheck.age())
                        .agePrediction(prediction.next_eye_age())
                        .aiResult(AiTextSanitizer.sanitize(presbyopiaAiResponse.comment()))
                        .status(StatusType.from(AiTextSanitizer.sanitize(presbyopiaAiResponse.presbyopiaStatus())))
                        .build();
            }

            presbyopiaCheckRepository.save(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.CHECK_RESULT_SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public void createAmslerCheck(AmslerCheckRequest amslerCheck, Long userId, SurveyData surveyData) {
        try {
            User user = userService.getUser(userId);
            List<AmslerCheck> recent = amslerCheckRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);

            AmslerCheck request = AmslerCheck.builder()
                    .user(user)
                    .rightMacularLoc(AmslerCheckProcessor.mapDisorderListToCodeString(amslerCheck.rightMacularLoc()))
                    .leftMacularLoc(AmslerCheckProcessor.mapDisorderListToCodeString(amslerCheck.leftMacularLoc()))
                    .createdAt(LocalDateTime.now())
                    .build();

            recent.add(request);

            AmslerAiResponse amslerAiResponse = fastApiService.getAmslerAiResponse(userId, recent, surveyData);

            request = AmslerCheck.builder()
                    .user(user)
                    .rightMacularLoc(AmslerCheckProcessor.mapDisorderListToCodeString(amslerCheck.rightMacularLoc()))
                    .leftMacularLoc(AmslerCheckProcessor.mapDisorderListToCodeString(amslerCheck.leftMacularLoc()))
                    .aiResult(AiTextSanitizer.sanitize(amslerAiResponse.comment()))
                    .build();

            amslerCheckRepository.save(request);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CHECK_RESULT_SAVE_FAILED, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void createMChartCheck(MChartCheckRequest mChartCheck, Long userId, SurveyData surveyData) {
        try {
            User user = userService.getUser(userId);
            List<MChartCheck> recent = mChartCheckRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);

            MChartCheck request = MChartCheck.builder()
                    .user(user)
                    .leftEyeVer(mChartCheck.leftEyeVer())
                    .rightEyeVer(mChartCheck.rightEyeVer())
                    .leftEyeHor(mChartCheck.leftEyeHor())
                    .rightEyeHor(mChartCheck.rightEyeHor())
                    .createdAt(LocalDateTime.now())
                    .build();

            recent.add(request);
            MChartAiResponse mChartAiResponse = fastApiService.getMChartAiResponse(userId, recent, surveyData);

            request = MChartCheck.builder()
                    .user(user)
                    .leftEyeVer(mChartCheck.leftEyeVer())
                    .rightEyeVer(mChartCheck.rightEyeVer())
                    .leftEyeHor(mChartCheck.leftEyeHor())
                    .rightEyeHor(mChartCheck.rightEyeHor())
                    .aiResult(AiTextSanitizer.sanitize(mChartAiResponse.comment()))
                    .build();

            mChartCheckRepository.save(request);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CHECK_RESULT_SAVE_FAILED, e.getMessage());
        }
    }

    @Override
    public AmdCheckDetailResponse selectAmdCheckDetail(Long userId, LocalDateTime targetDateTime) {
        LocalDateTime start = targetDateTime.withNano(0);
        LocalDateTime end = start.plusSeconds(1);

        AmslerCheck amsler = amslerCheckRepository
                .findByUser_IdAndCreatedAtBetween(userId, start, end)
                .orElse(null);

        MChartCheck mchart = mChartCheckRepository
                .findByUser_IdAndCreatedAtBetween(userId, start, end)
                .orElse(null);

        AmslerCheckDetailResponse amslerDto = amsler != null
                ? AmslerCheckDetailResponse.of(amsler)
                : null;

        MChartCheckDetailResponse mchartDto = mchart != null
                ? MChartCheckDetailResponse.of(mchart)
                : null;

        return new AmdCheckDetailResponse(amslerDto, mchartDto);
    }

    @Override
    public AmdCheckDetailResponse selectAmdCheckDetailByDifferentDate(Long userId, LocalDateTime amslerDateTime, LocalDateTime mchartDateTime) {
        LocalDateTime amslerStart = amslerDateTime.withNano(0);
        LocalDateTime amslerEnd = amslerStart.plusSeconds(1);

        LocalDateTime mchartStart = mchartDateTime.withNano(0);
        LocalDateTime mchartEnd = mchartStart.plusSeconds(1);

        AmslerCheck amsler = amslerCheckRepository
                .findByUser_IdAndCreatedAtBetween(userId, amslerStart, amslerEnd)
                .orElse(null);

        MChartCheck mchart = mChartCheckRepository
                .findByUser_IdAndCreatedAtBetween(userId, mchartStart, mchartEnd)
                .orElse(null);

        AmslerCheckDetailResponse amslerDto = amsler != null
                ? AmslerCheckDetailResponse.of(amsler)
                : null;

        MChartCheckDetailResponse mchartDto = mchart != null
                ? MChartCheckDetailResponse.of(mchart)
                : null;

        return new AmdCheckDetailResponse(amslerDto, mchartDto);
    }

    @Override
    @Transactional
    public void createData(QrRequest request, Long userId) {
        User user = userService.getUser(userId);

        // 문진 저장
        SurveyRequest surveyRequest = new SurveyRequest(
                userId,
                request.age(),
                request.gender(),
                request.glasses(),
                request.surgery(),
                request.diabetes(),
                request.currentSmoking(),
                request.pastSmoking()
        );
        surveyService.createSurvey(surveyRequest, user);

        SurveyData surveyData = new SurveyData(
                surveyRequest.userId(),
                surveyRequest.age(),
                surveyRequest.gender(),
                surveyRequest.glasses(),
                surveyRequest.surgery(),
                surveyRequest.diabetes(),
                surveyRequest.currentSmoking()
        );

        TestResult results = request.testResults();
        if (results == null) return;
        // 각 검사 데이터 존재 여부 확인 후 저장

        if (results.presbyopiaCheck() != null)
            createPresbyopiaCheck(results.presbyopiaCheck(), userId, surveyData);

        if (results.sightCheck() != null)
            createSightCheck(results.sightCheck(), userId, surveyData);

        if (results.amslerCheck() != null)
            createAmslerCheck(results.amslerCheck(), userId, surveyData);

        if (results.mChartCheck() != null)
            createMChartCheck(results.mChartCheck(), userId, surveyData);

    }

    public boolean aredsInputHasChanged(Long userId) {
        // 현재 최신 검사 ID
        AmslerCheck amslerCheck = amslerDataService.selectLatestAmslerCheck(userId);
        MChartCheck mChartCheck = mChartDataService.selectLatestMChartCheck(userId);
        Survey survey = surveyService.selectLatestSurvey(userId);

        Long latestAmslerId = amslerCheck == null ? null : amslerCheck.getId();
        Long latestMchartId = mChartCheck == null ? null : mChartCheck.getId();
        Long latestSurveyId = survey == null ? null : survey.getId();

        // 가장 최근 예측 결과 조회
        AredsResultResponse lastResult = fastApiService.getLatestAredsResultByUserId(userId);
        if (lastResult == null) return true; // 예측 결과 없음 → 예측 필요

        return !latestAmslerId.equals(lastResult.amslerCheckId())
                || !latestMchartId.equals(lastResult.mChartCheckId())
                || !latestSurveyId.equals(lastResult.surveyId());
    }

}
