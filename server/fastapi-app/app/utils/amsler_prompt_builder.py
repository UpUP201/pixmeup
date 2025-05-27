def build_amsler_prompt(survey, amsler_test_list):
    import re
    sorted_list = sorted(amsler_test_list, key=lambda x: x.created_at)
    latest = sorted_list[-1]

    age_group = {
        9: "9세 이하", 1: "10대", 2: "20대", 4: "30대",
        5: "40대", 6: "50대", 7: "60대", 8: "70세 이상"
    }.get(survey.age, f"코드 {survey.age}")

    gender_text = "남성" if survey.gender == "M" else "여성"

    surgery_desc = {
        "normal": "수술 이력 없음",
        "correction": "라식/라섹 수술 이력",
        "cataract": "백내장 수술 이력",
        "etc": "기타 수술 이력"
    }.get(survey.surgery, survey.surgery)

    abnormal_map = {
        "n": "정상",
        "d": "왜곡된 시야(황반변성 의심)",
        "b": "검게 보이는 영역(중심 맹점 가능)",
        "w": "흐리거나 하얗게 보임(시신경 이상 또는 백내장 의심)"
    }

    def interpret(loc_string):
        codes = re.split(r"[,\s]+", loc_string.strip().lower())
        return [abnormal_map[c] for c in codes if c in abnormal_map and c != "n"]

    left_issues = interpret(latest.left_macular_loc)
    right_issues = interpret(latest.right_macular_loc)

    def summary_text(eye: str, issues: list[str]):
        if not issues:
            return f"{eye} 눈은 특이 소견이 없습니다."
        else:
            joined = ", ".join(issues)
            return f"{eye} 눈에서 {joined}이(가) 관찰됩니다."

    left_summary = summary_text("왼쪽", left_issues)
    right_summary = summary_text("오른쪽", right_issues)

    return f"""
당신은 황반 질환을 진단하는 안과 전문의입니다.
아래 검사 정보를 참고하여, 가장 최근 암슬러 차트 검사 결과를 사용자가 이해하기 쉬운 언어로 설명해 주세요.

<출력 규칙>
- JSON 없이 순수 comment 문장 한 줄만 출력하세요.
- 최대 2문장, 문장당 약 50자 이내로 부드럽고 명확하게 작성합니다.
- 의학 용어는 가능한 한 피하고, 감각적이고 쉬운 표현으로 대체하세요.
  예: "중심 시야가 가려질 수 있습니다", "일그러짐이 보일 수 있습니다"

<주의 사항>
- 황반변성, 중심 맹점 등의 병명은 사용하지 마세요.
- 사용자의 당뇨, 흡연, 수술 이력은 판단에 간접 참고만 하고, comment에는 직접 드러내지 마세요.
- 현재 상태와 필요한 조치를 간단히 안내해주세요.
- 증상이 없을 경우에도 정기 검사 권유 문구를 포함해주세요.

📌 예시:
- "왼쪽 눈 중심에서 일그러짐이 보일 수 있습니다. 정밀 검사를 통해 상태 확인이 필요합니다."
- "양쪽 눈 모두 특이 소견이 없습니다. 주기적인 검사를 권장드립니다."

[문진 정보 요약]
- 나이대: {age_group}
- 성별: {gender_text}
- 안경 착용: {"착용" if survey.glasses else "비착용"}
- 수술 이력: {surgery_desc}
- 당뇨 여부: {"있음" if survey.diabetes else "없음"}
- 흡연 여부: {"있음" if survey.smoking else "없음"}

[최근 암슬러 차트 검사 요약]
- 검사일자: {latest.created_at.strftime('%Y-%m-%d')}
- {left_summary}
- {right_summary}

⛔ 반드시 위 정보를 참고해 부드러운 comment 문장만 출력하세요.
"""
