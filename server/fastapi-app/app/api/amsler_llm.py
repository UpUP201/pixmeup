from fastapi import APIRouter
from app.schemas.amsler_llm_schema import AmslerRequest
from app.services.amsler_llm_service import generate_amsler_comment

router = APIRouter()

@router.post("/llm/amsler")
async def amsler_comment(req: AmslerRequest):
    result = await generate_amsler_comment(req.survey, req.amsler_test_list)
    return {
        "userId": req.survey.user_id,
        "comment": result["comment"]
    }