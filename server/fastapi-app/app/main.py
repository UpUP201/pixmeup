from contextlib import asynccontextmanager
from fastapi import FastAPI
from app.api import (
    tmp, areds, image, llm, total, sight_llm, presbyopia_llm, 
    amsler_llm, mchart_llm, sight_prediction, eye_age_prediction, model_metrics
)
from app.exceptions.exception_handlers import (
    handle_custom_exception,
    handle_global_exception,
    handle_value_error
)
from app.exceptions.custom_exception import CustomAppException
from prometheus_fastapi_instrumentator import Instrumentator
from app.core.global_retriever import create_retriever 
from app.startup.faiss_index_checker import ensure_faiss_index_files_exist
import logging

@asynccontextmanager
async def lifespan(app: FastAPI):
    ensure_faiss_index_files_exist()  # 먼저 FAISS 파일이 존재하는지 확인 (없으면 다운로드)
    app.state.retriever = create_retriever()  # 그 다음에 로딩
    yield

# @SpringBootApplication 역할
app = FastAPI(lifespan=lifespan)

# prometheus metric 수집용
Instrumentator().instrument(app).expose(app)

# 전역 로깅 설정
logging.basicConfig(level=logging.INFO)

# 라우터 등록 -> 루프화
ROUTERS = [
    tmp, areds, image, llm, total, 
    sight_llm, presbyopia_llm, amsler_llm, mchart_llm, 
    sight_prediction, eye_age_prediction, model_metrics
]

for module in ROUTERS:
    app.include_router(module.router, prefix="/ml")

# 예외 핸들러 등록
app.add_exception_handler(CustomAppException, handle_custom_exception)
app.add_exception_handler(ValueError, handle_value_error)
app.add_exception_handler(Exception, handle_global_exception)

# 실행 명령어 단축용 -> python app/main.py
def main():
    import uvicorn
    uvicorn.run("app.main:app", host="127.0.0.1", port=8000, reload=True)

if __name__ == "__main__":
    main()