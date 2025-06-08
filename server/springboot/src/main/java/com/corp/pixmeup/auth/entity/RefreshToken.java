package com.corp.pixmeup.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash("refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String token; // JWT 토큰 문자열

    @Indexed
    private Long userId;  // 토큰 소유자

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long ttl = 7L; // 만료시간: 7일

    @Builder
    public RefreshToken(String token, Long userId, Long ttl) {
        this.token = token;
        this.userId = userId;
        this.ttl = ttl;
    }
}