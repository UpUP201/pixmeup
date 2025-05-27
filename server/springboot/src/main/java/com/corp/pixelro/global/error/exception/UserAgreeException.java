package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class UserAgreeException extends BusinessException {
	public UserAgreeException(ErrorCode errorCode) {
		super(errorCode);
	}
}