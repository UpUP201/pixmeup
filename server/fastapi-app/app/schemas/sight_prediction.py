from pydantic import BaseModel, Field
from typing import List

class SightRecord(BaseModel):
    date: str = Field(..., example="2024-01-01")
    left_sight: int = Field(..., ge=1, le=20, example=8)  # 0.8 → 8
    right_sight: int = Field(..., ge=1, le=20, example=9)

class SightPredictionRequest(BaseModel):
    user_id: int = Field(..., example=42)
    history: List[SightRecord] = Field(..., description="최소 6회 이상의 시력 측정 기록")

class SightPredictionResponse(BaseModel):
    next_left_sight: int = Field(..., example=7)   # 예측된 0.7 → 7
    next_right_sight: int = Field(..., example=8)