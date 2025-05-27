package com.corp.pixelro.external.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SightRequest(
	SurveyData survey,
	@JsonProperty("sight_test_list") List<SightTest> sightTestList
) {}