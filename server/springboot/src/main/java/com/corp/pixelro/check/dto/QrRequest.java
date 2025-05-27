package com.corp.pixelro.check.dto;

import com.corp.pixelro.survey.dto.SurveyRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record QrRequest(
        Integer age,
        String gender,
        Boolean glasses,
        String surgery,
        Boolean diabetes,
        Boolean currentSmoking,
        Boolean pastSmoking,
        TestResult testResults
) {
}
