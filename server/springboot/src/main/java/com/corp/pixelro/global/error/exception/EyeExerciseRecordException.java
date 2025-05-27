package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeExerciseRecordException extends BusinessException {
	public EyeExerciseRecordException(ErrorCode errorCode) {
		super(errorCode);
	}
}