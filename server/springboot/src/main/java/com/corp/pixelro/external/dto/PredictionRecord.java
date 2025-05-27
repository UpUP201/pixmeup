package com.corp.pixelro.external.dto;

import com.corp.pixelro.external.type.PredictionType;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "예측 기록 요약 DTO")
public record PredictionRecord(
        @Schema(description = "기록 ID", example = "66411018f6ef23cde8b401ae")
        String id,

        @JsonProperty("created_at")
        @Schema(description = "생성일시", example = "2024-05-08T10:00:00")
        String createdAt,

        @Schema(description = "예측 타입", example = "AREDS")
        PredictionType type
) {}
