package com.corp.pixmeup.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// Common
	INVALID_INPUT_VALUE(400, "잘못된 입력값입니다"),
	INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다"),
	UNAUTHORIZED(401, "인증되지 않은 접근입니다"),
	FORBIDDEN(403, "권한이 없습니다"),
	INVALID_ENUM_VALUE(400, "잘못된 상태값입니다"),
	TOO_MANY_REQUESTS(429, "너무 많은 요청이 발생했습니다. 잠시 후 다시 시도해주세요"),
	BAD_REQUEST(400, "잘못된 접근입니다."),
	HASH_GENERATION_FAILED(500, "해시 생성에 실패했습니다."),

	// Auth & User
	INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),
	EXPIRED_TOKEN(401, "만료된 토큰입니다"),
	REFRESH_TOKEN_NOT_FOUND(401, "리프레시 토큰을 찾을 수 없습니다"),
	EMAIL_DUPLICATE(400, "이미 존재하는 이메일입니다"),
	INVALID_PASSWORD(400, "잘못된 비밀번호입니다"),
	INVALID_ROLE_TYPE(400, "잘못된 권한 타입입니다"),
	LOGIN_BAD_CREDENTIALS(401, "아이디 또는 비밀번호가 일치하지 않습니다"),
	LOGIN_FAILED(401, "로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요"),
	USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다"),
	USER_SEARCH_ERROR(500, "사용자 정보 조회 중 오류가 발생했습니다"),
	INVALID_EMAIL_VERIFICATION(401, "유효하지 않은 인증 토큰입니다"),
	EXPIRED_EMAIL_VERIFICATION(401, "만료된 인증 토큰입니다"),
	DUPLICATE_EMAIL(400, "이미 등록된 이메일입니다"),
	FAIL_MESSAGE_SEND(500, "문자 발송에 실패했습니다"),
	PHONE_VERIFICATION_FAILED(400, "휴대전화 인증에 실패했습니다"),
	PHONE_VERIFICATION_EXPIRED(400, "휴대전화 인증이 만료되었습니다"),
	PHONE_ALREADY_REGISTERED(400, "이미 등록된 휴대전화 번호입니다"),
	LOGOUT_FAILED(500, "로그아웃 처리 중 오류가 발생했습니다"),
	NOT_AUTHENTICATED(401, "인증되지 않은 사용자입니다."),
	USER_ALREADY_EXISTS(409, "이미 가입된 사용자입니다."),
	INVALID_USER_ID(400, "잘못된 유저 ID 입니다."),

	// Check
	CHECK_RESULT_SAVE_FAILED(500, "검사 결과 저장에 실패했습니다"),
	CHECK_LIST_NOT_FOUND(200, "검사 결과 목록이 존재하지 않습니다."),
	SIGHT_CHECK_NOT_FOUND(404, "시력검사 결과가 존재하지 않습니다."),
	SIGHT_LIST_NOT_FOUND(200, "시력검사 결과 목록이 존재하지 않습니다."),
	AMSLER_CHECK_NOT_FOUND(404, "암슬러 검사 결과가 존재하지 않습니다."),
	AMSLER_LIST_NOT_FOUND(200, "암슬러 검사 결과 목록이 존재하지 않습니다."),
	MCHART_CHECK_NOT_FOUND(404, "엠차트 검사 결과가 존재하지 않습니다."),
	MCHART_LIST_NOT_FOUND(200, "엠차트 결과 목록이 존재하지 않습니다."),
	PRESBYOPIA_CHECK_NOT_FOUND(404, "노안검사 결과가 존재하지 않습니다."),
	PRESBYOPIA_LIST_NOT_FOUND(200, "노안검사 결과 목록이 존재하지 않습니다."),

	// Survey
	SURVEY_SAVE_FAILED(500, "문진표 저장에 실패했습니다."),
	SURVEY_NOT_FOUND(404, "문진이 존재하지 않습니다."),
	SURVEY_LIST_NOT_FOUND(200, "문진 목록이 존재하지 않습니다."),

	// Kiosk
	KIOSK_NOT_FOUND(404, "존재하지 않는 키오스크입니다."),

	// BloodPressureCheck
	BLOOD_PRESSURE_CHECK_NOT_FOUND(404, "혈압 검사 결과를 찾을 수 없습니다."),
	BLOOD_PRESSURE_CHECK_INSERT_FAILED(400, "혈압 검사 결과를 생성 하는데 실패 했습니다."),
	BLOOD_PRESSURE_CHECK_DELETE_FAILED(400, "혈압 검사 결과를 삭제 하는데 실패 했습니다."),

	// BodyCompositionCheck
	BODY_COMPOSITION_CHECK_NOT_FOUND(404, "인바디 검사 결과를 찾을 수 없습니다."),
	BODY_COMPOSITION_CHECK_INSERT_FAILED(400, "인바디 검사 결과를 생성 하는데 실패 했습니다."),
	BODY_COMPOSITION_CHECK_DELETE_FAILED(400, "인바디 검사 결과를 삭제 하는데 실패 했습니다."),

	// GripStrengthCheck
	GRIP_STRENGTH_CHECK_NOT_FOUND(404, "악력 검사 결과를 찾을 수 없습니다."),
	GRIP_STRENGTH_CHECK_INSERT_FAILED(400, "악력 검사 결과를 생성 하는데 실패 했습니다."),
	GRIP_STRENGTH_CHECK_DELETE_FAILED(400, "악력 검사 결과를 삭제 하는데 실패 했습니다."),

	// DementiaCheck
	DEMENTIA_CHECK_NOT_FOUND(404, "치매 검사 결과를 찾을 수 없습니다."),
	DEMENTIA_CHECK_INSERT_FAILED(400, "치매 검사 결과를 생성 하는데 실패 했습니다."),
	DEMENTIA_CHECK_DELETE_FAILED(400, "치매 검사 결과를 삭제 하는데 실패 했습니다."),

	// Webauthn
	INVALID_AUTH_TYPE(400, "유효하지 않은 인증 정보입니다."),
	STRONG_AUTH_REQUIRED(401, "인증이 거부되었습니다."),
	RECENT_AUTH_REQUIRED(401, "인증이 거부되었습니다."),
	CREDENTIAL_NOT_FOUND(404, "존재하지 않는 인증 정보입니다."),
	INVALID_SESSION(404, "인증 세션을 찾을 수 없습니다."),
	INVALID_SESSION_OPERATION(400, "세션 작업이 잘못되었습니다."),

	// EyeExercise
	EYE_EXERCISE_NOT_FOUND(404, "해당하는 눈 운동 정보를 찾을 수 없습니다."),
	EYE_EXERCISE_STEP_NOT_FOUND(404, "해당하는 눈 운동 순서 정보를 찾을 수 없습니다."),
	EYE_EXERCISE_RECORD_NOT_FOUND(404, "해당하는 눈 운동 기록을 찾을 수 없습니다."),

	// S3
	S3_INPUT_NOT_VAILD(400, "S3 기능을 이용 하기 위한 입력값이 올바르지 않습니다."),

	// SSE
	INVALID_SSE_ID(400,"유효하지 않은 SSE ID 입니다."),
	IVALID_SSE_USER(400, "현재 로그인된 유저와 SSE를 요청한 유저의 ID가 일치하지 않습니다."),

	// AMD 예측 예외 상황
	SURVEY_NOT_EXISTS(200, "문진 결과가 존재하지 않아\nAMD 예측이 불가합니다."),
	AMSLER_NOT_EXISTS(200, "암슬러 검사 결과가 존재하지 않아\n AMD 예측이 불가합니다."),
	MCHART_NOT_EXISTS(200, "엠식 검사 결과가 존재하지 않아\n AMD 예측이 불가합니다."),

	// FastAPI 예외
	FAST_API_ERROR(400, "FAST_API에서 예외가 발생했습니다.")
	;

	private final int status;
	private final String message;
}
