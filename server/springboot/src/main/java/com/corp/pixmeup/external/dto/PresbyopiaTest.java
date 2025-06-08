package com.corp.pixmeup.external.dto;

import java.time.LocalDateTime;

import com.corp.pixmeup.check.entity.PresbyopiaCheck;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record PresbyopiaTest(
	@Schema(description = "첫 번째 체크 거리 (cm)", example = "22.5")
	@JsonProperty("first_distance")
	Double firstDistance,

	@Schema(description = "두 번째 체크 거리 (cm)", example = "24.3")
	@JsonProperty("second_distance")
	Double secondDistance,

	@Schema(description = "세 번째 체크 거리 (cm)", example = "26.1")
	@JsonProperty("third_distance")
	Double thirdDistance,

	@Schema(description = "평균 거리 (cm)", example = "24.3")
	@JsonProperty("avg_distance")
	Double avgDistance,

	@Schema(description = "검사 일시", example = "2025-05-08T14:20:00")
	@JsonProperty("created_at")
	LocalDateTime createdAt
) {
	public static PresbyopiaTest from(PresbyopiaCheck check) {
		return new PresbyopiaTest(
			check.getFirstDistance(),
			check.getSecondDistance(),
			check.getThirdDistance(),
			check.getAvgDistance(),
			check.getCreatedAt()
		);
	}
}