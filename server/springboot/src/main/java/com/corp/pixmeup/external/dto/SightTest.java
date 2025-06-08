package com.corp.pixmeup.external.dto;

import java.time.LocalDateTime;

import com.corp.pixmeup.check.entity.SightCheck;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SightTest(
	float distance,

	@JsonProperty("left_sight")
	Integer leftSight,

	@JsonProperty("right_sight")
	Integer rightSight,

	@JsonProperty("created_at")
	LocalDateTime createdAt
) {
	public static SightTest from(SightCheck check) {
		return new SightTest(
			40f,
			check.getLeftSight(),
			check.getRightSight(),
			check.getCreatedAt()
		);
	}
}
