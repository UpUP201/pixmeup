from fastapi import APIRouter
from app.schemas.presbyopia_llm_schema import PresbyopiaRequest
from app.services.presbyopia_llm_service import generate_presbyopia_comment

router = APIRouter()

@router.post("/llm/presbyopia")
async def presbyopia_comment(req: PresbyopiaRequest):
    result = await generate_presbyopia_comment(req.survey, req.presbyopia_test_list)
    return {
        "userId": req.survey.user_id,
        "comment": result["comment"],
        "presbyopiaStatus": result["presbyopia_status"]
    }
