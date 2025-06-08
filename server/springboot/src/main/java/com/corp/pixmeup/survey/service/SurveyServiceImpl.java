package com.corp.pixmeup.survey.service;

import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.exception.BusinessException;
import com.corp.pixmeup.survey.converter.SurveyConverter;
import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.survey.dto.SurveyRequest;
import com.corp.pixmeup.survey.dto.SurveyResponse;
import com.corp.pixmeup.survey.repository.SurveyRepository;
import com.corp.pixmeup.survey.type.Gender;
import com.corp.pixmeup.survey.type.SurgeryType;
import com.corp.pixmeup.user.entity.User;
import com.corp.pixmeup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final UserService userService;
    private final SurveyRepository surveyRepository;

    // dto converters
    private final SurveyConverter surveyConverter;

    @Override
    public SurveyResponse sendSurveyData(SurveyRequest request, Long userId) {
        User user = userService.getUser(userId);

        Survey survey = surveyConverter.toEntity(request, user);

        Survey saved = surveyRepository.save(survey);

        return surveyConverter.toDto(saved);
    }

    @Override
    public List<Survey> selectAllSurveys(Long userId) {
        List<Survey> result = surveyRepository.findAllByUser_Id(userId);
        return result;
    }

    @Override
    public Survey selectLatestSurvey(Long userId) {
        return surveyRepository
                .findTopByUser_IdOrderByCreatedAtDesc(userId)
                .orElse(null);

    }

    @Override
    public void createSurvey(SurveyRequest request, User user) {
        try {
            Survey survey = Survey.builder()
                    .user(user)
                    .age(request.age())
                    .gender(Gender.valueOf(request.gender()))
                    .glasses(Boolean.TRUE.equals(request.glasses()))
                    .surgery(SurgeryType.valueOf(request.surgery().toUpperCase())) // "normal" → "NORMAL"
                    .diabetes(Boolean.TRUE.equals(request.diabetes()))
                    .currentSmoking(Boolean.TRUE.equals(request.currentSmoking()))
                    .pastSmoking(Boolean.TRUE.equals(request.pastSmoking()))
                    .build();
            surveyRepository.save(survey);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SURVEY_SAVE_FAILED);
        }
    }

}
