from fastapi import APIRouter
from app.schemas.sight_prediction import SightPredictionRequest, SightPredictionResponse
from app.services.sight_prediction_service import predict_next_sight
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode

@router.post("/predict/sight", response_model=SightPredictionResponse)
async def predict_sight(request: SightPredictionRequest):
    if len(request.history) < 6:
        raise CustomAppException(ErrorCode.INVALID_INPUT_VALUE)

    try:
        return predict_next_sight(request)
    except CustomAppException as ce:
        raise ce
    except Exception:
        raise CustomAppException(ErrorCode.INTERNAL_SERVER_ERROR)
