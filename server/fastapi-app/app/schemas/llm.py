from pydantic import BaseModel

class GeminiRequest(BaseModel):
    prompt: str

class GeminiResponse(BaseModel):
    result: str