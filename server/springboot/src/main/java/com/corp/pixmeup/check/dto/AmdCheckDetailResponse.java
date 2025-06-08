package com.corp.pixmeup.check.dto;

import lombok.Builder;

@Builder
public record AmdCheckDetailResponse(
        AmslerCheckDetailResponse amsler,
        MChartCheckDetailResponse mchart
) {
}
