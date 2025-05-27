package com.corp.pixelro.external.dto;

public record UploadResponse(
        String presignedUrl,
        String s3Key
) {
}