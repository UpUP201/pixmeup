package com.corp.pixmeup.check.dto;

import com.corp.pixmeup.check.entity.AmslerCheck;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "암슬러 검사 상세 응답 DTO")
public record AmslerCheckDetailResponse(
        @Schema(description = "암슬러 검사 ID", example = "1")
        Long id,

        @Schema(description = "오른쪽 시야 결과", example = "Distorted")
        String rightMacularLoc,

        @Schema(description = "왼쪽 시야 결과", example = "Normal")
        String leftMacularLoc,

        @Schema(description = "AI 분석 결과", example = "황반변성 의심")
        String aiResult,

        @Schema(description = "검사 생성일시", example = "2025-05-09T10:00:00")
        LocalDateTime createdAt
) {
    public static AmslerCheckDetailResponse of(AmslerCheck entity) {
        return new AmslerCheckDetailResponse(
                entity.getId(),
                entity.getRightMacularLoc(),
                entity.getLeftMacularLoc(),
                entity.getAiResult(),
                entity.getCreatedAt()
        );
    }
}
