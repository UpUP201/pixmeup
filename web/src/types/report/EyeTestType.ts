// 추후 변경 가능
export type EyeTest = '안구 나이' | '시력' | '황반 변성' | '암슬러';

export interface EyeTestResultTypes {
  '안구 나이': {
    age: number | '-';
  };
  시력: {
    leftSight: number | '-';
    rightSight: number | '-';
  };
  '황반 변성': {
    leftEyeVer?: number | '-';
    rightEyeVer?: number | '-';
    leftEyeHor?: number | '-';
    rightEyeHor?: number | '-';
  };
  암슬러: {
    amslerStatus: '정상 시야' | '왜곡' | '암점' | '시야 결손' | '혼합 이상' | undefined;
  };
}

export interface EyeTestResults {
  age: number;
  leftSight: number;
  rightSight: number;
  leftEyeVer?: number;
  rightEyeVer?: number;
  leftEyeHor?: number;
  rightEyeHor?: number;
  amslerStatus?: '정상 시야' | '왜곡' | '암점' | '시야 결손' | '혼합 이상';
}

export const EyeTestTypeLabel: Record<EyeTest, Record<string, string>> = {
  '안구 나이': {
    age: '세 이하',
  },
  시력: {
    left: '좌',
    right: '우',
  },
  '황반 변성': {
    leftEyeVer: '좌',
    rightEyeVer: '우',
    leftEyeHor: '좌',
    rightEyeHor: '우',
  },
  암슬러: {
    result: '',
  },
};

export interface AmsikResult {
  vertical: {
    left: number;
    right: number;
  };
  horizontaol: {
    left: number;
    right: number;
  };
}

// api
// 검사 리포트 용 시력 & 안구 나이
export interface RecentVisionReportResponse {
  name: string;
  age: number;
  leftSight: number;
  rightSight: number;
  createdAt: string;
}

export interface RecentVisionReportRequest {
  selectedDateTime?: string;
}

// 검사 통합 리스트 조회
interface ReportTotal {
  dateTime: string;
  hasSight: boolean;
  hasPresbyopia: boolean;
  hasAmsler: boolean;
  hasMChart: boolean;
}

export interface ReportTotalListResponse {
  content: ReportTotal[];
  number: number;
  last: boolean;
}

// 노안 검사 상세
interface PresbyopiaReport {
  id: number;
  age: number;
  agePrediction: number;
  aiResult: string;
  status: 'BAD' | 'NORMAL' | 'GOOD';
  createdAt: string;
}

export type PresbyopiaReportList = PresbyopiaReport[];

// 근거리 시력검사 상세
interface MyopiaReport {
  id: number;
  leftSight: number;
  rightSight: number;
  leftSightPrediction: number;
  rightSightPrediction: number;
  aiResult: string;
  status: 'BAD' | 'NORMAL' | 'GOOD';
  createdAt: string;
}

export type MyopiaReportList = MyopiaReport[];

// 황반 변성 검사 상세
export type Amsler = 'n' | 'w' | 'b' | 'd';

export interface AmslerReport {
  id: number;
  rightMacularLoc: string;
  leftMacularLoc: string;
  aiResult: string;
  createdAt: string;
}

export interface McharReport {
  id: number;
  leftEyeVer: number;
  rightEyeVer: number;
  leftEyeHor: number;
  rightEyeHor: number;
  aiResult: string;
  createdAt: string;
}
export interface MacularReport {
  amsler: AmslerReport;
  mchart: McharReport;
}

export interface ImagePredictReport {
  id: string;
  userId: number;
  imageUrl: string;
  summary: string;
  description: string;
  createdAt: string;
}

export interface DiagnosisReportResult {
  id: string;
  user_id: number;
  user_name: string;
  risk_percent: number;
  summary: string;
  survey_id: number;
  mchart_check_id: number;
  amsler_check_id: number;
  risk: 'Medium' | 'Low' | 'High';
  created_at: string;
}
