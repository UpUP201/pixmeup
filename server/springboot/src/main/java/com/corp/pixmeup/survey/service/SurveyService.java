package com.corp.pixmeup.survey.service;

import com.corp.pixmeup.survey.dto.SurveyRequest;
import com.corp.pixmeup.survey.dto.SurveyResponse;
import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.user.entity.User;

import java.util.List;

public interface SurveyService {
    SurveyResponse sendSurveyData(SurveyRequest request, Long userId);

    // 해당 사용자의 모든 문진 조회
    List<Survey> selectAllSurveys(Long userId);

    // 해당 사용자의 가장 최근 문진 조회
    Survey selectLatestSurvey(Long userId);

    // 문진 추가
    void createSurvey(SurveyRequest request, User user);

}
