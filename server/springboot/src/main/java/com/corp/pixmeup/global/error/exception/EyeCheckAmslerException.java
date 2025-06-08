package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckAmslerException extends BusinessException {
	public EyeCheckAmslerException(ErrorCode errorCode) {
		super(errorCode);
	}
}