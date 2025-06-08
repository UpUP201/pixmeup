package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class UserException extends BusinessException {
	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}