package com.corp.pixmeup.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AmslerAiResponse(
	@Schema(description = "사용자 ID", example = "1")
	Long userId,

	@Schema(description = "암슬러 AI 분석 코멘트", example = "좌안에 왜곡이 관찰됩니다. 정밀 검진을 권장드립니다.")
	String comment
) {
}
