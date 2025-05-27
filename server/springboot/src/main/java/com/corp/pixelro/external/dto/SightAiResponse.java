package com.corp.pixelro.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SightAiResponse(
	@Schema(description = "사용자 ID", example = "1")
	Long userId,

	@Schema(description = "AI가 분석한 시력 상태 코멘트", example = "시력 이상 소견이 반복됩니다. 정밀 검진이 필요합니다.")
	String comment,

	@Schema(description = "시력 상태 (good, normal, bad)", example = "bad")
	String sightStatus
) {
}
