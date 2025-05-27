import os
import zipfile
from datetime import datetime
from app.utils.file_util import build_data_yaml, extract_timestamp_from_filename
import requests
from app.constants.disease_labels import YOLO_CLASS_MAP
from app.core.config import SPRING_API_URL

# 기본 경로 설정
BASE_DIR = "user_data"
IMAGES_DIR = os.path.join(BASE_DIR, "images")
LABELS_DIR = os.path.join(BASE_DIR, "labels")
OUTPUT_DIR = "temp_zip"
LAST_TIMESTAMP_PATH = os.path.join(OUTPUT_DIR, "last_zip_time.txt")
SPRING_UPLOAD_URL = f"{SPRING_API_URL}/api/v2/s3/upload-direct"

os.makedirs(OUTPUT_DIR, exist_ok=True)

def load_last_zip_time():
    if not os.path.exists(LAST_TIMESTAMP_PATH):
        return None
    with open(LAST_TIMESTAMP_PATH, "r") as f:
        return datetime.strptime(f.read().strip(), "%Y%m%d%H%M%S")
    
def save_current_zip_time(ts: datetime):
    with open(LAST_TIMESTAMP_PATH, "w") as f:
        f.write(ts.strftime("%Y%m%d%H%M%S"))

def should_include(filename: str, last_time: datetime):
    ts = extract_timestamp_from_filename(filename)
    return ts and (last_time is None or ts > last_time)

def create_zip_dataset():
    last_zip_time = load_last_zip_time()
    now = datetime.now()
    zip_filename = f"dataset_{now.strftime('%Y%m%d_%H%M%S')}.zip"
    zip_path = os.path.join(OUTPUT_DIR, zip_filename)

    with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for filename in os.listdir(IMAGES_DIR):
            if should_include(filename, last_zip_time):
                zipf.write(os.path.join(IMAGES_DIR, filename), arcname=f"images/{filename}")
        for filename in os.listdir(LABELS_DIR):
            if should_include(filename, last_zip_time):
                zipf.write(os.path.join(LABELS_DIR, filename), arcname=f"labels/{filename}")
        yaml = build_data_yaml([YOLO_CLASS_MAP[i] for i in sorted(YOLO_CLASS_MAP.keys())])
        zipf.writestr("data.yaml", yaml)

    save_current_zip_time(now)
    print(f"✅ zip 생성 완료: {zip_path}")
    return zip_path

def upload_zip_via_spring(user_id: int, zip_path: str):
    with open(zip_path, "rb") as f:
        res = requests.post(
            SPRING_UPLOAD_URL,
            params={
                "userId": user_id,
                "fileName": os.path.basename(zip_path),
                "contentType": "application/zip"
            },
            data=f.read(),
            headers={"Content-Type": "application/zip"}
        )
        res.raise_for_status()
        print("✅ Spring 업로드 요청 완료")

if __name__ == "__main__":
    zip_path = create_zip_dataset()
    upload_zip_via_spring(user_id=0, zip_path=zip_path)
