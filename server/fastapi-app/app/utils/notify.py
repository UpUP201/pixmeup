import requests

def send_slack_message(message: str, webhook_url: str):
    payload = {"text": message}
    headers = {"Content-Type": "application/json"}
    requests.post(webhook_url, json=payload, headers=headers)

# import os
# from utils.notify import send_slack_message

# send_slack_message("📢 모델 학습이 완료되었습니다.", os.getenv("SLACK_WEBHOOK_URL"))
