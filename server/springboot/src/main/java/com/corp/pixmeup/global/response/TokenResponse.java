package com.corp.pixmeup.global.response;

import com.corp.pixmeup.auth.entity.RefreshToken;

public record TokenResponse<T> (
        RefreshToken refreshToken,
        T data
){
}
