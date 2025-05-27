from app.schemas.sight_prediction import SightPredictionRequest, SightPredictionResponse
from app.models.sight_model import predict_sight_model

def predict_next_sight(request: SightPredictionRequest) -> SightPredictionResponse:
    history = request.history[-6:]
    left_series = [r.left_sight for r in history]
    right_series = [r.right_sight for r in history]

    next_left = predict_sight_model(left_series)
    next_right = predict_sight_model(right_series)

    return SightPredictionResponse(
        next_left_sight=next_left,
        next_right_sight=next_right
    )