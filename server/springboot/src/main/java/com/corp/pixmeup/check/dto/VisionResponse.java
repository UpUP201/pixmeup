package com.corp.pixmeup.check.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "시력 & 안구나이 검사 응답 DTO")
@Builder
public record VisionResponse(
        @Schema(description = "이름", example = "홍길동")
        String name,
        @Schema(description = "나이", example = "65")
        Integer age,
        @Schema(description = "왼쪽 시력 (0.1 단위)", example = "7")
        Integer leftSight,
        @Schema(description = "오른쪽 시력 (0.1 단위)", example = "8")
        Integer rightSight,
        @Schema(description = "검사 날짜", example = "2025-05-09T10:00:00")
        LocalDateTime createdAt
) {
}
