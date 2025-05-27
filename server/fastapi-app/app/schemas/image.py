from pydantic import BaseModel

class ImagePredictionResponse(BaseModel):
    id: str
    user_id: int
    summary: str
    description: str
    s3_key: str
    created_at: str

class ImagePredictionRequest(BaseModel):
    user_id: int
    file_url: str
    s3_key: str
    
