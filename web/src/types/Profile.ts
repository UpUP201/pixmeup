export type EyeExamTag = '근거리 시력' | '노안 조절력' | '암슬러 차트' | '엠식 변형시';
export type AIExamTag = 'AMD 검사' | 'AI 정밀 진단';

export interface BaseExamRecord {
  id: string;
  date: Date;
  result?: string;
}

export interface EyeExamRecord extends BaseExamRecord {
  type: 'eye';
  tags: EyeExamTag[];
}

export interface AIExamRecord extends BaseExamRecord {
  type: 'ai';
  tags: AIExamTag[];
}

export type ExamRecord = EyeExamRecord | AIExamRecord;

export interface ExamRecordGroup {
  month: string; // YYYY.MM 형식
  records: ExamRecord[];
}
