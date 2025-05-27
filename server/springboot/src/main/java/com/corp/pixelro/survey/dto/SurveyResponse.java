package com.corp.pixelro.survey.dto;

public record SurveyResponse(
        Long resultId,
        Long userId,
        Integer age,
        String gender,
        Boolean glasses,
        String surgery,
        Boolean diabetes,
        Boolean currentSmoking,
        Boolean pastSmoking
) {
}
