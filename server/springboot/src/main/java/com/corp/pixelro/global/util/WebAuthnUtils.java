package com.corp.pixelro.global.util;

import com.yubico.webauthn.data.ByteArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * WebAuthn 관련 유틸리티 클래스
 */
@Slf4j
@Component
public class WebAuthnUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

    private WebAuthnUtils() {
        // 인스턴스화 방지
    }

    /**
     * 32바이트 랜덤 챌린지 생성
     */
    public static ByteArray generateChallenge() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return new ByteArray(bytes);
    }

    /**
     * 사용자 ID를 SHA-256 해시하여 일관된 길이의 ID로 변환
     */
    public static ByteArray createUserHandle(String userId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(userId.getBytes(StandardCharsets.UTF_8));
            return new ByteArray(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 알고리즘을 찾을 수 없습니다", e);
            // 예상치 못한 오류이지만 최소한 작동하도록 사용자 ID를 직접 사용
            return new ByteArray(userId.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 바이트 배열을 Base64URL 형식으로 인코딩
     */
    public static String base64UrlEncode(ByteArray bytes) {
        return base64UrlEncode(bytes.getBytes());
    }

    /**
     * 바이트 배열을 Base64URL 형식으로 인코딩
     */
    public static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Base64URL 문자열을 바이트 배열로 디코딩
     */
    public static byte[] base64UrlDecode(String base64Url) {
        return Base64.getUrlDecoder().decode(base64Url);
    }

    /**
     * Base64URL 문자열을 ByteArray로 디코딩
     */
    public static ByteArray base64UrlDecodeToByteArray(String base64Url) {
        return new ByteArray(base64UrlDecode(base64Url));
    }

    public static String extractUserIdFromUserHandle(ByteArray userHandle) {
        return new String(userHandle.getBytes(), StandardCharsets.UTF_8);
    }
} 