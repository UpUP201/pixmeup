package com.corp.pixelro.check.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "최근 검사 조회 요청 DTO")
public record LatestRequest(
        @Schema(description = "선택한 날짜", example = "2025-05-09T10:00:00")
        LocalDateTime selectedDateTime
) {
}
