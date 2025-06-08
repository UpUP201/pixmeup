package com.corp.pixmeup.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record PresbyopiaAiResponse(
	@Schema(description = "사용자 ID", example = "1")
	Long userId,

	@Schema(description = "AI 분석 코멘트", example = "노안 경향이 관찰됩니다. 정밀 검진을 권장드립니다.")
	String comment,

	@Schema(description = "노안 상태", example = "normal")
	@JsonProperty("presbyopia_status")
	String presbyopiaStatus
) {}
