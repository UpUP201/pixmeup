from fastapi import APIRouter
from pydantic import BaseModel
from datetime import datetime
from app.core import config
from motor.motor_asyncio import AsyncIOMotorClient
from app.schemas.model_metrics import ModelMetricsRequest
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode

router = APIRouter()

client = AsyncIOMotorClient(config.MONGO_URI)
db = client[config.MONGO_DB]
collection = db["model_metrics"]

@router.post("/model/metrics")
async def save_metrics(data: ModelMetricsRequest):
    doc = {
        "date": data.date,
        "f1_score": data.f1_score,
        "precision": data.precision,
        "recall": data.recall,
        "created_at": datetime.utcnow()
    }
    await collection.insert_one(doc)
    return {"status": "success"}

@router.get("/model/metrics/latest")
async def get_latest_metrics():
    doc = await collection.find().sort("created_at", -1).limit(1).to_list(length=1)
    if not doc:
        return CustomAppException(ErrorCode.IMAGE_RESULT_NOT_FOUND)

    data = doc[0]
    # 사용하기 쉽게 _id에서 id로 변환
    data["id"] = str(data["_id"])
    # 원본 _id는 제거
    data.pop("_id", None)
    return {"status": "success", "data": data}