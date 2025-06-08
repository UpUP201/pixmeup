from motor.motor_asyncio import AsyncIOMotorClient
from datetime import datetime
from bson import ObjectId
from app.models.yolo import predict_image
from app.schemas.image import ImagePredictionResponse
from app.core.gemini_client import get_gemini_response
from app.constants.disease_labels import DISEASE_LABEL_KO, YOLO_CLASS_MAP
from app.core import config
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode
from app.utils.s3_util import upload_file_by_proxy
from PIL import Image
import urllib.request
import tempfile
import os
import re
import logging

client = AsyncIOMotorClient(config.MONGO_URI)
db = client[config.MONGO_DB]
collection = db["image_predictions"]
logger = logging.getLogger(__name__)

async def get_image_result(result_id: str) -> dict | None:
    doc = await collection.find_one({"_id": ObjectId(result_id)})

    # ID 변환
    doc["id"] = str(doc["_id"])
    doc.pop("_id", None)

    # 필수 필드 보완
    doc["s3_key"] = doc.get("s3_key", "")
    doc["user_id"] = doc.get("user_id", None)
    doc["summary"] = doc.get("summary", "")
    doc["description"] = doc.get("description", "")

    created_at = doc.get("created_at")
    if isinstance(created_at, datetime):
        created_at = created_at.isoformat()
    elif created_at is None:
        created_at = datetime.utcnow().isoformat()
    else:
        created_at = str(created_at)

    doc["created_at"] = created_at

    return doc

async def get_latest_image_result(user_id: int) -> dict | None:
    doc = await collection.find({"user_id": user_id}).sort("created_at", -1).limit(1).to_list(length=1)
    if doc:
        doc[0]["id"] = str(doc[0]["_id"])
        doc[0].pop("_id")
        return doc[0]
    return None

async def save_image_result(data: dict) -> str:
    try:
        result = await collection.insert_one(data)
        return str(result.inserted_id)
    except Exception:
        raise CustomAppException(ErrorCode.IMAGE_RESULT_SAVE_FAILED)

async def predict_image_service(s3_key: str, file_url: str, user_id: int) -> ImagePredictionResponse:
    # presigned url로 이미지 다운로드
    ext = os.path.splitext(s3_key)[1] or ".jpg" # 확장자 없을 경우 대비
    with tempfile.NamedTemporaryFile(suffix=ext, delete=False) as tmp:
        try:
            urllib.request.urlretrieve(file_url, tmp.name)
        except Exception as e:
            logger.error(f"❌ 이미지 다운로드 실패: {file_url}, 에러: {e}")

        image_path = tmp.name

    # YOLO 예측
    raw = predict_image(image_path)
    predicted_boxes = parse_yolo_result(raw)

    # 예측 결과가 아예 없을 경우 -> 예외처리로 바꾸기
    if not predicted_boxes:
        summary = "질병이 감지되지 않았습니다."
        description = "이미지에서 이상 소견이 탐지되지 않았습니다. 정기적인 검진으로 눈 건강을 유지하세요."
        record = {
            "user_id": user_id,
            "class_id": None,
            "class_en": "none",
            "class_ko": "이상 없음",
            "summary": summary,
            "description": description,
            "s3_key": s3_key,
            "created_at": datetime.utcnow()
        }
        result_id = await save_image_result(record)
        return ImagePredictionResponse(
            id=result_id,
            user_id=user_id,
            summary=summary,
            description=description,
            s3_key=s3_key,
            created_at=record["created_at"].isoformat()
        )

    # ✅ .txt 저장: conf 무관하게 전체 박스 저장
    # .txt 파일 경로 설정
    txt_path = os.path.splitext(image_path)[0] + ".txt"
    with open(txt_path, 'w') as f:
        for box in predicted_boxes:
            class_id = int(box[5])
            cx, cy, w, h = convert_to_yolo_format(box[0:4], image_path)
            f.write(f"{class_id} {cx} {cy} {w} {h}\n")
    
    # .txt 파일명을 원본 s3_key 기반으로 생성
    label_filename = os.path.basename(s3_key).replace(ext, ".txt")

    # presigned URL 받아서 S3에 업로드
    _ = upload_file_by_proxy(
        user_id=user_id,
        filename=label_filename,
        local_path=txt_path,
        content_type="text/plain"
    )

    # ✅ 가장 conf 높은 박스만 대표 class로 사용
    predicted_boxes.sort(key=lambda b: b[4], reverse=True)
    best_box = predicted_boxes[0]
    class_id = int(best_box[5])
    class_name_en = YOLO_CLASS_MAP.get(class_id, "healthy eyes")
    class_name_ko = DISEASE_LABEL_KO.get(class_name_en, "정상")

    if class_name_en == "healthy eyes":
        summary = "감지된 질병이 없습니다."
        prompt = (
            "정상으로 판별되었습니다. 사용자에게 눈 건강을 잘 유지하고 있다고 안내해주세요."
            "200자 이내로 작성해주세요. "
            "**굵은 글씨(별표), 마크다운, 특수 문자, 이모지는 사용하지 마세요. "
            "문장은 부드럽게 마무리해 주세요."
        )
    else:
        summary = choose_subject_particle(class_name_ko)
        prompt = (
            f"검사 결과 '{class_name_ko}'가 감지되었습니다. "
            f"이 질환이 무엇인지 간단히 설명하고, 일반적인 관리법을 핵심만 짧게 알려주세요. "
            "200자 이내로 작성해주세요. "
            "**굵은 글씨(별표), 마크다운, 특수 문자, 이모지는 사용하지 마세요. "
            "문장은 예의 있고 신뢰감 있게 마무리해 주세요."
        )

    try:
        llm_result = await get_gemini_response(prompt)
        llm_result = "\n\n".join(llm_result.split("\n\n")[:3])
    except Exception:
        llm_result = f"{class_name_ko} 질환이 감지되었습니다. 정밀 검진이 필요합니다."

    record = {
        "user_id": user_id,
        "class_id": class_id,
        "class_en": class_name_en,
        "class_ko": class_name_ko,
        "summary": summary,
        "description": llm_result,
        "s3_key": s3_key,
        "created_at": datetime.utcnow()
    }
    result_id = await save_image_result(record)

    print("🎯 최종 리턴 직전 데이터:", {
        "id": result_id,
        "user_id": user_id,
        "summary": summary,
        "description": llm_result,
        "s3_key": s3_key,
        "created_at": record["created_at"]
    })

    return ImagePredictionResponse(
        id=result_id,
        user_id=user_id,
        summary=summary,
        description=llm_result,
        s3_key=s3_key,
        created_at=record["created_at"].isoformat()
    )

def choose_subject_particle(noun: str) -> str:
    """명사에 어울리는 주격 조사 '이/가'를 붙인다."""
    return f"{noun}{'이' if has_final_consonant(noun) else '가'} 감지되었습니다."

def has_final_consonant(word: str) -> bool:
    """받침이 있는지 판단 (한글만 대상)"""
    if not word or not re.match(r'[가-힣]+$', word[-1]):
        return False
    code = ord(word[-1]) - 0xAC00
    return code % 28 != 0

def parse_yolo_result(results):
    print("🧪 YOLO 결과 파싱 시작")

    if not results or not hasattr(results[0], 'boxes'):
        print("⚠️ Warning: 결과가 없거나 boxes 속성이 없음")
        return []

    boxes = results[0].boxes
    if boxes is None:
        print("⚠️ Warning: boxes 자체가 None")
        return []

    print(f"🔍 박스 수: {len(boxes)}")
    print(f"📦 boxes.xyxy: {boxes.xyxy}")
    print(f"📊 boxes.conf: {boxes.conf}")
    print(f"🏷️ boxes.cls: {boxes.cls}")

    parsed = []
    for i, (xyxy, conf, cls) in enumerate(zip(boxes.xyxy, boxes.conf, boxes.cls)):
        x1, y1, x2, y2 = map(float, xyxy)
        parsed.append([x1, y1, x2, y2, float(conf), int(cls)])
        print(f"✅ [{i}] Box: {parsed[-1]}")

    print("🎯 YOLO 결과 파싱 완료")
    return parsed

def convert_to_yolo_format(box, image_path=None):
    """
    box: [x1, y1, x2, y2]
    image_path: 원본 이미지 경로 (사이즈 계산용)
    return: cx, cy, w, h (0~1로 정규화된 YOLO 포맷)
    """
    x1, y1, x2, y2 = box
    bbox_width = x2 - x1
    bbox_height = y2 - y1
    center_x = x1 + bbox_width / 2
    center_y = y1 + bbox_height / 2

    with Image.open(image_path) as img:
        img_w, img_h = img.size

    return (
        round(center_x / img_w, 6),
        round(center_y / img_h, 6),
        round(bbox_width / img_w, 6),
        round(bbox_height / img_h, 6)
    )

