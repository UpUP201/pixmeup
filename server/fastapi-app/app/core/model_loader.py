from ultralytics import YOLO
import requests # HTTP 통신을 가장 쉽게 할 수 있는 라이브러리
from app.core.config import SPRING_API_URL, YOLO_MODEL_DIR
import os

# 모델 파일 저장 경로
MODEL_LOCAL_PATH = os.path.join(YOLO_MODEL_DIR, "best.pt")

def download_latest_model_from_s3():
    res = requests.post(f"{SPRING_API_URL}/api/v2/s3/download-model")
    # 응답이 200이 아니면 에러로 처리
    res.raise_for_status()
    print("✅ 모델 다운로드 요청 완료")

# YOLO 모델 로드 (없으면 S3에서 다운로드 시도)
try:
    model = YOLO(YOLO_MODEL_DIR)
except FileNotFoundError:
    print("🔄 best.pt가 없어 S3에서 다운로드 시도 중...")
    download_latest_model_from_s3()
    model = YOLO(YOLO_MODEL_DIR)