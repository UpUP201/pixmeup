from typing import Optional

from fastapi import APIRouter   # FastAPI의 라우터 객체를 만들기 위한 클래스
from app.schemas.areds import AREDSInput, AREDSResult
from app.services.areds_service import save_areds_result, get_areds_result, get_latest_areds_by_user, make_areds_summary, map_to_model_input   # service 계층 함수 불러옴
from app.models.areds_predictor import predict_amd_risk
from datetime import datetime

router = APIRouter()    # 라우터 객체 생성 (@RestController 클래스 생성과 비슷)

# response_model: 이 함수의 리턴값을 자동 검증 + 문서화
@router.post("/predict/areds", response_model=AREDSResult)
async def insert_areds_result(request: AREDSInput):
    model_input = map_to_model_input(request.dict())
    result = predict_amd_risk(model_input)
    risk_percent = result["risk_percent"]
    risk = result["risk"] 
    summary = make_areds_summary(risk_percent)
    # 입력, 예측 결과, 사용자 아이디, 생성시간을 하나의 dict로 병합합
    record = request.dict() | result | {
        "user_id": request.user_id,
        "risk_percent": risk_percent,
        "summary": summary,
        "risk": risk,
        "created_at": datetime.utcnow(),
        "mchart_check_id": request.mchart_check_id,
        "amsler_check_id": request.amsler_check_id,
        "survey_id": request.survey_id
    }
    # MongoDB에 저장
    result_id = await save_areds_result(record)
    return {
        "id": result_id,
        "user_id": request.user_id,
        "risk_percent": risk_percent,
        "summary": summary,
        "risk": risk,
        "created_at": record["created_at"].isoformat(),
        "mchart_check_id": request.mchart_check_id,
        "amsler_check_id": request.amsler_check_id,
        "survey_id": request.survey_id
    }

@router.get("/predict/areds/{result_id}", response_model=AREDSResult)
async def select_areds_result(result_id: str):
    doc = await get_areds_result(result_id)

    return doc

@router.get("/predict/areds/latest/{user_id}", response_model=Optional[AREDSResult])
async def select_latest_areds_by_user(user_id: int):
    doc = await get_latest_areds_by_user(user_id)
    
    return doc