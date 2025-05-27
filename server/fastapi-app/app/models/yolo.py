from ultralytics import YOLO
import os
from app.core.config import YOLO_MODEL_DIR

_model = None  # 전역 YOLO 인스턴스

def get_latest_model_path():
    files = [f for f in os.listdir(YOLO_MODEL_DIR) if f.endswith(".pt")]
    files.sort(reverse=True)
    return os.path.join(YOLO_MODEL_DIR, files[0]) if files else None

def get_model():
    global _model
    if _model is None:
        model_path = get_latest_model_path()
        if not model_path:
            raise ValueError("모델 파일이 없습니다.")
        _model = YOLO(model_path)
    return _model

def reload_model():
    global _model
    model_path = get_latest_model_path()
    if not model_path:
        raise ValueError("모델 파일이 없습니다.")
    _model = YOLO(model_path)

def predict_image(image_path: str):
    model = get_model()
    results = model(image_path)
    return results
