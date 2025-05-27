from pydantic import BaseModel
from typing import List, Literal
from datetime import datetime

class SurveyData(BaseModel):
    user_id: int
    age: int
    gender: Literal["M", "W"]
    glasses: bool
    surgery: Literal["normal", "correction", "cataract", "etc"]
    diabetes: bool
    smoking: bool

class MChartTest(BaseModel):
    left_eye_ver: int
    right_eye_ver: int
    left_eye_hor: int
    right_eye_hor: int
    created_at: datetime

class MChartRequest(BaseModel):
    survey: SurveyData
    mchart_test_list: List[MChartTest]
