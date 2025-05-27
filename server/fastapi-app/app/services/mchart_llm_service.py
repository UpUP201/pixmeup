import json
import re
from app.utils.mchart_prompt_builder import build_mchart_prompt
from app.core.gemini_client import get_gemini_response
from app.services.rag_service import get_retrieved_knowledge  # ✅ RAG 추가

async def generate_mchart_comment(survey, mchart_test_list):
    # 1️⃣ 기존 prompt 생성
    prompt = build_mchart_prompt(survey, mchart_test_list)

    # 2️⃣ RAG 문서 검색 추가
    rag_context = await get_retrieved_knowledge(
        user_id=survey.user_id,
        test_type="mchart",
        query_text=prompt
    )

    # 3️⃣ prompt + knowledge 조합
    enhanced_prompt = f"""{prompt}

[참고 지식]
{rag_context}

위 검사 결과와 추가 지식을 참고하여 mChart 검사 결과에 대한 comment를 json 형식으로 작성해 주세요.
"""

    # 4️⃣ LLM 호출
    response_text = await get_gemini_response(enhanced_prompt)

    # 5️⃣ 코드블록에서 json 추출
    match = re.search(r"```json(.*?)```", response_text, re.DOTALL)
    if match:
        json_str = match.group(1).strip()
    else:
        json_str = response_text.strip()  # fallback

    try:
        parsed = json.loads(json_str)
        return {"comment": parsed.get("comment", "")}
    except Exception as e:
        # fallback → json 파싱 실패 시 원문 일부 반환
        return {"comment": json_str[:100]}
