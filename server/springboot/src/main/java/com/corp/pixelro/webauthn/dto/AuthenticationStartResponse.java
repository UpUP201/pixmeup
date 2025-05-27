package com.corp.pixelro.webauthn.dto;

import com.corp.pixelro.webauthn.vo.CredentialDescriptor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "WebAuthn 인증 시작 응답")
public record AuthenticationStartResponse(
        @Schema(description = "base64url 인코딩된 challenge", example = "challenge-123")
        String challenge,
        
        @Schema(description = "인증 제한 시간 (밀리초)", example = "60000")
        long timeout,
        
        @Schema(description = "사용 가능한 인증정보 목록")
        List<CredentialDescriptor> allowCredentials,
        
        @Schema(description = "사용자 검증 방식", example = "preferred", allowableValues = {"required", "preferred", "discouraged"})
        String userVerification
) {
}
