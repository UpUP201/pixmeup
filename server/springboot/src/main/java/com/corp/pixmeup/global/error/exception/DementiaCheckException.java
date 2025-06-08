package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class DementiaCheckException extends BusinessException {
	public DementiaCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}