from pydantic import BaseModel

class ModelMetricsRequest(BaseModel):
    date: str  # "2025-05-14"
    f1_score: float
    precision: float
    recall: float