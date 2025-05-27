package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeExerciseException extends BusinessException {
	public EyeExerciseException(ErrorCode errorCode) {
		super(errorCode);
	}
}