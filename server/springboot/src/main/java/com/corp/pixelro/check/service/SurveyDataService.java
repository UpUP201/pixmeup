package com.corp.pixelro.check.service;

import com.corp.pixelro.external.dto.SurveyData;
import com.corp.pixelro.survey.entity.Survey;
import com.corp.pixelro.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyDataService {
    private final SurveyService surveyService;

    public SurveyData getLatestSurveyData(Long userId) {
        Survey survey = surveyService.selectLatestSurvey(userId);
        return SurveyData.builder()
                .userId(userId)
                .age(survey.getAge())
                .gender(survey.getGender().toString())
                .glasses(survey.isGlasses())
                .surgery(survey.getSurgery().toString())
                .diabetes(survey.isDiabetes())
                .smoking(survey.isCurrentSmoking())
                .build();
    }
}