from pydantic import BaseModel, Field
from typing import List

class EyeAgePredictionRequest(BaseModel):
    user_id: int = Field(..., example=42)
    history: List[int] = Field(..., min_items=6, example=[21, 22, 23, 24, 24, 25])

class EyeAgePredictionResponse(BaseModel):
    next_eye_age: int = Field(..., example=26)
