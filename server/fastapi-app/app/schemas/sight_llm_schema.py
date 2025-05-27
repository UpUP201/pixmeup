from pydantic import BaseModel
from typing import List, Literal
from datetime import datetime

class SurveyData(BaseModel):
    user_id: int
    age: int  # 이미 코드화된 나이값이 들어옴 (1~9)
    gender: Literal["M", "W"]
    glasses: bool
    surgery: Literal["normal", "correction", "cataract", "etc"]
    diabetes: bool
    smoking: bool

class SightTest(BaseModel):
    distance: float
    left_sight: int  # 예: 2 → 0.2
    right_sight: int
    created_at: datetime

class SightRequest(BaseModel):
    survey: SurveyData
    sight_test_list: List[SightTest]