package com.corp.pixmeup.check.dto;

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
