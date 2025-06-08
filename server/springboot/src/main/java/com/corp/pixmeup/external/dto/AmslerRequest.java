package com.corp.pixmeup.external.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record AmslerRequest(
	@Schema(description = "문진 정보")
	@JsonProperty("survey")
	SurveyData survey,

	@Schema(description = "암슬러 차트 검사 리스트")
	@JsonProperty("amsler_test_list")
	List<AmslerTest> amslerTestList
) {}