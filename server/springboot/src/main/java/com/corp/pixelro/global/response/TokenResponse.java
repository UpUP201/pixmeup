package com.corp.pixelro.global.response;

import com.corp.pixelro.auth.entity.RefreshToken;

public record TokenResponse<T> (
        RefreshToken refreshToken,
        T data
){
}
