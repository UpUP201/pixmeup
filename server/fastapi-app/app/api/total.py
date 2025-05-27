from fastapi import APIRouter, Query
from typing import List
from app.schemas.total import PredictionRecord
from app.services.total_service import get_prediction_records

router = APIRouter()

@router.get("/predict/records", response_model=List[PredictionRecord])
async def get_prediction_records_api(
    user_id: int,
    page: int = Query(0),
    size: int = Query(10)
):
    return await get_prediction_records(user_id, page, size)
