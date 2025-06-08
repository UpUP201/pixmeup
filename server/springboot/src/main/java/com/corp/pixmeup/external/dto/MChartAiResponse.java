package com.corp.pixmeup.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MChartAiResponse(
	@Schema(description = "사용자 ID", example = "1")
	Long userId,

	@Schema(description = "AI 분석 코멘트", example = "우안에 수직 왜곡이 관찰됩니다. 정밀 검진을 권장합니다.")
	String comment
) {
}
