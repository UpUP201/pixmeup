from pydantic import BaseModel, Field 
# Pydantic: FastAPI가 내부적으로 사용하는 데이터 유효성 검사 라이브러리
# BaseModel: 모든 DTO의 부모 클래스
# Field: 각 필드에 추가 조건 설정

# ge: greater than equal, le: less than equal, ...: 필수(required)
class AREDSInput(BaseModel):
    user_id: int
    age: int = Field(..., ge=0, le=120)
    male: int = Field(..., ge=0, le=1)
    past_smoking: int = Field(..., ge=0, le=1)
    current_smoking: int = Field(..., ge=0, le=1)
    mchart_abnormal_flag: bool = Field(...)
    amsler_abnormal_flag: bool = Field(...)
    mchart_check_id: int
    amsler_check_id: int
    survey_id: int

class AREDSResult(BaseModel):
    id: str     # Mongo 고유 식별자 -> 응답용으로 문자열 변환 필요
    user_id: int
    risk_percent: int  # 0 ~ 100 정수
    summary: str       # 멘트 하나만 저장
    risk: str           # Low | Medium | High
    created_at: str
    mchart_check_id: int
    amsler_check_id: int
    survey_id: int

