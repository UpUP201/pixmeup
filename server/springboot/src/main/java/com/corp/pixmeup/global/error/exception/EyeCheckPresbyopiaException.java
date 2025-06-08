package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckPresbyopiaException extends BusinessException {
	public EyeCheckPresbyopiaException(ErrorCode errorCode) {
		super(errorCode);
	}
}