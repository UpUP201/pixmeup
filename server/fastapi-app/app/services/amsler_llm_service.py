import re
import time
from app.utils.amsler_prompt_builder import build_amsler_prompt
from app.core.gemini_client import get_gemini_response
from app.services.rag_service import get_retrieved_knowledge  # ✅ RAG 추가

async def generate_amsler_comment(survey, amsler_test_list):
    print(f"📥 [LLM] 암슬러 분석 시작: user_id={survey.user_id}")
    total_start = time.time()

    # 1️⃣ 기존 prompt 생성
    prompt = build_amsler_prompt(survey, amsler_test_list)
    print("🧱 [Prompt] 생성 완료")

    # 2️⃣ RAG 문서 검색 추가
    rag_start = time.time()
    rag_context = await get_retrieved_knowledge(
        user_id=survey.user_id,
        test_type="amsler",
        query_text=prompt
    )
    print(f"📚 [RAG] 문서 검색 완료 ({time.time() - rag_start:.2f}s)")

    # 3️⃣ prompt + knowledge 조합
    enhanced_prompt = f"""{prompt}

[참고 지식]
{rag_context}

위 검사 결과와 추가 지식을 참고하여 암슬러 검사 결과에 대한 코멘트를 작성해 주세요.
"""

    # 4️⃣ LLM 호출
    llm_start = time.time()
    response_text = await get_gemini_response(enhanced_prompt)
    print(f"🤖 [LLM] 응답 수신 완료 ({time.time() - llm_start:.2f}s)")

    # 5️⃣ 코드 블록 제거 + 개행 제거
    clean_text = re.sub(r"```.*?```", "", response_text, flags=re.DOTALL).strip()
    clean_text = clean_text.replace("\n", " ").strip()

    print(f"✅ [완료] 전체 소요 시간: {time.time() - total_start:.2f}s")
    return {"comment": clean_text}
