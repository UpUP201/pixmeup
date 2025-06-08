package com.corp.pixmeup.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 인증 응답")
public record AssertionResponse(
        @Schema(description = "인증기 데이터 (base64url 인코딩)", example = "eyJ0eXBlIjoid2ViYXV0aG4uZ2V0In0...")
        String authenticatorData,       
        
        @Schema(description = "클라이언트 데이터 (base64url 인코딩)", example = "eyJ0eXBlIjoid2ViYXV0aG4uZ2V0In0...")
        String clientDataJSON,          
        
        @Schema(description = "서명 데이터 (base64url 인코딩)", example = "MEUCIQ...")
        String signature,               
        
        @Schema(description = "사용자 핸들 (base64url 인코딩, 선택사항)", example = "dXNlcjEyMw")
        String userHandle               
) {
}