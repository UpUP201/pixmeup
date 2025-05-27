package com.corp.pixelro.check.dto;

import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Schema(description = "엠차트 검사 상세 응답 DTO")
public record MChartCheckDetailResponse(

        @Schema(description = "엠차트 검사 ID", example = "2")
        Long id,

        @Schema(description = "왼쪽 수직 시야", example = "2")
        Integer leftEyeVer,

        @Schema(description = "오른쪽 수직 시야", example = "3")
        Integer rightEyeVer,

        @Schema(description = "왼쪽 수평 시야", example = "1")
        Integer leftEyeHor,

        @Schema(description = "오른쪽 수평 시야", example = "2")
        Integer rightEyeHor,

        @Schema(description = "AI 분석 결과", example = "정상")
        String aiResult,

        @Schema(description = "검사 생성일시", example = "2025-05-09T10:00:00")
        LocalDateTime createdAt

) {

    public static MChartCheckDetailResponse of(MChartCheck entity) {
        return new MChartCheckDetailResponse(
                entity.getId(),
                entity.getLeftEyeVer(),
                entity.getRightEyeVer(),
                entity.getLeftEyeHor(),
                entity.getRightEyeHor(),
                entity.getAiResult(),
                entity.getCreatedAt()
        );
    }
}
