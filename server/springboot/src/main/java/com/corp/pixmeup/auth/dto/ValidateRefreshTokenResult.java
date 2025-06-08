package com.corp.pixmeup.auth.dto;

import com.corp.pixmeup.auth.entity.RefreshToken;

public record ValidateRefreshTokenResult(
        boolean success,
        String accessToken,
        RefreshToken refreshToken
) {
}
