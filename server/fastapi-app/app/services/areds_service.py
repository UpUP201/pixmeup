from motor.motor_asyncio import AsyncIOMotorClient  # MongoDB에 비동기로 연결하는 드라이버
from bson import ObjectId   # MongoDB의 기본 ID 타입 사용
from app.core import config
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode
from datetime import datetime

client = AsyncIOMotorClient(config.MONGO_URI)    # MongoDB에 비동기 클라이언트로 연결
db = client[config.MONGO_DB]
collection = db["amd_predictions"]    # MongoDB 내 사용한 collection 선택

# dict 형태의 data를 collection에 insert하고, 자동 생성된 _id값을 문자열로 변환해서 반환
async def save_areds_result(data: dict) -> str:
    try:
        result = await collection.insert_one(data)
        return str(result.inserted_id)
    except Exception:
        raise CustomAppException(ErrorCode.AREDS_RESULT_SAVE_FAILED)

async def get_areds_result(result_id: str) -> dict | None:
    doc = await collection.find_one({"_id": ObjectId(result_id)})
    if doc:
        doc["id"] = str(doc["_id"])
        doc.pop("_id")
        if isinstance(doc["created_at"], datetime):
            doc["created_at"] = doc["created_at"].isoformat()
        doc["risk_percent"] = int(doc["risk_percent"])
        return doc
    return None

async def get_latest_areds_by_user(user_id: int) -> dict | None:
    docs = await collection.find({"user_id": user_id}).sort("created_at", -1).limit(1).to_list(length=1)
    if docs:
        doc = docs[0]
        doc["id"] = str(doc["_id"])
        doc.pop("_id")
        if isinstance(doc["created_at"], datetime):
            doc["created_at"] = doc["created_at"].isoformat()
        doc["risk_percent"] = int(doc["risk_percent"])
        return doc
    return None

def make_areds_summary(risk_percent: int) -> str:
    if risk_percent >= 70:
        return "귀하는 향후 AMD 발생 위험이 높은 편입니다. 조기 발견 및 치료를 위해 정밀 검진이 필요합니다."
    elif risk_percent >= 40:
        return "귀하는 향후 AMD 발생 위험이 중간 수준입니다. 눈 건강에 유의하며, 필요 시 전문의 상담을 받아보세요."
    else:
        return "귀하는 향후 AMD 발생 위험이 낮은 편입니다. 정기적인 검진을 통해 눈 상태를 확인하세요."

def map_to_model_input(data: dict) -> dict:
    return {
        "age": data["age"],
        "smoking": 1 if data.get("past_smoking") or data.get("current_smoking") else 0,
        "drusen_area": 1 if data.get("mchart_abnormal_flag") else 0,
        "pigment_abnormality": 1 if data.get("amsler_abnormal_flag") else 0
    }
