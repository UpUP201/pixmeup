package com.corp.pixelro.webauthn.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * WebAuthn 응답 클래스
 * WebAuthn 작업의 결과를 클라이언트에 반환하기 위한 공통 응답 형식
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "WebAuthn 공통 응답")
public class WebAuthnResponse<T> {
    /**
     * 응답 데이터
     */
    @Schema(description = "응답 데이터")
    private T data;
    
    /**
     * 세션 ID (등록/인증 시작 시에 사용)
     */
    @Schema(description = "WebAuthn 세션 ID", example = "da4c5bfb-9f35-4666-984e-319645463f76")
    private String sessionId;
    
    /**
     * 성공 응답 생성 (메시지 없음)
     */
    public static <T> WebAuthnResponse<T> success(T data) {
        return WebAuthnResponse.<T>builder()
                .data(data)
                .build();
    }

    /**
     * 성공 응답 생성 (세션 ID 포함)
     */
    public static <T> WebAuthnResponse<T> success(T data, String sessionId) {
        return WebAuthnResponse.<T>builder()
                .data(data)
                .sessionId(sessionId)
                .build();
    }
} 