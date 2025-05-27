import numpy as np

def predict_sight_model(values: list[int]) -> int:
    # int 값 (예: 8, 9) → float 변환
    float_values = [v / 10 for v in values]
    x = np.arange(len(float_values))
    coef = np.polyfit(x, float_values, deg=1)
    pred = np.polyval(coef, len(float_values))
    pred = np.clip(pred, 0.1, 2.0)
    return int(round(pred * 10))  # float → int 변환 (ex. 0.7 → 7)
