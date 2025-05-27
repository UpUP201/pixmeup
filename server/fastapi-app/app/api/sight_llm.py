from fastapi import APIRouter
from app.schemas.sight_llm_schema import SightRequest
from app.services.sight_llm_service import generate_sight_comment

router = APIRouter()

@router.post("/llm/sight")
async def sight_comment(req: SightRequest):
    result = await generate_sight_comment(req.survey, req.sight_test_list)
    return {
        "userId": req.survey.user_id,
        "comment": result["comment"],
        "sightStatus": result["sight_status"]
    }
