package com.corp.pixmeup.external.util;

import java.util.UUID;

import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.exception.UserException;

public class EmitterUtil {

	public static String generateEmitterIdByUserId(Long userId) {
		if(userId == null) {
			throw new UserException(ErrorCode.INVALID_USER_ID);
		}
		UUID uuid = UUID.randomUUID();
		return userId + "_" + uuid;
	}

	public static Long getUserIdFromEmitterId(String emitterId) {
		if(emitterId == null || emitterId.isBlank()) {
			throw new UserException(ErrorCode.INVALID_SSE_ID);
		}
		String userIdStr = emitterId.split("_")[0];
		if(userIdStr == null || userIdStr.isBlank()) {
			throw new UserException(ErrorCode.INVALID_SSE_ID);
		}
		return Long.parseLong(userIdStr);
	}
}
