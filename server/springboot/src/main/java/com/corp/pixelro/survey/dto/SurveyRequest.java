package com.corp.pixelro.survey.dto;

public record SurveyRequest(
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
