import requests
from app.core.config import SPRING_API_URL


def upload_file_by_proxy(user_id: int, filename: str, local_path: str, content_type: str):
    with open(local_path, "rb") as f:
        file_bytes = f.read()

    params = {
        "userId": user_id,
        "fileName": filename,
        "contentType": content_type
    }

    res = requests.post(
        f"{SPRING_API_URL}/api/v2/s3/upload-url",
        params=params,
        timeout=10
    )

    res.raise_for_status()
    upload_url = res.json()["data"]["presignedUrl"].replace("&amp;", "&")
    upload_res = requests.put(
        upload_url,
        data=file_bytes,
        headers={"Content-Type": content_type},
        timeout=10
    )
    f"upload complete: {upload_res}"

    return res.json()["data"]["s3Key"]
