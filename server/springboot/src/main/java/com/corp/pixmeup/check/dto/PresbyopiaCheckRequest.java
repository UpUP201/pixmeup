package com.corp.pixmeup.check.dto;

import com.corp.pixmeup.check.type.StatusType;
import lombok.Builder;

@Builder
public record PresbyopiaCheckRequest(
        Long userId,
        Double firstDistance,
        Double secondDistance,
        Double thirdDistance,
        Double avgDistance,
        Integer age,
        Integer agePrediction,
        String aiResult,
        StatusType status
) {
}
