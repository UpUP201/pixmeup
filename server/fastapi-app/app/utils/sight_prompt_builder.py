def build_prompt(survey, sight_test_list):
    sorted_list = sorted(sight_test_list, key=lambda x: x.created_at)
    latest = sorted_list[-1]

    # 이상 여부 직접 판단
    def is_abnormal(test):
        return (
            test.left_sight / 10 < 0.5 or test.right_sight / 10 < 0.5
        )

    abnormal_count = sum(1 for s in sorted_list if is_abnormal(s))

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
        abnormal_flag = is_abnormal(test)
        test_summaries.append(
            f"{i+1}회차 검사일: {test.created_at.strftime('%Y-%m-%d')}, "
            f"좌안: {test.left_sight / 10:.1f} , "
            f"우안: {test.right_sight / 10:.1f} , "
            f"이상 여부: {'있음' if abnormal_flag else '없음'}"
        )
    test_summary_text = " / ".join(test_summaries)

    return f"""
당신은 시력 검사 전문가입니다.
사용자의 문진 정보와 누적 시력 검사 결과를 분석하고,
전문적이고 정중한 말투로 시력 상태를 평가하는 코멘트를 작성해야 합니다.

<목표>
시력 검사 결과를 바탕으로, 아래 2가지 항목을 반드시 생성하세요:

- comment: 
  1. 좌안/우안 각각의 시력 변화 경향이나 이상 여부를 구체적으로 기술하세요. (예: 좌안 시력 저하 반복, 우안은 경미한 변동 등)
  2. 해당 경향이 의미하는 바와 사용자가 취해야 할 구체적인 조치를 안내하세요.
  3. 결과는 정중하면서도 전문가답게 작성하며, 각 문장은 50자 내외로 간결하게 표현하세요.
  (총 2문장까지 가능, 한글 기준 약 50자 내외, 예의 있는 말투로 마무리)

- sight_status: 
  good / normal / bad 중 하나 선택  
  시력 상태에 대한 전반적 평가 (comment와 논리적으로 일치해야 함)

<출력 형식>
아래 형식의 JSON을 반환하세요. 그 외 텍스트, 설명, 코드블록은 포함하지 마세요.

<출력 예시>
다음은 상황별 예시입니다. comment에 해당 하는 부분을 잘 완성해주세요.

예시:
{{
  "comment": "좌우 시력이 모두 안정적으로 유지되고 있습니다. 현재로서는 정기적인 검진만으로 충분합니다.",
  "sight_status": "good"
}}

또는

{{
  "comment": "최근 좌안은 안정적이나 우안에서 경미한 시력 변동이 관찰됩니다. 정기 검진을 통해 지속 관찰이 필요합니다.",
  "sight_status": "normal"
}}

또는

{{
  "comment": "좌안 시력이 반복적으로 저하되는 경향이 보입니다. 조기 진단을 위해 안과 정밀 검사를 받아보시길 권장드립니다.",
  "sight_status": "bad"
}}

<조건 요약>
- 수치나 성별/나이는 직접 언급하지 마세요.
- 문진 정보(수술, 흡연, 당뇨, 안경 등)는 판단의 근거로 간접 활용 가능합니다.
- comment는 예의 있고 부드럽게 마무리하세요.
- 사용자의 나이와 성별과 같은 개별 정보를 직접 언급하거나 노출하지 마세요.
- sight_status는 아래 기준에 따라 판단하세요:

<판단 기준 예시>
- good: 검사 결과에 이상이 없고 경향성도 안정적일 경우  
- normal: 경미한 시력 이상 또는 변동은 있으나 심각하지 않음  
- bad: 반복된 이상 소견, 저시력, 교정/수술 이력 + 당뇨/흡연 등 리스크 존재  

[문진 요약]
- 나이대: {age_group}
- 성별: {gender_text}
- 안경 착용: {"착용" if survey.glasses else "비착용"}
- 수술 이력: {surgery_desc}
- 당뇨 여부: {"있음" if survey.diabetes else "없음"}
- 흡연 여부: {"있음" if survey.smoking else "없음"}

[검사 이력 요약]
총 {len(sorted_list)}회 검사, 이상 판정 {abnormal_count}회
{test_summary_text}

[최근 검사]
- 날짜: {latest.created_at.strftime('%Y-%m-%d')}
- 좌안: {latest.left_sight / 10:.1f} 
- 우안: {latest.right_sight / 10:.1f} 
- 이상 여부: {"있음" if is_abnormal(latest) else "없음"}
"""