package com.corp.pixmeup.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "WebAuthn 인증정보 설명자")
public record CredentialDescriptor(
        @Schema(description = "인증정보 타입", example = "public-key")
        String type,                
        
        @Schema(description = "인증정보 ID (base64url 인코딩)", example = "credential-123")
        String id,                 
        
        @Schema(description = "전송 방식 목록", example = "[\"internal\"]")
        List<String> transports
) {
}
