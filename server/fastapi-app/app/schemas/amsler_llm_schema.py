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

class AmslerTest(BaseModel):
    left_macular_loc: str  # e.g., "n,n,n,w,n,n,d,b,n"
    right_macular_loc: str
    created_at: datetime

class AmslerRequest(BaseModel):
    survey: SurveyData
    amsler_test_list: List[AmslerTest]
