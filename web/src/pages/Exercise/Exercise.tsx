import {
  ExerciseList,
  ExerciseListSkeleton,
  ExerciseRecommend,
  ExerciseRecommendSkeleton,
} from '@/components';
import { Suspense } from 'react';

const Exercise = () => {
  return (
    <div className="flex w-full flex-col pb-20">
      <Suspense fallback={<ExerciseRecommendSkeleton />}>
        <ExerciseRecommend />
      </Suspense>
      <div className="bg-line-50 h-2 w-full"></div>
      {/* 운동 목록 영역 */}
      <div className="mt-2 flex-1 bg-white px-5">
        <div className="py-3 text-xl font-bold">눈 운동</div>
        <div className="text-line-800 mb-6 text-base">
          눈의 피로를 풀어주는 다양한 운동을 모았어요.
          <br />
          꾸준히 따라하면 눈 건강에 큰 도움이 됩니다!
        </div>
        <Suspense fallback={<ExerciseListSkeleton />}>
          <ExerciseList />
        </Suspense>
      </div>
    </div>
  );
};

export default Exercise;
