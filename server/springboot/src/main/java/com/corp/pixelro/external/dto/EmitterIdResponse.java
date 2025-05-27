package com.corp.pixelro.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record EmitterIdResponse(
	@Schema(description = "SSE와 통신하기 위한 Emitter Id(userId_UUID형식)", example = "123_f9c6a810-403a-46e7-95cf-cba520b57ba6")
	String emitterId
) {
	@Builder
	public EmitterIdResponse(String emitterId) {
		this.emitterId = emitterId;
	}
}
