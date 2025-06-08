package com.corp.pixmeup.check.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record AmslerCheckRequest(
        Long userId,
        @JsonProperty("rightEyeDisorderType")
        List<String> rightMacularLoc,
        @JsonProperty("leftEyeDisorderType")
        List<String> leftMacularLoc,
        String aiResult
) {
}
