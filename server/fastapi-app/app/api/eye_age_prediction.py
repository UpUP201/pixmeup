from fastapi import APIRouter
from app.schemas.eye_age_prediction import EyeAgePredictionRequest, EyeAgePredictionResponse
from app.services.eye_age_prediction_service import predict_next_eye_age
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode

router = APIRouter(prefix="/predict/eye-age", tags=["Eye Age Prediction"])

@router.post("", response_model=EyeAgePredictionResponse)
async def predict_eye_age(request: EyeAgePredictionRequest):
    if len(request.history) < 6:
        raise CustomAppException(ErrorCode.INVALID_INPUT_VALUE)

    try:
        return predict_next_eye_age(request)
    except CustomAppException as ce:
        raise ce
    except Exception:
        raise CustomAppException(ErrorCode.INTERNAL_SERVER_ERROR)
