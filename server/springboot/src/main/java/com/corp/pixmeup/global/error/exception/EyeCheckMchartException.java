package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckMchartException extends BusinessException {
	public EyeCheckMchartException(ErrorCode errorCode) {
		super(errorCode);
	}
}