import numpy as np
from app.exceptions.custom_exception import CustomAppException
from app.enums.error_code import ErrorCode

def predict_amd_risk(data: dict) -> dict:
    # 논문 기반 회귀 계수 (PMC4082761 가번)
    try:
        beta = {
            "intercept": -3.5,
            "age": 0.04,
            "smoking": 0.7,
            "drusen_area": 1.13,
            "pigment_abnormality": 1.49
        }

        y = beta["intercept"]
        y += beta["age"] * float(data.get("age", 0))
        y += beta["smoking"] * int(bool(data.get("smoking", 0)))
        y += beta["drusen_area"] * int(bool(data.get("drusen_area", 0)))
        y += beta["pigment_abnormality"] * int(bool(data.get("pigment_abnormality", 0)))

        prob = 1 / (1 + np.exp(-y))
        risk = "High" if prob >= 0.7 else "Medium" if prob >= 0.4 else "Low"
        risk_percent = int(round(prob * 100))

        return {
            "risk_percent": risk_percent,
            "risk": risk
        }

    except Exception:
        raise CustomAppException(ErrorCode.INTERNAL_SERVER_ERROR)
