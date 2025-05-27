def build_mchart_prompt(survey, mchart_test_list):
    sorted_list = sorted(mchart_test_list, key=lambda x: x.created_at)
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
    }.get(survey.surgery, "알 수 없음")

    summaries = []
    for i, test in enumerate(sorted_list):
        summaries.append(
            f"{i+1}회차 검사일: {test.created_at.strftime('%Y-%m-%d')}, "
            f"좌안 수직:{test.left_eye_ver}, 수평:{test.left_eye_hor} / "
            f"우안 수직:{test.right_eye_ver}, 수평:{test.right_eye_hor}"
        )
    test_summary_text = " / ".join(summaries)

    return f"""
당신은 망막 질환을 진단하는 안과 전문의입니다.
다음 사용자 검사 데이터를 바탕으로, 시야 왜곡 여부와 그 정도를 사용자 친화적인 언어로 설명하고, 정밀 검진 필요성을 안내하는 comment를 작성하세요.

📌 출력 형식
- 반드시 다음 JSON만 출력합니다. 코드블럭 없이 순수 JSON만 반환하세요.
- comment는 1~2문장, 문장당 50자 이내로 작성하세요.

📌 comment 구성 규칙
- 최근 검사 기준으로 시야 왜곡이 **어느 눈에서, 어느 방향(수직/수평)** 으로 어느 정도 나타나는지를 설명하세요.
- 수치(예: 8, 10 등)는 직접 언급하지 말고, 의미 해석을 기반으로 표현하세요.
  (예: 0.8mm 이상은 중등도 이상으로 간주 → "왜곡이 뚜렷하게 관찰됩니다")
- 예시는 다음과 같은 기준으로 응답해야 합니다:

📌 왜곡 수준 판단 기준 (수치 해석 기반)
- 0~3: 왜곡 없음 또는 매우 경미함 → “시야는 안정적입니다”
- 4~7: 경미한 왜곡 가능성 → “선이 약간 일그러져 보일 수 있습니다”
- 8 이상: 중등도 이상 왜곡 가능성 → “중심이 뚜렷하게 휘어 보이는 증상이 있습니다”

📌 금지 사항
- 수치 자체를 언급하지 마세요 (예: “좌안 수직 8” ❌)
- 병명(황반변성 등)은 직접 언급하지 마세요
- 당뇨/흡연/수술 등의 문진 정보는 comment에 포함하지 마세요

📌 예시
{{
  "comment": "왼쪽 눈 수직 방향에서 중심이 휘어 보이는 증상이 있습니다. 시세포 이상 가능성을 고려해 정밀 검사를 권장드립니다."
}}

또는

{{
  "comment": "현재 양쪽 눈 모두 시야는 안정적으로 유지되고 있습니다. 정기 검진을 통해 상태를 확인해 주세요."
}}

[문진 정보]
- 나이대: {age_group}
- 성별: {gender_text}
- 안경 착용: {"착용" if survey.glasses else "비착용"}
- 수술 이력: {surgery_desc}
- 당뇨 여부: {"있음" if survey.diabetes else "없음"}
- 흡연 여부: {"있음" if survey.smoking else "없음"}

[엠차트 검사 이력 요약]
총 {len(sorted_list)}회 검사 / {test_summary_text}

[최근 검사]
- 날짜: {latest.created_at.strftime('%Y-%m-%d')}
- 좌안: 수직 {latest.left_eye_ver}, 수평 {latest.left_eye_hor}
- 우안: 수직 {latest.right_eye_ver}, 수평 {latest.right_eye_hor}
"""
