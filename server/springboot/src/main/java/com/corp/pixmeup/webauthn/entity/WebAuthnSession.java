package com.corp.pixmeup.webauthn.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@RedisHash("webauthn_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WebAuthnSession implements Serializable {

    @Id
    private String id;
    
    @Indexed
    private Long userId;
    
    @Indexed
    private String challenge;
    
    /**
     * 세션 유형: "REGISTRATION" 또는 "AUTHENTICATION"
     */
    private String operation;
    
    /**
     * 요청 시 생성된 JSON 원본
     */
    private String requestJson;
    
    /**
     * 사용자 요청 IP
     */
    private String clientIp;
    
    /**
     * 사용자 브라우저 정보
     */
    private String userAgent;
    
    /**
     * 생성 시간
     */
    private long createdAt;

    @Builder.Default
    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long ttl = 5L; // 5분
    
    public WebAuthnSession withChallenge(String challenge) {
        this.challenge = challenge;
        return this;
    }
} 