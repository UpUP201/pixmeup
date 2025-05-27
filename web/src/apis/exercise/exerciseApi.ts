import { fetchAPI } from '@/lib/fetchAPI';
import {
  ExerciseCompleteRequest,
  ExerciseCompleteResponse,
  ExerciseDetailResponse,
  ExerciseListResponse,
  ExerciseRecommendationResponse,
  ExerciseStartResponse,
} from '@/types/exercise';

// 눈 운동 목록 조회
export const getExerciseListAPI = () =>
  fetchAPI<ExerciseListResponse>({
    method: 'GET',
    url: '/exercises',
  });

// 눈 운동 추천 조회
export const getExerciseRecommendAPI = () =>
  fetchAPI<ExerciseRecommendationResponse>({
    method: 'GET',
    url: '/exercises/recommend',
  });

// 눈 운동 상세 조회
export const getExerciseDetailAPI = (id: string) =>
  fetchAPI<ExerciseDetailResponse>({
    method: 'GET',
    url: `/exercises/${id}`,
  });

// 눈 운동 시작
export const getExerciseStartAPI = (id: string) =>
  fetchAPI<ExerciseStartResponse>({
    method: 'GET',
    url: `/exercises/${id}/start`,
  });

// 눈 운동 완료 기록 저장
export const postExerciseCompleteAPI = (eyeExerciseId: string) =>
  fetchAPI<ExerciseCompleteResponse, ExerciseCompleteRequest>({
    method: 'POST',
    url: '/exercises/complete',
    data: { eyeExerciseId },
  });
