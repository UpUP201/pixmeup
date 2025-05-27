package com.corp.pixelro.survey.service;

import com.corp.pixelro.survey.dto.SurveyRequest;
import com.corp.pixelro.survey.dto.SurveyResponse;
import com.corp.pixelro.survey.entity.Survey;
import com.corp.pixelro.user.entity.User;

import java.util.List;

public interface SurveyService {
    SurveyResponse sendSurveyData(SurveyRequest request, Long userId);

    // 해당 사용자의 모든 문진 조회
    List<Survey> selectAllSurveys(Long userId);

    // 해당 사용자의 가장 최근 문진 조회
    Survey selectLatestSurvey(Long userId);

    void createSurvey(SurveyRequest request, User user);

}
