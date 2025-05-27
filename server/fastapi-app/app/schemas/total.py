from pydantic import BaseModel
from typing import Literal

class PredictionRecord(BaseModel):
    id: str
    created_at: str
    type: Literal["AREDS", "IMAGE"]