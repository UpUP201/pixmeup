package com.corp.pixelro.external.util;

import com.corp.pixelro.external.dto.AredsPredictionInput;

import java.util.HashMap;
import java.util.Map;

/**
 * FastAPI 요청용 입력값 조립 및 변환 유틸
 */
public class AredsRequestBuilder {
    public static Map<String, Object> toRequestMap(AredsPredictionInput request, Long userId) {
        return Map.of(
                "user_id", userId,
                "age", request.age(),
                "male", request.male(),
                "past_smoking", request.pastSmoking(),
                "current_smoking", request.currentSmoking(),
                "mchart_abnormal_flag", request.mchartAbnormalFlag(),
                "amsler_abnormal_flag", request.amslerAbnormalFlag(),
                "mchart_check_id", request.mChartCheckId(),
                "amsler_check_id", request.amslerCheckId(),
                "survey_id", request.surveyId()
        );
    }

    public static AredsPredictionInput buildFrom(
            Long userId,
            Map<String, Object> surveyData,
            Map<String, Object> mchartData,
            Map<String, Object> amslerData
    ) {
        return AredsPredictionInput.builder()
                .age((int) surveyData.get("survey_age"))
                .male("M".equals(surveyData.get("survey_gender")) ? 1 : 0)
                .pastSmoking((boolean) surveyData.get("survey_past_smoking") ? 1 : 0)
                .currentSmoking((boolean) surveyData.get("survey_current_smoking") ? 1 : 0)
                .mchartAbnormalFlag((boolean) mchartData.get("mchart_abnormal_flag"))
                .amslerAbnormalFlag((boolean) amslerData.get("amsler_abnormal_flag"))
                .mChartCheckId((Long) mchartData.get("mchart_check_id"))
                .amslerCheckId((Long) amslerData.get("amsler_check_id"))
                .surveyId((Long) surveyData.get("survey_id"))
                .build();
    }
}
