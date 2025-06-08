package com.corp.pixmeup.external.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record PresbyopiaRequest(
	@Schema(description = "문진 정보")
	SurveyData survey,

	@Schema(description = "노안 조절력 검사 이력")
	@JsonProperty("presbyopia_test_list")
	List<PresbyopiaTest> presbyopiaTestList
) {}