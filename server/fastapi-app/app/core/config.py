import os
from dotenv import load_dotenv
from motor.motor_asyncio import AsyncIOMotorClient 

load_dotenv()

MONGO_URI = os.getenv("MONGO_URI")
MONGO_DB = os.getenv("MONGO_DB")

client = AsyncIOMotorClient(MONGO_URI)
db = client[MONGO_DB]

YOLO_MODEL_DIR = "/home/ubuntu/app/models/weights"

SPRING_API_URL = os.getenv("SPRING_API_URL")
