package com.corp.pixelro.external.dto;

import java.time.LocalDateTime;

import com.corp.pixelro.check.entity.AmslerCheck;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record AmslerTest(
	@Schema(description = "왼쪽 눈 황반 위치 정보 (쉼표 구분 9개)", example = "n,n,n,w,n,n,d,b,n")
	@JsonProperty("left_macular_loc")
	String leftMacularLoc,

	@Schema(description = "오른쪽 눈 황반 위치 정보 (쉼표 구분 9개)", example = "n,n,n,n,n,n,n,n,n")
	@JsonProperty("right_macular_loc")
	String rightMacularLoc,

	@Schema(description = "검사 일시", example = "2025-05-08T15:20:00")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonProperty("created_at")
	LocalDateTime createdAt
) {
	public static AmslerTest from(AmslerCheck check) {
		return new AmslerTest(
			check.getLeftMacularLoc(),
			check.getRightMacularLoc(),
			check.getCreatedAt()
		);
	}
}