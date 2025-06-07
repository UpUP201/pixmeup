import os
from dotenv import load_dotenv
from langchain_openai import OpenAIEmbeddings
from langchain_community.vectorstores import FAISS

from app.core.mock_embedding import FakeEmbeddings

load_dotenv()
os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"  # ✅ OpenMP 충돌 무시

USE_MOCK = os.getenv("USE_MOCK", "false").lower() == "true"

_retriever = None  # 전역 retriever 캐시용

def create_retriever():
    global _retriever
    if _retriever is None:
        print("📦 FAISS 벡터 인덱스 로딩 중...")

        if(USE_MOCK):
            embeddings = FakeEmbeddings()
        else:
            openai_api_key = os.getenv("OPENAI_API_KEY", "sk-...")
            embeddings = OpenAIEmbeddings(openai_api_key=openai_api_key)

        db = FAISS.load_local(
            "/app/vectorstore/medical_knowledge",
            embeddings,
            allow_dangerous_deserialization=True
        )
        _retriever = db.as_retriever(search_kwargs={"k": 5})
        print("✅ FAISS 벡터 인덱스 로딩 완료")
    return _retriever
