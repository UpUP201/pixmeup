package com.corp.pixelro.check.dto;

import com.corp.pixelro.check.entity.PresbyopiaCheck;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.check.type.StatusType;
import com.corp.pixelro.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Schema(description = "노안 검사 응답 DTO")
public record PresbyopiaCheckResponse(

        @Schema(description = "노안 검사 ID", example = "1")
        Long id,

        @Schema(description = "실제 나이", example = "64")
        Integer age,

        @Schema(description = "AI가 예측한 눈 나이", example = "68")
        Integer agePrediction,

        @Schema(description = "AI 분석 결과", example = "노안 의심")
        String aiResult,

        @Schema(description = "진단 상태", example = "WARNING")
        StatusType status,

        @Schema(description = "검사 생성일시", example = "2025-05-09T10:00:00")
        LocalDateTime createdAt

) {
    public static PresbyopiaCheckResponse of(PresbyopiaCheck entity) {
    return new PresbyopiaCheckResponse(
            entity.getId(),
            entity.getAge(),
            entity.getAgePrediction(),
            entity.getAiResult(),
            entity.getStatus(),
            entity.getCreatedAt()
    );
}
}
