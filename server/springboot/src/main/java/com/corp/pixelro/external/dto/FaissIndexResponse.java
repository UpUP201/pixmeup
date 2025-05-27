package com.corp.pixelro.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record FaissIndexResponse(
	@Schema(description = "faiss index URL")
	@JsonProperty("faiss_url")
	String faissUrl,

	@Schema(description = "faiss pkl URL")
	@JsonProperty("pkl_url")
	String pklUrl
) {

	@Builder
	public FaissIndexResponse(String faissUrl, String pklUrl) {
		this.faissUrl = faissUrl;
		this.pklUrl = pklUrl;
	}
}
