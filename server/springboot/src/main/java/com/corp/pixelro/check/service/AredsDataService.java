package com.corp.pixelro.check.service;

import com.corp.pixelro.check.entity.AmslerCheck;
import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.check.util.AmslerCheckProcessor;
import com.corp.pixelro.check.util.MChartCheckProcessor;
import com.corp.pixelro.external.dto.AredsPredictionInput;
import com.corp.pixelro.external.util.AredsRequestBuilder;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.EyeCheckAmslerException;
import com.corp.pixelro.global.error.exception.EyeCheckMchartException;
import com.corp.pixelro.global.error.exception.SurveyException;
import com.corp.pixelro.global.error.handler.GlobalExceptionHandler;
import com.corp.pixelro.survey.entity.Survey;
import com.corp.pixelro.survey.service.SurveyService;
import com.corp.pixelro.survey.util.SurveyProcessor;
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
