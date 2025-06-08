package com.corp.pixmeup.global.util;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.exception.BusinessException;
import com.corp.pixmeup.global.vo.CustomUserDetails;
import com.corp.pixmeup.user.entity.User;
import com.corp.pixmeup.user.repository.UserRepository;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private int accessExpireSec;

    @Value("${jwt.refreshTokenExpiration}")
    private int refreshExpireSec;

    private SecretKey key;

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Access Token 생성
    public String generateAccessToken(Long userId) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + accessExpireSec * 1000L);

        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // String userId를 받는 오버로딩 메서드 추가
    public String generateAccessToken(String userId) {
        return generateAccessToken(Long.parseLong(userId));
    }

    // 클레임을 포함한 토큰 생성
    public String generateTokenWithClaims(Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + accessExpireSec * 1000L);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // 토큰에서 모든 클레임 추출
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long userId) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + refreshExpireSec * 1000L);

        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 요청에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검증 (블랙리스트로 등록되어 있는지?)
    public boolean validateToken(String token) {
        try {
            String blacklistKey = "jwt:blacklist:" + token;
            if (redisTemplate.hasKey(blacklistKey)) {
                return false;
            }

            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return !claims.getPayload().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 userId 조회
    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId", Long.class);
    }

    // String 형태로 userId 반환
    public String getUserIdFromToken(String token) {
        Long userId = getUserId(token);
        return userId != null ? String.valueOf(userId) : null;
    }

    // 만료된 토큰에서 userId 조회
    public Long getUserIdFromExpiredToken(String token) {
        try {
            // 아직 안 만료된 토큰이면 그냥 클레임 파싱
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("userId", Long.class);

        } catch (ExpiredJwtException e) {
            // 만료된 토큰이어도 e.getClaims()로 클레임에 접근 가능
            Claims claims = e.getClaims();
            return claims.get("userId", Long.class);
        }
    }

    public long getRefreshTokenExpiresIn() {
        return refreshExpireSec;
    }

    // 토큰에서 CustomUserDetails 정보 조회 (DB 접근 필요)
    public UserDetails getUserDetailsFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Long userId = claims.get("userId", Long.class);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }

    // 토큰을 블랙리스트에 추가 (로그아웃 시 사용)
    public void addTokenToBlacklist(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(
                    "jwt:blacklist:" + token,
                    "logout",
                    ttl,
                    java.util.concurrent.TimeUnit.MILLISECONDS
            );
        }
    }

}
