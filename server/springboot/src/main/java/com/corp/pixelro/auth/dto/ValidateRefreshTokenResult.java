package com.corp.pixelro.auth.dto;

import com.corp.pixelro.auth.entity.RefreshToken;

public record ValidateRefreshTokenResult(
        boolean success,
        String accessToken,
        RefreshToken refreshToken
) {
}
