from fastapi import APIRouter
from app.schemas.llm import GeminiRequest, GeminiResponse
from app.core.gemini_client import get_gemini_response

router = APIRouter()

@router.post("/predict/gemini", response_model=GeminiResponse)
def predict_with_gemini(request: GeminiRequest):
    result = get_gemini_response(request.prompt)
    return GeminiResponse(result=result)