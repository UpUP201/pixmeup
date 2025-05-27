package com.corp.pixelro.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record ImagePredictionRequest(
	@Schema(description = "유저 아이디")
	@JsonProperty("user_id")
	Long userId,

	@Schema(description = "이미지 S3 파일 주소")
	@JsonProperty("file_url")
	String fileUrl,

	@Schema(description = "이미지 S3 Key")
	@JsonProperty("s3_key")
	String s3Key
) {

	@Builder
	public ImagePredictionRequest(Long userId, String fileUrl, String s3Key) {
		this.userId = userId;
		this.fileUrl = fileUrl;
		this.s3Key = s3Key;
	}
}
