package com.corp.pixmeup.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ImageUrlPredictionRequest(
	@Schema(description = "S3 에 저장된 이미지의 Key", example = "user_data/images/eye_image.png")
	String s3Key
) { }
