package com.corp.pixmeup.check.dto;

import com.corp.pixmeup.check.entity.SightCheck;
import com.corp.pixmeup.check.type.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "시력 검사 응답 DTO")
@Builder
public record SightCheckResponse(

        @Schema(description = "시력 검사 ID", example = "1")
        Long id,

        @Schema(description = "왼쪽 시력 (0.1 단위)", example = "7")
        Integer leftSight,

        @Schema(description = "오른쪽 시력 (0.1 단위)", example = "8")
        Integer rightSight,

        @Schema(description = "왼쪽 시력 예측값", example = "7")
        Integer leftSightPrediction,

        @Schema(description = "오른쪽 시력 예측값", example = "8")
        Integer rightSightPrediction,

        @Schema(description = "AI 분석 결과", example = "정상")
        String aiResult,

        @Schema(description = "진단 상태", example = "NORMAL")
        StatusType status,

        @Schema(description = "검사 생성일시", example = "2025-05-09T10:00:00")
        LocalDateTime createdAt

) {
    public static SightCheckResponse of(SightCheck entity) {
        return new SightCheckResponse(
                entity.getId(),
                entity.getLeftSight(),
                entity.getRightSight(),
                entity.getLeftSightPrediction(),
                entity.getRightSightPrediction(),
                entity.getAiResult(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
