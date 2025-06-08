package com.corp.pixmeup.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "GlobalResponse", description = "API 표준 응답 래퍼")
public class GlobalResponse<T> {
    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int status;

    @Schema(description = "메시지", example = "Success")
    private final String message;

    @Schema(description = "응답 데이터")
    private final T data;

    @Schema(description = "응답 시간", example = "2025-05-08 09:51:02")
    private final LocalDateTime timestamp;

    private GlobalResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;

        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 성공 응답
    public static <T> GlobalResponse<T> success(T data) {
        return new GlobalResponse<>(200, "Success", data);
    }

    public static <T> GlobalResponse<T> success(String message, T data) {
        return new GlobalResponse<>(200, message, data);
    }

    public static <T> GlobalResponse<T> created(T data) {
        return new GlobalResponse<>(201, "Created", data);
    }

    public static <T> GlobalResponse<T> created(String message, T data) {
        return new GlobalResponse<>(201, message, data);
    }

    public static <T> GlobalResponse<T> noContent(String message, T data) {
        return new GlobalResponse<>(204, message, data);
    }
}
