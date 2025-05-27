from app.schemas.eye_age_prediction import EyeAgePredictionRequest, EyeAgePredictionResponse
from app.models.eye_age_model import predict_eye_age_model

def predict_next_eye_age(request: EyeAgePredictionRequest) -> EyeAgePredictionResponse:
    history = request.history[-6:]
    predicted = predict_eye_age_model(history)

    return EyeAgePredictionResponse(
        next_eye_age=predicted
    )
