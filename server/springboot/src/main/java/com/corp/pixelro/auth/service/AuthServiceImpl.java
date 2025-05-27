package com.corp.pixelro.auth.service;

import com.corp.pixelro.auth.dto.*;
import com.corp.pixelro.auth.entity.RefreshToken;
import com.corp.pixelro.auth.repository.RefreshTokenRepository;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.BusinessException;
import com.corp.pixelro.global.response.TokenResponse;
import com.corp.pixelro.global.util.JwtTokenProvider;
import com.corp.pixelro.global.util.PhoneUtils;
import com.corp.pixelro.user.entity.User;
import com.corp.pixelro.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public PhoneAuthSendCodeResponse sendVerificationCode(PhoneAuthSendCodeRequest request) {
        SecureRandom random = new SecureRandom();
        String code = (100000 + random.nextInt(900000)) + "";
        String phoneNumber = PhoneUtils.normalizePhoneNumber(request.phoneNumber());

        boolean success = messageService.sendVerificationCode(phoneNumber, code);

        if (!success) {
            throw new BusinessException(ErrorCode.FAIL_MESSAGE_SEND);
        }

        redisTemplate.opsForValue()
                .set(phoneNumber, code, Duration.ofMinutes(3));

        return new PhoneAuthSendCodeResponse(
                true,
                180
        );
    }

    @Override
    public PhoneAuthVerifyCodeResponse verifyCode(PhoneAuthVerifyCodeRequest request) {
        String phoneNumber = PhoneUtils.normalizePhoneNumber(request.phoneNumber());
        String code = request.verificationCode();

        Object saved = redisTemplate.opsForValue().get(phoneNumber);
        if (saved == null) {
            throw new BusinessException(ErrorCode.PHONE_VERIFICATION_EXPIRED);
        }

        String savedCode = String.valueOf(saved);
        if (!Objects.equals(savedCode, code)) {
            throw new BusinessException(ErrorCode.PHONE_VERIFICATION_FAILED);
        }

        redisTemplate.delete(phoneNumber);

        return new PhoneAuthVerifyCodeResponse(
                true
        );
    }

    @Override
    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElse(null);

        if (user != null) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        user = User.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName()
        );
    }

    @Override
    @Transactional
    public TokenResponse<LoginResponse> loginUser(LoginRequest request) {
        String phoneNumber = PhoneUtils.normalizePhoneNumber(request.phoneNumber());

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Long userId = user.getId();

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_BAD_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtTokenProvider.generateRefreshToken(userId))
                .userId(userId)
                .ttl(jwtTokenProvider.getRefreshTokenExpiresIn())
                .build();

        refreshTokenRepository.save(refreshToken);

        return new TokenResponse<>(
                refreshToken,
                new LoginResponse(userId,
                        user.getName(),
                        accessToken
                )
        );
    }

    @Override
    @Transactional
    public void logout(Long userId, String accessToken) {
        jwtTokenProvider.addTokenToBlacklist(accessToken);
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public ValidateRefreshTokenResult validateRefreshToken(String refreshToken) {
        // 1. RefreshToken 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 2. userId
        Long userId = jwtTokenProvider.getUserId(refreshToken);

        // 3. DB에 저장된 RefreshToken과 일치?
        RefreshToken savedToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
        if (!savedToken.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 4. 새 토큰
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshTokenStr = jwtTokenProvider.generateRefreshToken(userId);

        // 5. 기존꺼 삭제, 새 RefreshToken 저장
        refreshTokenRepository.deleteByUserId(userId);
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenStr)
                .userId(userId)
                .ttl(jwtTokenProvider.getRefreshTokenExpiresIn())
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // 6. 결과 반환
        return new ValidateRefreshTokenResult(
                true,
                newAccessToken,
                newRefreshToken
        );
    }
}
