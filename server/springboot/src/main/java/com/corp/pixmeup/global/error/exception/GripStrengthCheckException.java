package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class GripStrengthCheckException extends BusinessException {
	public GripStrengthCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}