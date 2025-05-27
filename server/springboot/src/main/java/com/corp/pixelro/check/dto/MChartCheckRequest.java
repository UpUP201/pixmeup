package com.corp.pixelro.check.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MChartCheckRequest(
        Long userId,
        @JsonProperty("leftEyeVertical")
        Integer leftEyeVer,
        @JsonProperty("rightEyeVertical")
        Integer rightEyeVer,
        @JsonProperty("leftEyeHorizontal")
        Integer leftEyeHor,
        @JsonProperty("rightEyeHorizontal")
        Integer rightEyeHor,
        String aiResult
) {
}
