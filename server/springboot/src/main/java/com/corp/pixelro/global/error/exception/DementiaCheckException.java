package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class DementiaCheckException extends BusinessException {
	public DementiaCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}