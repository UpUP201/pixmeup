from fastapi import APIRouter
from app.schemas.mchart_llm_schema import MChartRequest
from app.services.mchart_llm_service import generate_mchart_comment

router = APIRouter()

@router.post("/llm/mchart")
async def mchart_comment(req: MChartRequest):
    result = await generate_mchart_comment(req.survey, req.mchart_test_list)
    return {
        "userId": req.survey.user_id,
        "comment": result["comment"]
    }
