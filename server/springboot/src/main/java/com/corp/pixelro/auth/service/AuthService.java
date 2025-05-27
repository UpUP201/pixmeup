package com.corp.pixelro.auth.service;

import com.corp.pixelro.auth.dto.*;
import com.corp.pixelro.global.response.TokenResponse;
import jakarta.validation.Valid;

public interface AuthService {
    PhoneAuthSendCodeResponse sendVerificationCode(PhoneAuthSendCodeRequest request);

    PhoneAuthVerifyCodeResponse verifyCode(PhoneAuthVerifyCodeRequest request);

    RegisterResponse registerUser(@Valid RegisterRequest request);

    TokenResponse<LoginResponse> loginUser(@Valid LoginRequest request);

    void logout(Long userId, String accessToken);

    ValidateRefreshTokenResult validateRefreshToken(String refreshToken);
}
