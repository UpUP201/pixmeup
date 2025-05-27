from enum import Enum

class ErrorCode(Enum):
    # CHECK
    AMSLER_CHECK_NOT_FOUND = (404, "암슬러 검사 결과가 존재하지 않습니다.")
    MCHART_CHECK_NOT_FOUND = (404, "엠차트 검사 결과가 존재하지 않습니다.")
    SURVEY_NOT_FOUND = (404, "문진이 존재하지 않습니다.")
    INTERNAL_SERVER_ERROR = (500, "서버 오류가 발생했습니다.")
    # AREDS
    AREDS_RESULT_NOT_FOUND = (404, "AREDS 검사 결과가 존재하지 않습니다.")
    AREDS_LIST_NOT_FOUND = (200, "AREDS 검사 결과 목록이 존재하지 않습니다.")
    AREDS_RESULT_SAVE_FAILED = (500, "AREDS 검사 결과 저장에 실패했습니다.")
    #IMAGE
    IMAGE_RESULT_NOT_FOUND = (404, "이미지 분석 결과가 존재하지 않습니다.")
    IMAGE_LIST_NOT_FOUND = (200, "이미지 분석 결과 목록이 존재하지 않습니다.")
    IMAGE_RESULT_SAVE_FAILED = (500, "이미지 분석 결과 저장에 실패했습니다.")
    #TOTAL
    PREDICTION_RECORD_FETCH_FAILED = (500, "예측 기록 조회에 실패했습니다.")
    #PREDICTION
    INVALID_INPUT_VALUE = (400, "6회 이상의 검사 기록이 필요합니다.")

    def __init__(self, status, message):
        self.status = status
        self.message = message