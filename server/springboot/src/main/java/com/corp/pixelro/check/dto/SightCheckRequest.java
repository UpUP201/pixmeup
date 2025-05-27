package com.corp.pixelro.check.dto;

import com.corp.pixelro.check.type.PerspectiveType;
import com.corp.pixelro.check.type.StatusType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SightCheckRequest(
        Long userId,
        @JsonProperty("leftEye")
        Integer leftSight,
        @JsonProperty("rightEye")
        Integer rightSight,
        PerspectiveType leftPerspective,
        PerspectiveType rightPerspective,
        Integer leftSightPrediction,
        Integer rightSightPrediction,
        String aiResult,
        StatusType status
) {
}
