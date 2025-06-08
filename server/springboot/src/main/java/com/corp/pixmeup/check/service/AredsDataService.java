package com.corp.pixmeup.check.service;

import com.corp.pixmeup.check.entity.AmslerCheck;
import com.corp.pixmeup.check.entity.MChartCheck;
import com.corp.pixmeup.check.util.AmslerCheckProcessor;
import com.corp.pixmeup.check.util.MChartCheckProcessor;
import com.corp.pixmeup.external.dto.AredsPredictionInput;
import com.corp.pixmeup.external.util.AredsRequestBuilder;
import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.exception.EyeCheckAmslerException;
import com.corp.pixmeup.global.error.exception.EyeCheckMchartException;
import com.corp.pixmeup.global.error.exception.SurveyException;
import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.survey.service.SurveyService;
import com.corp.pixmeup.survey.util.SurveyProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * AREDS 예측에 필요한 검사/문진 데이터를 준비하는 클래스
 */
@Service
@RequiredArgsConstructor
public class AredsDataService {

    private final AmslerDataService amslerDataService;
    private final MChartDataService mChartDataService;
    private final SurveyService surveyService;

    @Transactional
    public AredsPredictionInput prepareAredsInput(Long userId) {
        Survey survey = surveyService.selectLatestSurvey(userId);
        if (survey == null) {
            throw new SurveyException(ErrorCode.SURVEY_NOT_EXISTS);
        }
        AmslerCheck amsler = amslerDataService.selectLatestAmslerCheck(userId);
        if (amsler == null) {
            throw new EyeCheckAmslerException(ErrorCode.AMSLER_NOT_EXISTS);
        }
        MChartCheck mchart = mChartDataService.selectLatestMChartCheck(userId);
        if (mchart == null) {
            throw new EyeCheckMchartException(ErrorCode.MCHART_NOT_EXISTS);
        }

        Map<String, Object> surveyMap = SurveyProcessor.process(survey);
        Map<String, Object> amslerMap = AmslerCheckProcessor.process(amsler);
        Map<String, Object> mchartMap = MChartCheckProcessor.process(mchart);

        return AredsRequestBuilder.buildFrom(userId, surveyMap, mchartMap, amslerMap);
    }

}
