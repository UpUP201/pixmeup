package com.corp.pixmeup.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 등록 응답")
public record AttestationResponse(
        @Schema(description = "인증 객체 (공개키, 인증 장치 정보, 서명 등 포함, base64url 인코딩)", example = "o2NmbXRkbm9uZWdhdHRTdG10oGhhdXRoRGF0YVj...")
        String attestationObject,   
        @Schema(description = "클라이언트 데이터 (브라우저가 생성한 서명 데이터, base64url 인코딩)", example = "eyJ0eXBlIjoid2ViYXV0aG4uY3JlYXRlIn0...")
        String clientDataJSON       
) {
}
