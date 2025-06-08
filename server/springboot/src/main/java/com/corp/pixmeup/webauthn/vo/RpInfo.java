package com.corp.pixmeup.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn Relying Party 정보")
public record RpInfo(
        @Schema(description = "도메인", example = "localhost")
        String id,     
        @Schema(description = "서비스명", example = "Pixelro")
        String name    
) {
}
