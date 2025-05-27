from fastapi import APIRouter
from pydantic import BaseModel
from datetime import datetime
from app.core import config
from motor.motor_asyncio import AsyncIOMotorClient

router = APIRouter()

client = AsyncIOMotorClient(config.MONGO_URI)
db = client[config.MONGO_DB]
collection = db["model_metrics"]

class ModelMetricsRequest(BaseModel):
    date: str  # "2025-05-14"
    f1_score: float
    precision: float
    recall: float

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

@router.get("/ml/model/metrics/latest")
async def get_latest_metrics():
    doc = await collection.find().sort("created_at", -1).limit(1).to_list(length=1)
    if not doc:
        return {"status": "error", "message": "데이터 없음"}

    data = doc[0]
    data["id"] = str(data["_id"])
    data.pop("_id", None)
    return {"status": "success", "data": data}