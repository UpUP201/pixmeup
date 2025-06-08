import numpy as np

def predict_eye_age_model(values: list[int]) -> int:
    x = np.arange(len(values))
    y = np.array(values)
    
    # 1차 선형 회귀
    coef = np.polyfit(x, y, deg=1)
    next_index = len(values)
    prediction = np.polyval(coef, next_index)
    return int(round(prediction))
