from fastapi import APIRouter, UploadFile, Form, HTTPException, File, Body
from fastapi.responses import JSONResponse
from app.services.image_service import predict_image_service, get_image_result, get_latest_image_result
from app.schemas.image import ImagePredictionResponse, ImagePredictionRequest
from datetime import datetime
from app.core.model_loader import download_latest_model_from_s3
from app.models.yolo import reload_model
import logging

router = APIRouter()
logger = logging.getLogger(__name__)  # 파이썬 기본 logging 사용

@router.get("/predict/image/{result_id}", response_model=ImagePredictionResponse)
async def get_image_prediction(result_id: str):
    doc = await get_image_result(result_id)
    
    if "created_at" in doc and isinstance(doc["created_at"], datetime):
        doc["created_at"] = doc["created_at"].isoformat()

    return doc

@router.get("/predict/image/latest/{user_id}", response_model=ImagePredictionResponse)
async def get_latest_image_prediction(user_id: int):
    doc = await get_latest_image_result(user_id)

    if "created_at" in doc and isinstance(doc["created_at"], datetime):
        doc["created_at"] = doc["created_at"].isoformat()

    return doc

@router.post("/predict/image", response_model=ImagePredictionResponse)
async def predict_image(body: ImagePredictionRequest = Body(...)):
    logger.info("📥 [/predict/image] 요청 수신 - user_id: %s, s3_key: %s", body.user_id, body.s3_key)
    print("✅ FastAPI /predict/image 요청 수신 -", body.dict())  # 콘솔에 찍고 싶다면
    return await predict_image_service(
        s3_key=body.s3_key,
        file_url=body.file_url,
        user_id=body.user_id
    )

@router.post("/reload-model")
def reload_model_api():
    download_latest_model_from_s3()
    reload_model()  # yolo.py 안에 있는 리로딩
    return {"status": "success", "message": "모델 교체 및 인스턴스 갱신 완료"}
