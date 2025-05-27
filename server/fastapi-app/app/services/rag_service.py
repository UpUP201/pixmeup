import time
from app.core.global_retriever import create_retriever

# 🔧 context 검색 함수
async def get_retrieved_knowledge(user_id: int, test_type: str, query_text: str) -> str:
    start = time.time()
    print(f"🔍 [RAG] 벡터 검색 시작: {test_type}, user_id={user_id}")

    retriever = create_retriever()  # ✅ 런타임 시점에서 안전하게 로딩
    docs = await retriever.ainvoke(query_text)

    print(f"✅ [RAG] 검색 완료: {len(docs)}개 문서 ({time.time() - start:.2f}s 소요)")

    context = ""
    for i, doc in enumerate(docs, 1):
        context += f"#{i}: {doc.page_content.strip()}\n"

    return context.strip()

