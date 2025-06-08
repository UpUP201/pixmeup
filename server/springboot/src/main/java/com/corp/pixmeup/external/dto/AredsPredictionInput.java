package com.corp.pixmeup.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "AREDS 예측 요청 DTO")
@Builder
public record AredsPredictionInput(
        @Schema(description = "나이", example = "65")
        Integer age,
        @Schema(description = "남성 여부 (1=남성, 0=여성)", example = "1")
        Integer male,
        @Schema(description = "과거 흡연 여부", example = "1")
        Integer pastSmoking,
        @Schema(description = "현재 흡연 여부", example = "0")
        Integer currentSmoking,
        @Schema(description = "엠차트 이상 여부", example = "true")
        Boolean mchartAbnormalFlag,
        @Schema(description = "암슬러 이상 여부", example = "true")
        Boolean amslerAbnormalFlag,
        @Schema(description = "엠차트 검사 ID", example = "123")
        Long mChartCheckId,
        @Schema(description = "암슬러 검사 ID", example = "456")
        Long amslerCheckId,
        @Schema(description = "문진 ID", example = "789")
        Long surveyId
) {}
