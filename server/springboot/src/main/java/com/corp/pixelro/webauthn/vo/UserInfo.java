package com.corp.pixelro.webauthn.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 사용자 정보")
public record UserInfo(
        @Schema(description = "사용자 ID (base64url 인코딩)", example = "dXNlcjEyMw")
        String id,         
        
        @Schema(description = "사용자 이름", example = "user@example.com")
        String name,       
        
        @Schema(description = "표시 이름", example = "홍길동")
        String displayName 
) {
}
