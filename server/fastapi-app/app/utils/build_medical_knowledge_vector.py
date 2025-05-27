import os
from dotenv import load_dotenv
from langchain_community.document_loaders import PyMuPDFLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_openai import OpenAIEmbeddings  # ✅ 최신 방식
from langchain_community.vectorstores import FAISS  # ✅ 최신 방식

load_dotenv()

VECTOR_DIR = "/app/vectorstore/medical_knowledge"
PAPER_DIR = "app/data/papers"

def build_medical_knowledge_index():
    all_docs = []

    for filename in os.listdir(PAPER_DIR):
        if not filename.endswith(".pdf"):
            continue

        path = os.path.join(PAPER_DIR, filename)
        print(f"📄 처리 중: {filename}")

        try:
            loader = PyMuPDFLoader(path)
            raw_docs = loader.load()
        except Exception as e:
            print(f"❌ {filename} 로딩 실패: {e}")
            continue

        # 텍스트 조각 분리
        splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=50)
        chunks = splitter.split_documents(raw_docs)

        # 메타데이터에 파일명 추가
        for doc in chunks:
            doc.metadata["source"] = filename

        all_docs.extend(chunks)

    # 벡터화 및 저장
    if not all_docs:
        print("❗ 논문이 하나도 로딩되지 않았습니다.")
        return

    print(f"🧠 총 {len(all_docs)}개 조각 생성 완료. 벡터화 시작...")

    openai_api_key = os.getenv("OPENAI_API_KEY", "sk-tmp")
    # if not openai_api_key:
    #     raise EnvironmentError("❌ OPENAI_API_KEY가 설정되어 있지 않습니다.")

    embeddings = OpenAIEmbeddings(openai_api_key=openai_api_key)
    vectorstore = FAISS.from_documents(all_docs, embeddings)
    vectorstore.save_local(VECTOR_DIR)

    print(f"✅ 벡터 저장 완료: {VECTOR_DIR}")

if __name__ == "__main__":
    build_medical_knowledge_index()
