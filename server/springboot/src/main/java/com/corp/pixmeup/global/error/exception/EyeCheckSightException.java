package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckSightException extends BusinessException {
	public EyeCheckSightException(ErrorCode errorCode) {
		super(errorCode);
	}
}