from motor.motor_asyncio import AsyncIOMotorClient
from app.core import config
from datetime import datetime
from pymongo import DESCENDING
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode
import logging

client = AsyncIOMotorClient(config.MONGO_URI)
db = client[config.MONGO_DB]
areds_col = db["amd_predictions"]
image_col = db["image_predictions"]

logger = logging.getLogger(__name__)

async def get_prediction_records(user_id: int, page: int, size: int):
    try:
        skip = page * size

        pipeline = [
            {
                "$match": {"user_id": user_id}
            },
            {
                "$project": {
                    "created_at": 1,
                    "type": {"$literal": "AREDS"}
                }
            },
            {
                "$unionWith": {
                    "coll": "image_predictions",
                    "pipeline": [
                        {
                            "$match": {"user_id": user_id}
                        },
                        {
                            "$project": {
                                "created_at": 1,
                                "type": {"$literal": "IMAGE"}
                            }
                        }
                    ]
                }
            },
            {"$sort": {"created_at": -1}},
            {"$skip": skip},
            {"$limit": size}
        ]

        records = await areds_col.aggregate(pipeline).to_list(length=size)

        response = []
        for doc in records:
            response.append({
                "id": str(doc["_id"]),
                "created_at": doc["created_at"].isoformat() if isinstance(doc["created_at"], datetime) else str(doc["created_at"]),
                "type": doc["type"]
            })
        return response

    except Exception:
        logger.exception("Error in get_prediction_records")
        raise CustomAppException(ErrorCode.PREDICTION_RECORD_FETCH_FAILED)