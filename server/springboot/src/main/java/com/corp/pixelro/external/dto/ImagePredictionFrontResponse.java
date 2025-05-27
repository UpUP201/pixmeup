package com.corp.pixelro.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "이미지 예측 결과 DTO")
public record ImagePredictionFrontResponse(
        @Schema(description = "결과 ID", example = "66411018f6ef23cde8b401ae")
        String id,
        @Schema(description = "사용자 ID", example = "42")
        Long userId,
        @Schema(description = "예측 요약", example = "이상 없음")
        String summary,
        @Schema(description = "상세 설명", example = "질병이 감지되지 않았습니다.")
        String description,
        @Schema(description = "이미지 S3 URL 주소")
        String imageUrl,
        @Schema(description = "생성 일시", example = "2024-05-08T10:00:00")
        String createdAt
) {
        @Builder
        public ImagePredictionFrontResponse(String id, Long userId, String summary, String description, String imageUrl, String createdAt) {
                this.id = id;
                this.userId = userId;
                this.summary = summary;
                this.description = description;
                this.imageUrl = imageUrl;
                this.createdAt = createdAt;
        }
}

