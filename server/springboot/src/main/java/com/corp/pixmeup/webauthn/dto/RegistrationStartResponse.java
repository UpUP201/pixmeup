package com.corp.pixmeup.webauthn.dto;

import com.corp.pixmeup.webauthn.vo.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "WebAuthn 등록 시작 응답")
public record RegistrationStartResponse(
        @Schema(description = "base64url 인코딩된 challenge", example = "challenge-123")
        String challenge,
        
        @Schema(description = "Relying Party 정보")
        RpInfo rp,
        
        @Schema(description = "사용자 정보")
        UserInfo user,
        
        @Schema(description = "지원하는 공개키 알고리즘 목록")
        List<PubKeyCredParam> pubKeyCredParams,
        
        @Schema(description = "등록 제한 시간 (밀리초)", example = "60000")
        long timeout,
        
        @Schema(description = "제외할 인증정보 목록")
        List<CredentialDescriptor> excludeCredentials,
        
        @Schema(description = "인증기 선택 옵션")
        AuthenticatorSelection authenticatorSelection,
        
        @Schema(description = "인증 방식", example = "none", allowableValues = {"none", "indirect", "direct"})
        String attestation
) {
}
