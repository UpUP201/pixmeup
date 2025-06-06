package com.corp.pixelro.survey.util;

import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.BusinessException;
import com.corp.pixelro.survey.entity.Survey;

import java.util.HashMap;
import java.util.Map;

public class SurveyProcessor {
    public static Map<String, Object> process(Survey survey) {
        int ageEstimate = estimateAgeFromGroup(survey.getAge());

        Map<String, Object> map = new HashMap<>();
        // ** 외부에서 추가하던 id를 processor에서 추가 **
        map.put("survey_id", survey.getId());
        map.put("survey_age", ageEstimate);
        map.put("survey_gender", survey.getGender().name());
        map.put("survey_glasses_wearing", survey.isGlasses());
        map.put("survey_eye_surgery_type", survey.getSurgery().name().toLowerCase());
        map.put("survey_diabetes", survey.isDiabetes());
        map.put("survey_past_smoking", survey.isPastSmoking());
        map.put("survey_current_smoking", survey.isCurrentSmoking());
        return map;
    }

    private static int estimateAgeFromGroup(int groupCode) {
        return switch (groupCode) {
            case 1 -> 15;
            case 2 -> 25;
            case 4 -> 35;
            case 5 -> 45;
            case 6 -> 55;
            case 7 -> 65;
            case 8 -> 75;
            case 9 -> 5;
            default -> 0;
        };
    }

    /**
     * 제공된 그룹 코드에 따라 연령대를 추정합니다.
     *
     * @param groupCode 특정 연령대를 나타내는 코드
     * @return "10대" 또는 "20대"와 같은 연령대 문자열
     * @throws BusinessException 유효하지 않은 그룹 코드가 제공된 경우
     */
    public static String estimateFirstAgeFromGroup(Integer groupCode) {
        if (groupCode == null) return null;

        return switch (groupCode) {
            case 1 -> "10대";
            case 2 -> "20대";
            case 4 -> "30대";
            case 5 -> "40대";
            case 6 -> "50대";
            case 7 -> "60대";
            case 8 -> "70세 이상";
            case 9 -> "9세 이하";
            default -> throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }
}
