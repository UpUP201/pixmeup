import json
import re
from app.utils.sight_prompt_builder import build_prompt
from app.core.gemini_client import get_gemini_response
from app.services.rag_service import get_retrieved_knowledge  # ✅ RAG 추가

async def generate_sight_comment(survey, sight_test_list):
    # 1️⃣ 기존 prompt 생성
    prompt = build_prompt(survey, sight_test_list)

    # 2️⃣ RAG 문서 검색 추가
    rag_context = await get_retrieved_knowledge(
        user_id=survey.user_id,
        test_type="sight",
        query_text=prompt
    )

    # 3️⃣ prompt + knowledge 조합
    enhanced_prompt = f"""{prompt}

[참고 지식]
{rag_context}

위 검사 결과와 추가 지식을 참고하여 시력 검사 결과에 대한 종합적인 코멘트와 상태를 json 형식으로 작성해 주세요.
"""

    # 4️⃣ LLM 호출
    response_text = await get_gemini_response(enhanced_prompt)

    # 5️⃣ LLM 결과 처리
    clean_text = re.sub(r"```json|```", "", response_text).strip()

    try:
        parsed = json.loads(clean_text)
        return {
            "comment": parsed.get("comment", "").strip().replace("\n", ""),
            "sight_status": parsed.get("sight_status", "unknown")
        }
    except json.JSONDecodeError:
        return {
            "comment": clean_text.strip().replace("\n", ""),
            "sight_status": "unknown"
        }
