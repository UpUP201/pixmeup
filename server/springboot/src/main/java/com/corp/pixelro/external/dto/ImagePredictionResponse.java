package com.corp.pixelro.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 예측 결과 DTO")
public record ImagePredictionResponse(
        @Schema(description = "결과 ID", example = "66411018f6ef23cde8b401ae")
        String id,
        @Schema(description = "사용자 ID", example = "42")
        @JsonProperty("user_id")
        Long userId,
        @Schema(description = "예측 요약", example = "이상 없음")
        String summary,
        @Schema(description = "상세 설명", example = "질병이 감지되지 않았습니다.")
        String description,
        @Schema(description = "이미지 S3 Key")
        @JsonProperty("s3_key")
        String s3Key,
        @Schema(description = "생성 일시", example = "2024-05-08T10:00:00")
        @JsonProperty("created_at")
        String createdAt
) {}

