package com.corp.pixmeup.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 공개키 인증정보 파라미터")
public record PubKeyCredParam(
        @Schema(description = "인증정보 타입", example = "public-key")
        String type,      
        
        @Schema(description = "알고리즘 식별자", example = "-7")
        long alg           
) {
}
