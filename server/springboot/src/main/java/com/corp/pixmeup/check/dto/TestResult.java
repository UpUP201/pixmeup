package com.corp.pixmeup.check.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TestResult(
        @JsonProperty("shortVisualAcuity")
        SightCheckRequest sightCheck,
        @JsonProperty("presbyopia")
        PresbyopiaCheckRequest presbyopiaCheck,
        @JsonProperty("amslerGrid")
        AmslerCheckRequest amslerCheck,
        @JsonProperty("mChart")
        MChartCheckRequest mChartCheck
) {
}
