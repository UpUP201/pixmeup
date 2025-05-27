import { Skeleton } from '@/components/ui/skeleton';
import { EXERCISE_META } from '@/utils/exerciseMeta';

const ExerciseDetailSkeleton = () => {
  const exerciseMeta = EXERCISE_META['1'];

  return (
    <div className="relative flex h-full flex-col overflow-hidden">
      <div className="flex flex-1 flex-col overflow-y-auto px-4 pb-20">
        {/* 상단 운동 정보 스켈레톤 */}
        <div className={`${exerciseMeta.bgClass} flex items-center justify-between rounded-lg p-5`}>
          <div className="flex flex-col gap-2">
            <Skeleton className="h-8 w-48" />
            <Skeleton className="h-5 w-64" />
          </div>
          <Skeleton className="h-6 w-20" />
        </div>

        {/* 운동 이미지 스켈레톤 */}
        <div className="my-5 flex justify-center">
          <Skeleton className="h-44 w-44 rounded-full" />
        </div>

        {/* 운동 설명 스켈레톤 */}
        <div className="flex flex-col gap-2">
          <Skeleton className="h-7 w-48" />
          <Skeleton className="h-20 w-full" />
        </div>

        {/* 구분선 */}
        <div className="border-line-100 my-7 border-t" />

        {/* 운동 순서 스켈레톤 */}
        <div className="flex flex-col gap-4">
          {[1, 2, 3].map((step) => (
            <div key={step} className="flex gap-4">
              <Skeleton className="h-8 w-8 rounded-full" />
              <div className="flex flex-1 flex-col gap-2">
                <Skeleton className="h-5 w-32" />
                <Skeleton className="h-16 w-full" />
              </div>
            </div>
          ))}
        </div>

        <div className="border-line-100 my-4 border-t" />

        {/* 주의사항 및 안내 스켈레톤 */}
        <div className="mb-4 text-sm">
          <Skeleton className="mb-2 h-5 w-20" />
          <Skeleton className="mb-4 h-24 w-full" />
          <Skeleton className="mb-2 h-5 w-16" />
          <Skeleton className="h-24 w-full" />
        </div>
      </div>

      {/* 운동 시작 버튼 스켈레톤 */}
      <div className="absolute right-0 bottom-0 left-0 bg-white p-2 pb-4 shadow-[0_-4px_6px_-1px_rgba(0,0,0,0.05)]">
        <Skeleton className="h-10 w-full" />
      </div>
    </div>
  );
};

export default ExerciseDetailSkeleton;
