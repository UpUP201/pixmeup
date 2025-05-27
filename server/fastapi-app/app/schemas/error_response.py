from pydantic import BaseModel
from datetime import datetime

class ErrorResponse(BaseModel):
    timestamp: datetime
    name: str
    status: int
    message: str