package com.corp.pixmeup.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SurveyData(
	@Schema(description = "사용자 ID", example = "1")
	@JsonProperty("user_id")
	Long userId,

	@Schema(description = "나이 그룹 코드 (예: 4는 30대)", example = "4")
	Integer age,

	@Schema(description = "성별 (M: 남성, W: 여성)", example = "M")
	String gender,

	@Schema(description = "안경 착용 여부", example = "true")
	Boolean glasses,

	@Schema(description = "수술 이력 (normal, correction, cataract, etc)", example = "normal")
	String surgery,

	@Schema(description = "당뇨 여부", example = "false")
	Boolean diabetes,

	@Schema(description = "흡연 여부", example = "false")
	Boolean smoking
) {}