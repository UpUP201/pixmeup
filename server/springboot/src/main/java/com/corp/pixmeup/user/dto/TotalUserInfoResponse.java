package com.corp.pixmeup.user.dto;

import com.corp.pixmeup.check.dto.AmslerCheckDetailResponse;
import com.corp.pixmeup.check.dto.MChartCheckDetailResponse;
import com.corp.pixmeup.check.dto.PresbyopiaCheckResponse;
import com.corp.pixmeup.check.dto.SightCheckResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 전체 정보")
public record TotalUserInfoResponse(

        @Schema(description = "사용자 이름", example = "박성문")
        String name,

        @Schema(description = "마지막 검사일 (검사 기록 없을 땐 없음으로 표시)", example = "2025-05-20")
        String latestTestDate,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "성별", example = "남성", nullable = true)
        String gender,

        @Schema(description = "나이", example = "10대", nullable = true)
        String age,

        @Schema(description = "안경 착용 여부", example = "true", nullable = true)
        Boolean glasses,

        @Schema(description = "수술 이력", example = "없음", nullable = true)
        String surgery,

        @Schema(description = "과거 흡연 여부", example = "false", nullable = true)
        Boolean pastSmoking,

        @Schema(description = "현재 흡연 여부", example = "false", nullable = true)
        Boolean currentSmoking,

        @Schema(description = "당뇨 여부", example = "false", nullable = true)
        Boolean diabetes,

        @Schema(description = "시력 검사 결과", nullable = true)
        SightCheckResponse sightCheck,

        @Schema(description = "암슬러 검사 결과", nullable = true)
        AmslerCheckDetailResponse amslerCheck,

        @Schema(description = "엠차트 검사 결과", nullable = true)
        MChartCheckDetailResponse mChartCheck,

        @Schema(description = "노안 검사 결과", nullable = true)
        PresbyopiaCheckResponse presbyopiaCheck
) {
}
