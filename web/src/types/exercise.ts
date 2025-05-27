export interface Exercise {
  eyeExerciseId: string;
  eyeExerciseName: string;
  totalDuration: number;
}

export interface ExerciseDetail extends Exercise {
  eyeExerciseSummary: string;
  description: string;
  precautions: string;
  guidelines: string;
  totalSteps: number;
  thumbnailUrl: string;
}

export interface ExerciseStep {
  stepOrder: string;
  title: string;
  instruction: string;
  stepDuration: number;
}

export interface ExerciseStepWithMedia extends ExerciseStep {
  stepImageUrl: string;
  stepTtsUrl: string;
}

// 운동 추천
export type ExerciseRecommendationResponse = Pick<Exercise, 'eyeExerciseId' | 'eyeExerciseName'> & {
  userName: string;
};

// 운동 목록 조회
export type ExerciseListItem = Pick<
  Exercise,
  'eyeExerciseId' | 'eyeExerciseName' | 'totalDuration'
>;
export type ExerciseListResponse = ExerciseListItem[];

// 운동 상세 조회
export type ExerciseDetailResponse = ExerciseDetail & {
  eyeExerciseStepList: ExerciseStep[];
};

// 운동 진행 시작
export type ExerciseStartResponse = Pick<
  ExerciseDetail,
  'eyeExerciseId' | 'eyeExerciseName' | 'totalSteps' | 'thumbnailUrl'
> & {
  eyeExerciseStepList: ExerciseStepWithMedia[];
};

export type ExerciseCompleteRequest = Pick<Exercise, 'eyeExerciseId'>;
export type ExerciseCompleteResponse = Pick<Exercise, 'eyeExerciseId'>;
