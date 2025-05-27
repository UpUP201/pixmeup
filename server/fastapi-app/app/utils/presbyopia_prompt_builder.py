def build_presbyopia_prompt(survey, presbyopia_test_list):
    sorted_list = sorted(presbyopia_test_list, key=lambda x: x.created_at)
    latest = sorted_list[-1]
    abnormal_count = sum(test.avg_distance > 25 for test in sorted_list)

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

    test_summaries = []
    for i, test in enumerate(sorted_list):
        test_summaries.append(
            f"{i+1}회차 검사일: {test.created_at.strftime('%Y-%m-%d')}, "
            f"평균 거리: {test.avg_distance:.1f}cm, "
            f"노안 의심: {'있음' if test.avg_distance > 25 else '없음'}"
        )
    test_summary_text = " / ".join(test_summaries)

    return f"""
당신은 노안 검사 전문의입니다.
사용자의 누적 검사 이력을 바탕으로 노안 증상 여부와 그 심각도를 정밀하게 판단하고,
전문적인 어조와 예의 있는 말투로 종합적인 comment를 작성하세요.

📌 출력 형식:
- 아래 JSON 형식으로만 응답하세요. 설명이나 기타 텍스트는 포함하지 마세요.
- 총 2문장까지, 문장 당 약 50자 내외로 작성합니다.

<조건 요약>
- comment에는 반드시 아래 2가지가 포함되어야 합니다:
  1. 평균 거리의 경향 및 최근 검사 기준으로 본 노안 증상 유무
     (예: "평균 거리 증가가 반복되어 노안이 의심됩니다.")
  2. 검사자의 상태에 따른 조언 또는 조치 (예: "안과 검진을 받아보시길 권장드립니다.")
- presbyopia_status는 아래 조건에 맞춰 판단하세요:

<판단 기준 예시>
- good: 검사 결과에 이상이 없고 경향성도 안정적일 경우  

- normal: 경미한 변화 또는 평균 거리 증가가 있으나 심각하지 않음  

- bad: 평균 거리 25cm 이상 반복, 이력(수술/흡연/당뇨 등)과 함께 저하 경향

<주의>
- ‘시력이 좋습니다’ 같은 표현은 피하고, 반드시 ‘노안 증상 여부’에 초점을 맞춰 작성하세요.
- ‘평균 거리’ 를 직접적으로 언급하는 것 보다는 ‘가까운 물체가 흐릿하게 보이는 증상이 반복되고 있습니다.’식의 표현을 사용하여 주세요.
- 사용자의 나이와 성별과 같은 개별 정보를 직접 언급하거나 노출하지 마세요.
- ✅ 문진 정보는 반드시 **간접 참고만 하며**, comment에서 **직접 언급하지 마세요.**
  예) “흡연 이력이 있으므로…” → ❌ 금지
  예) “정밀 검진이 필요합니다.” → ✅ OK

예시:
{{
  "comment": "가까운 물체를 볼 때 뚜렷하게 인지되고 있습니다. 현재 상태는 양호하나 정기적인 검진은 지속해 주세요.",
  "presbyopia_status": "good"
}}
또는
{{
  "comment": "가까운 글자가 순간적으로 흐릿하게 느껴지는 경우가 있습니다. 불편함이 지속된다면 안과 상담을 권장드립니다.",
  "presbyopia_status": "normal"
}}
또는는
{{
  "comment": "가까운 물체가 흐릿하게 보이는 증상이 반복되고 있습니다. 노안 가능성을 고려해 안과 검진을 받아보시길 권장드립니다.",
  "presbyopia_status": "bad"
}}

[문진 요약]
- 나이대: {age_group}
- 성별: {gender_text}
- 안경 착용: {"착용" if survey.glasses else "비착용"}
- 수술 이력: {surgery_desc}
- 당뇨 여부: {"있음" if survey.diabetes else "없음"}
- 흡연 여부: {"있음" if survey.smoking else "없음"}

[노안 검사 이력 요약]
총 {len(sorted_list)}회 검사 중 평균 거리 25cm 초과 {abnormal_count}회
{test_summary_text}

[최근 검사]
- 날짜: {latest.created_at.strftime('%Y-%m-%d')}
- 평균 거리: {latest.avg_distance:.1f}cm
- 노안 의심: {"있음" if latest.avg_distance > 25 else "없음"}

⛔ 반드시 위 JSON 구조로만 응답하고, 설명이나 말머리는 포함하지 마세요.
"""
