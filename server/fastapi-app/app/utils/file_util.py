import os
import re
from datetime import datetime

def get_txt_filename(image_filename: str) -> str:
    return os.path.splitext(image_filename)[0] + ".txt"

def build_data_yaml(classes: list[str]) -> str:
    class_list = "\n".join([f"  - '{c}'" for c in classes])
    return f"""
        train: images
        val: images
        nc: {len(classes)}
        names:
        {class_list}
    """.strip()

def extract_timestamp_from_filename(filename: str) -> datetime:
    """
    파일명에서 타임스탬프 추출
    예: 42-20250516T142211.png → datetime(2025, 5, 16, 14, 22, 11)
    """
    match = re.search(r"(\d{8}T\d{6})", filename)
    if not match:
        raise ValueError("Invalid filename format, timestamp not found.")
    return datetime.strptime(match.group(1), "%Y%m%dT%H%M%S")