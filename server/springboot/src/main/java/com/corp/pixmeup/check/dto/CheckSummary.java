package com.corp.pixmeup.check.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CheckSummary(
        @Schema(description = "검사 날짜", example = "2025-05-09T10:00:00")
        LocalDateTime dateTime,
        @Schema(description = "시력 검사 여부", example = "true")
        Boolean hasSight,
        @Schema(description = "노안 검사 여부", example = "false")
        Boolean hasPresbyopia,
        @Schema(description = "암슬러 검사 여부", example = "true")
        Boolean hasAmsler,
        @Schema(description = "엠차트 검사 여부", example = "true")
        Boolean hasMChart
) {
}
