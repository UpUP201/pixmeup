import os
import requests
import html
import time
from urllib.request import urlretrieve

INDEX_DIR = "/app/vectorstore/medical_knowledge"
FAISS_PATH = os.path.join(INDEX_DIR, "index.faiss")
PKL_PATH = os.path.join(INDEX_DIR, "index.pkl")

SPRING_API_URL = os.getenv("SPRING_API_URL", "http://springboot:8080")

def ensure_faiss_index_files_exist():
    if os.path.exists(FAISS_PATH) and os.path.exists(PKL_PATH):
        print("✅ FAISS 인덱스 파일이 이미 존재합니다.")
        return

    print("📦 FAISS 인덱스 파일이 없어 SpringBoot에서 다운로드 URL 요청 중...")

    try:
        res = requests.post(f"{SPRING_API_URL}/api/v2/s3/download-faiss")
        res.raise_for_status()
        res_data = res.json()

        print("📥 응답 데이터:", res_data)

        data = res_data["data"]  # ✅ 중첩된 data 키에 접근
        faiss_url = html.unescape(data["faiss_url"])
        pkl_url = html.unescape(data["pkl_url"])

        os.makedirs(INDEX_DIR, exist_ok=True)

        urlretrieve(faiss_url, FAISS_PATH)
        urlretrieve(pkl_url, PKL_PATH)

        # ✅ 파일 존재 여부 다시 확인
        if not os.path.exists(FAISS_PATH):
            raise FileNotFoundError("❌ index.faiss 파일이 존재하지 않습니다.")
        if not os.path.exists(PKL_PATH):
            raise FileNotFoundError("❌ index.pkl 파일이 존재하지 않습니다.")

        print("✅ FAISS 인덱스 파일 다운로드 완료")

    except Exception as e:
        print("❌ FAISS 인덱스 파일 다운로드 실패:", e)
        raise
