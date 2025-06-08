package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class S3Exception extends BusinessException {
	public S3Exception(ErrorCode errorCode) {
		super(errorCode);
	}
}