package com.corp.pixmeup.external.dto;

public record UploadResponse(
        String presignedUrl,
        String s3Key
) {
}