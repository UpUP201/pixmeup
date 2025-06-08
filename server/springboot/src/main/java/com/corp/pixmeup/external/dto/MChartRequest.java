package com.corp.pixmeup.external.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record MChartRequest(
	@Schema(description = "문진 정보")
	@JsonProperty("survey")
	SurveyData survey,

	@Schema(description = "엠차트 검사 이력")
	@JsonProperty("mchart_test_list")
	List<MChartTest> mchartTestList
) {
}
