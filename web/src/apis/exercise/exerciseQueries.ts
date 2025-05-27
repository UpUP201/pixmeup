import { useSuspenseQuery, useMutation } from '@tanstack/react-query';
import {
  getExerciseDetailAPI,
  getExerciseListAPI,
  getExerciseRecommendAPI,
  getExerciseStartAPI,
  postExerciseCompleteAPI,
} from './exerciseApi';

// 눈 운동 목록 조회
export const useGetExerciseList = () => {
  return useSuspenseQuery({
    queryKey: ['exercises'],
    queryFn: getExerciseListAPI,
    staleTime: 5 * 60 * 1000, // 5분
  });
};

// 눈 운동 추천 조회
export const useGetExerciseRecommend = () => {
  return useSuspenseQuery({
    queryKey: ['exerciseRecommend'],
    queryFn: getExerciseRecommendAPI,
    staleTime: 5 * 60 * 1000, // 5분
  });
};

// 눈 운동 상세 조회
export const useGetExerciseDetail = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['exerciseDetail', id],
    queryFn: () => getExerciseDetailAPI(id),
    staleTime: 5 * 60 * 1000, // 5분
  });
};

// 눈 운동 시작
export const useGetExerciseStart = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['exerciseStart', id],
    queryFn: () => getExerciseStartAPI(id),
    staleTime: 20 * 1000, // 20초
  });
};

// 눈 운동 완료 기록 저장
export const usePostExerciseComplete = () => {
  return useMutation({
    mutationFn: (eyeExerciseId: string) => postExerciseCompleteAPI(eyeExerciseId),
  });
};
