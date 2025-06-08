package com.corp.pixmeup.auth.service;

import com.corp.pixmeup.auth.dto.*;
import com.corp.pixmeup.global.response.TokenResponse;
import jakarta.validation.Valid;

public interface AuthService {
    PhoneAuthSendCodeResponse sendVerificationCode(PhoneAuthSendCodeRequest request);

    PhoneAuthVerifyCodeResponse verifyCode(PhoneAuthVerifyCodeRequest request);

    RegisterResponse registerUser(@Valid RegisterRequest request);

    TokenResponse<LoginResponse> loginUser(@Valid LoginRequest request);

    void logout(Long userId, String accessToken);

    ValidateRefreshTokenResult validateRefreshToken(String refreshToken);
}
