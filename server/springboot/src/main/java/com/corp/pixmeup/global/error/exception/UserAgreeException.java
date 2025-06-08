package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class UserAgreeException extends BusinessException {
	public UserAgreeException(ErrorCode errorCode) {
		super(errorCode);
	}
}