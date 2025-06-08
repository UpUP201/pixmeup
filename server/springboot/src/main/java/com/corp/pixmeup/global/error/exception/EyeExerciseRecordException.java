package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeExerciseRecordException extends BusinessException {
	public EyeExerciseRecordException(ErrorCode errorCode) {
		super(errorCode);
	}
}