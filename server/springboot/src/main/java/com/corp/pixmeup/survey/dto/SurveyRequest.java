package com.corp.pixmeup.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문진 요청 DTO")
public record SurveyRequest(
        @Schema(description = "사용자 ID", example = "42")
        Long userId,

        @Schema(description = "나이", example = "65")
        Integer age,

        @Schema(description = "성별", example = "M", allowableValues = {"M", "W"})
        String gender,

        @Schema(description = "안경 착용 여부", example = "true")
        Boolean glasses,

        @Schema(description = "수술 이력", example = "CATARACT", allowableValues = {"NORMAL", "CORRECTION", "CATARACT", "ETC"})
        String surgery,

        @Schema(description = "당뇨병 여부", example = "false")
        Boolean diabetes,

        @Schema(description = "현재 흡연 여부", example = "true")
        Boolean currentSmoking,

        @Schema(description = "과거 흡연 여부", example = "false")
        Boolean pastSmoking
) {
}