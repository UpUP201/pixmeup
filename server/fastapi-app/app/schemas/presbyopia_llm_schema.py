from pydantic import BaseModel
from typing import List, Literal
from datetime import datetime

class SurveyData(BaseModel):
    user_id: int
    age: int  # 코드화된 나이값 (1~9)
    gender: Literal["M", "W"]
    glasses: bool
    surgery: Literal["normal", "correction", "cataract", "etc"]
    diabetes: bool
    smoking: bool

class PresbyopiaTest(BaseModel):
    first_distance: float
    second_distance: float
    third_distance: float
    avg_distance: float
    created_at: datetime

class PresbyopiaRequest(BaseModel):
    survey: SurveyData
    presbyopia_test_list: List[PresbyopiaTest]

class PresbyopiaAiResponse(BaseModel):
    userId: int
    comment: str
    presbyopia_status: Literal["good", "normal", "bad", "unknown"]
