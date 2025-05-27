import PDFDocument from '@/types/PDFDocument';
import { Document, Page, Text, View, StyleSheet, Font } from '@react-pdf/renderer';

// 폰트 등록 - 스포카 한 산스 네오 사용
Font.register({
  family: 'Spoqa Han Sans Neo',
  fonts: [
    {
      src: 'https://cdn.jsdelivr.net/gh/spoqa/spoqa-han-sans@latest/Subset/SpoqaHanSansNeo/SpoqaHanSansNeo-Regular.woff2',
      fontWeight: 'normal',
      fontStyle: 'normal',
    },
    {
      src: 'https://cdn.jsdelivr.net/gh/spoqa/spoqa-han-sans@latest/Subset/SpoqaHanSansNeo/SpoqaHanSansNeo-Bold.woff2',
      fontWeight: 'bold',
      fontStyle: 'normal',
    },
    {
      src: 'https://cdn.jsdelivr.net/gh/spoqa/spoqa-han-sans@latest/Subset/SpoqaHanSansNeo/SpoqaHanSansNeo-Medium.woff2',
      fontWeight: 'medium',
      fontStyle: 'normal',
    },
  ],
});

// 색상 테마 정의
const colors = {
  primary: '#0B4990',
  secondary: '#3F97FF',
  background: '#FFFFFF',
  patientInfo: '#EBF2FA',
  medicalHistory: '#F8FAFC',
  visualAcuity: '#E8F4FF',
  eyeAge: '#E1F0FF',
  mChart: '#EFF8FF',
  amsler: '#F0F7FF',
  text: {
    dark: '#1E293B',
    medium: '#475569',
    light: '#64748B',
  },
  border: '#E2E8F0',
};

// 스타일 정의 - 간격 축소 및 레이아웃 최적화
const styles = StyleSheet.create({
  page: {
    padding: 20, // 여백 축소
    backgroundColor: colors.background,
    fontFamily: 'Spoqa Han Sans Neo',
    fontSize: 9, // 기본 폰트 크기 축소
    color: colors.text.dark,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10, // 간격 축소
    borderBottom: `1.5px solid ${colors.primary}`,
    paddingBottom: 5, // 간격 축소
  },
  title: {
    fontSize: 16, // 크기 축소
    fontWeight: 'bold',
    color: colors.primary,
  },
  headerRight: {
    flexDirection: 'column',
    alignItems: 'flex-end',
  },
  dateText: {
    fontSize: 7, // 크기 축소
    color: colors.text.light,
  },
  // 컨텐츠 컨테이너 - 간격 축소
  contentContainer: {
    display: 'flex',
    flexDirection: 'column',
    gap: 6, // 간격 축소
  },
  // 환자 정보 섹션
  patientSection: {
    padding: 8, // 패딩 축소
    backgroundColor: colors.patientInfo,
    borderRadius: 4,
    borderLeft: `3px solid ${colors.primary}`,
    marginBottom: 6, // 간격 축소
  },
  // 일반 섹션 베이스 스타일
  section: {
    padding: 6, // 패딩 축소
    backgroundColor: colors.medicalHistory,
    borderRadius: 4,
    borderLeft: `2px solid ${colors.secondary}`,
    marginBottom: 6, // 간격 축소
  },
  // 검사 별 다른 색상 적용
  visualAcuitySection: {
    padding: 6, // 패딩 축소
    backgroundColor: colors.visualAcuity,
    borderRadius: 4,
    borderLeft: `2px solid ${colors.secondary}`,
    marginBottom: 6, // 간격 축소
  },
  eyeAgeSection: {
    padding: 6, // 패딩 축소
    backgroundColor: colors.eyeAge,
    borderRadius: 4,
    borderLeft: `2px solid ${colors.secondary}`,
    marginBottom: 6, // 간격 축소
  },
  mChartSection: {
    padding: 6, // 패딩 축소
    backgroundColor: colors.mChart,
    borderRadius: 4,
    borderLeft: `2px solid ${colors.secondary}`,
    marginBottom: 6, // 간격 축소
  },
  amslerSection: {
    padding: 6, // 패딩 축소
    backgroundColor: colors.amsler,
    borderRadius: 4,
    borderLeft: `2px solid ${colors.secondary}`,
    marginBottom: 6, // 간격 축소
  },
  sectionTitle: {
    fontSize: 10, // 크기 축소
    fontWeight: 'bold',
    marginBottom: 4, // 간격 축소
    color: colors.primary,
    borderBottom: `1px solid ${colors.border}`,
    paddingBottom: 2, // 간격 축소
    textAlign: 'left', // 왼쪽 정렬
  },
  twoColumns: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  column: {
    width: '50%',
  },
  threeColumns: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  thirdColumn: {
    width: '33.33%',
  },
  // 양 끝 정렬을 위한 행 스타일 수정
  row: {
    flexDirection: 'row',
    marginBottom: 2, // 간격 축소
    paddingVertical: 1, // 간격 축소
    justifyContent: 'space-between', // 양 끝 정렬
    paddingRight: 8,
  },
  label: {
    fontSize: 8, // 크기 축소
    fontWeight: 'bold',
    color: colors.text.medium,
  },
  value: {
    fontSize: 8, // 크기 축소
    color: colors.primary,
    textAlign: 'right', // 오른쪽 정렬
  },
  valueHighlight: {
    fontSize: 8, // 크기 축소
    fontWeight: 'bold',
    color: colors.primary,
    textAlign: 'right', // 오른쪽 정렬
  },
  footer: {
    marginTop: 8, // 간격 축소
    padding: 5, // 패딩 축소
    borderTop: `1px solid ${colors.border}`,
    fontSize: 6, // 크기 축소
    color: colors.text.light,
    textAlign: 'center',
  },
  // 상태 태그 스타일
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end', // 오른쪽 정렬
  },
  statusTag: {
    paddingVertical: 1, // 패딩 축소
    paddingHorizontal: 4, // 패딩 축소
    borderRadius: 3,
    fontSize: 7, // 크기 축소
    color: 'white',
    textAlign: 'center',
  },
  statusNormal: {
    backgroundColor: '#10B981',
  },
  statusWarning: {
    backgroundColor: '#F59E0B',
  },
  statusDanger: {
    backgroundColor: '#EF4444',
  },
  aiResultBox: {
    marginTop: 2, // 간격 축소
    padding: 4, // 패딩 축소
    backgroundColor: '#F1F5F9',
    borderRadius: 3,
    fontSize: 7, // 크기 축소
  },
  aiResultTitle: {
    fontWeight: 'bold',
    marginBottom: 1, // 간격 축소
    fontSize: 7, // 크기 축소
  },
  aiResultText: {
    fontSize: 7, // 크기 축소
    color: colors.text.medium,
  },
  pageNumber: {
    position: 'absolute',
    bottom: 10, // 위치 조정
    right: 10, // 위치 조정
    fontSize: 6, // 크기 축소
    color: colors.text.light,
  },
});

// 상태에 따른 스타일 선택 함수
const getStatusStyle = (status: string | null) => {
  if (!status) return styles.statusNormal;

  if (status.includes('정상') || status.includes('좋음')) {
    return styles.statusNormal;
  } else if (status.includes('주의') || status.includes('관찰')) {
    return styles.statusWarning;
  } else {
    return styles.statusDanger;
  }
};

// 불리언 값 변환 함수
const formatBoolean = (value?: boolean) => {
  return value ? '예' : '아니오';
};

// 데이터 포맷팅 함수
const formatDate = (dateString: string) => {
  if (!dateString) return null;

  const date = new Date(dateString);
  return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;
};

// EyeDocument 컴포넌트
const EyeDocument = ({ data }: { data: PDFDocument }) => {
  // 현재 날짜 계산
  const today = new Date();
  const formattedDate = `${today.getFullYear()}년 ${today.getMonth() + 1}월 ${today.getDate()}일`;

  return (
    <Document>
      <Page size="A4" style={styles.page}>
        {/* 헤더 섹션 */}
        <View style={styles.header}>
          <Text style={styles.title}>눈 건강 검진 결과 보고서</Text>
          <View style={styles.headerRight}>
            <Text style={styles.dateText}>발행일: {formattedDate}</Text>
          </View>
        </View>

        {/* 전체 컨텐츠 영역 (세로 배치) */}
        <View style={styles.contentContainer}>
          {/* 환자 정보 섹션 - 양 끝 정렬 */}
          <View style={styles.patientSection}>
            <Text style={styles.sectionTitle}>환자 정보</Text>
            <View style={styles.twoColumns}>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>이름</Text>
                  <Text style={styles.valueHighlight}>{data.name}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>성별</Text>
                  <Text style={styles.value}>{data.gender}</Text>
                </View>
              </View>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>전화번호</Text>
                  <Text style={styles.value}>{data.phoneNumber}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>나이</Text>
                  <Text style={styles.value}>{data.age}</Text>
                </View>
              </View>
            </View>
            <View style={styles.row}>
              <Text style={styles.label}>마지막 검사일</Text>
              <Text style={styles.value}>{formatDate(data.latestTestDate)}</Text>
            </View>
          </View>

          {/* 문진 기록 섹션 - 양 끝 정렬 */}
          <View style={styles.section}>
            <Text style={styles.sectionTitle}>문진 기록</Text>
            <View style={styles.twoColumns}>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>과거 흡연</Text>
                  <Text style={styles.value}>{formatBoolean(data.pastSmoking)}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>현재 흡연</Text>
                  <Text style={styles.value}>{formatBoolean(data.currentSmoking)}</Text>
                </View>
              </View>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>안경 착용</Text>
                  <Text style={styles.value}>{formatBoolean(data.glasses)}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>당뇨병</Text>
                  <Text style={styles.value}>{formatBoolean(data.diabetes)}</Text>
                </View>
              </View>
            </View>
            <View style={styles.row}>
              <Text style={styles.label}>눈 수술 이력</Text>
              <Text style={styles.value}>{data.surgery}</Text>
            </View>
          </View>

          {/* 시력 검사 결과 - 양 끝 정렬 */}
          <View style={styles.visualAcuitySection}>
            <Text style={styles.sectionTitle}>시력 검사 결과</Text>
            <View style={styles.twoColumns}>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>좌안 시력</Text>
                  <Text style={styles.valueHighlight}>{data.sightCheck.leftSight}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>우안 시력</Text>
                  <Text style={styles.valueHighlight}>{data.sightCheck.rightSight}</Text>
                </View>
              </View>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>좌안 예측 시력</Text>
                  <Text style={styles.value}>{data.sightCheck.leftSightPrediction}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>우안 예측 시력</Text>
                  <Text style={styles.value}>{data.sightCheck.rightSightPrediction}</Text>
                </View>
              </View>
            </View>
            <View style={styles.row}>
              <Text style={styles.label}>상태</Text>
              <View style={styles.statusContainer}>
                <Text style={[styles.statusTag, getStatusStyle(data.sightCheck.status)]}>
                  {data.sightCheck.status}
                </Text>
              </View>
            </View>
            <View style={styles.aiResultBox}>
              <Text style={styles.aiResultTitle}>AI 분석 결과:</Text>
              <Text style={styles.aiResultText}>{data.sightCheck.aiResult}</Text>
            </View>
          </View>

          {/* 안구 나이 검사 결과 - 양 끝 정렬 */}
          <View style={styles.eyeAgeSection}>
            <Text style={styles.sectionTitle}>안구 나이 검사 결과</Text>
            <View style={styles.twoColumns}>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>안구 나이</Text>
                  <Text style={styles.valueHighlight}>{data.presbyopiaCheck.age}세</Text>
                </View>
              </View>
              <View style={styles.column}>
                <View style={styles.row}>
                  <Text style={styles.label}>예측 안구 나이</Text>
                  <Text style={styles.value}>{data.presbyopiaCheck.agePrediction}세</Text>
                </View>
              </View>
            </View>
            <View style={styles.row}>
              <Text style={styles.label}>상태</Text>
              <View style={styles.statusContainer}>
                <Text style={[styles.statusTag, getStatusStyle(data.presbyopiaCheck.status)]}>
                  {data.presbyopiaCheck.status}
                </Text>
              </View>
            </View>
            <View style={styles.aiResultBox}>
              <Text style={styles.aiResultTitle}>AI 분석 결과:</Text>
              <Text style={styles.aiResultText}>{data.presbyopiaCheck.aiResult}</Text>
            </View>
          </View>

          {/* 3단 레이아웃 - 하단 검사 결과 */}
          <View style={styles.twoColumns}>
            {/* 왼쪽 - 엠차트 */}
            <View style={[styles.column, { paddingRight: 3 }]}>
              <View style={styles.mChartSection}>
                <Text style={styles.sectionTitle}>엠차트 검사 결과</Text>
                <View style={styles.row}>
                  <Text style={styles.label}>좌안 수직</Text>
                  <Text style={styles.value}>{data.mChartCheck.leftEyeVer}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>우안 수직</Text>
                  <Text style={styles.value}>{data.mChartCheck.rightEyeVer}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>좌안 수평</Text>
                  <Text style={styles.value}>{data.mChartCheck.leftEyeHor}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>우안 수평</Text>
                  <Text style={styles.value}>{data.mChartCheck.rightEyeHor}</Text>
                </View>
                <View style={styles.aiResultBox}>
                  <Text style={styles.aiResultTitle}>AI 분석:</Text>
                  <Text style={styles.aiResultText}>{data.mChartCheck.aiResult}</Text>
                </View>
              </View>
            </View>

            {/* 오른쪽 - 암슬러 */}
            <View style={[styles.column, { paddingLeft: 3 }]}>
              <View style={styles.amslerSection}>
                <Text style={styles.sectionTitle}>암슬러 검사 결과</Text>
                <View style={styles.row}>
                  <Text style={styles.label}>좌안 암슬러</Text>
                  <Text style={styles.value}>{data.amslerCheck.leftMacularLoc}</Text>
                </View>
                <View style={styles.row}>
                  <Text style={styles.label}>우안 암슬러</Text>
                  <Text style={styles.value}>{data.amslerCheck.rightMacularLoc}</Text>
                </View>
                <View style={styles.aiResultBox}>
                  <Text style={styles.aiResultTitle}>AI 분석:</Text>
                  <Text style={styles.aiResultText}>{data.amslerCheck.aiResult}</Text>
                </View>
              </View>
            </View>
          </View>
        </View>

        {/* 푸터 섹션 */}
        <View style={styles.footer}>
          <Text>본 보고서는 {formattedDate}에 생성되었습니다. © 2025 눈 건강 클리닉</Text>
          <Text style={{ marginTop: 2 }}>
            본 결과는 참고용으로만 사용되어야 하며, 정확한 진단을 위해서는 전문의와 상담하시기
            바랍니다.
          </Text>
        </View>

        {/* 페이지 번호 */}
        <Text style={styles.pageNumber}>1/1</Text>
      </Page>
    </Document>
  );
};

export default EyeDocument;
