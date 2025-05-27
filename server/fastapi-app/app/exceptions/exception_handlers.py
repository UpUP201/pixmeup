from fastapi import Request
from fastapi.responses import JSONResponse
from app.schemas.error_response import ErrorResponse
from app.exceptions.custom_exception import CustomAppException
from datetime import datetime
from starlette.status import HTTP_404_NOT_FOUND, HTTP_500_INTERNAL_SERVER_ERROR
from fastapi.encoders import jsonable_encoder
import traceback

async def handle_custom_exception(request: Request, exc: CustomAppException):
    code = exc.error_code
    return JSONResponse(
        status_code=code.status,
        content=jsonable_encoder(ErrorResponse(
            timestamp=datetime.utcnow(),
            name=code.name,
            status=code.status,
            message=code.message
        ))
    )

async def handle_global_exception(request: Request, exc: Exception):

    print("🔥 [FastAPI GLOBAL ERROR] 예외 발생:")
    print(traceback.format_exc())

    return JSONResponse(
        status_code=HTTP_500_INTERNAL_SERVER_ERROR,
        content=jsonable_encoder(ErrorResponse(
            timestamp=datetime.utcnow(),
            name=exc.__class__.__name__,
            status=500,
            message=str(exc)
        ))
    )

async def handle_value_error(request: Request, exc: ValueError):
    return JSONResponse(
        status_code=HTTP_404_NOT_FOUND,
        content=jsonable_encoder(ErrorResponse(
            timestamp=datetime.utcnow(),
            name="NOT_FOUND",
            status=404,
            message=str(exc)
        ))
    )
