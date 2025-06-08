package com.corp.pixmeup.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 인증기 선택 옵션")
public record AuthenticatorSelection(
        @Schema(description = "인증기 부착 방식", example = "platform", allowableValues = {"platform", "cross-platform"})
        String authenticatorAttachment,  
        
        @Schema(description = "사용자 검증 방식", example = "preferred", allowableValues = {"required", "preferred", "discouraged"})
        String userVerification,        
        
        @Schema(description = "Resident Key 요구사항", example = "false", allowableValues = {"true", "false"})
        String residentKey       
) {
}
