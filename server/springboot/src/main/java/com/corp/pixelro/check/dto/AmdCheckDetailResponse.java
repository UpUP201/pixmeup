package com.corp.pixelro.check.dto;

import lombok.Builder;

@Builder
public record AmdCheckDetailResponse(
        AmslerCheckDetailResponse amsler,
        MChartCheckDetailResponse mchart
) {
}
