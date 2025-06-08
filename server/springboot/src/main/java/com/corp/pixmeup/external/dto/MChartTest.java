package com.corp.pixmeup.external.dto;

import java.time.LocalDateTime;

import com.corp.pixmeup.check.entity.MChartCheck;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record MChartTest(
	@Schema(description = "왼쪽 눈 수직 왜곡 정도", example = "2")
	@JsonProperty("left_eye_ver")
	Integer leftEyeVer,

	@Schema(description = "오른쪽 눈 수직 왜곡 정도", example = "3")
	@JsonProperty("right_eye_ver")
	Integer rightEyeVer,

	@Schema(description = "왼쪽 눈 수평 왜곡 정도", example = "1")
	@JsonProperty("left_eye_hor")
	Integer leftEyeHor,

	@Schema(description = "오른쪽 눈 수평 왜곡 정도", example = "0")
	@JsonProperty("right_eye_hor")
	Integer rightEyeHor,

	@Schema(description = "검사 일시", example = "2025-05-08T14:20:00")
	@JsonProperty("created_at")
	LocalDateTime createdAt
) {
	public static MChartTest from(MChartCheck check) {
		return new MChartTest(
			check.getLeftEyeVer(),
			check.getRightEyeVer(),
			check.getLeftEyeHor(),
			check.getRightEyeHor(),
			check.getCreatedAt()
		);
	}
}