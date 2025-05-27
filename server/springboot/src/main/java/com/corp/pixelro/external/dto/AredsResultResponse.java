package com.corp.pixelro.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AREDS 예측 결과 DTO")
public record AredsResultResponse(
        @Schema(description = "결과 ID", example = "66410e85f6ef23cde8b40192")
        String id,
        @Schema(description = "사용자 ID", example = "42")
        @JsonProperty("user_id")
        Long userId,
        @Schema(description = "위험 확률 (%)", example = "65")
        @JsonProperty("risk_percent")
        Integer riskPercent,
        @Schema(description = "결과 요약", example = "중간 위험군입니다.")
        String summary,
        @Schema(description = "위험 등급", example = "Medium")
        String risk,
        @Schema(description = "생성 일시", example = "2024-05-08T10:00:00")
        @JsonProperty("created_at")
        String createdAt,
        @Schema(description = "엠차트 ID", example = "123")
        @JsonProperty("mchart_check_id")
        Long mChartCheckId,
        @Schema(description = "암슬러 ID", example = "456")
        @JsonProperty("amsler_check_id")
        Long amslerCheckId,
        @Schema(description = "문진 ID", example = "789")
        @JsonProperty("survey_id")
        Long surveyId,
        @Schema(description = "유저 이름", example = "김종명")
        @JsonProperty("user_name")
        String userName
) {}
