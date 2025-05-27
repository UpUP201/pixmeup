import { Skeleton } from '../ui/skeleton';

const AMDResultSkeleton = () => {
  return (
    <div className="flex flex-col gap-4 px-4 py-5">
      {/* 상단 텍스트 영역 Skeleton */}
      <div className="mb-1 flex flex-col">
        <Skeleton className="mb-2 h-7 w-64"></Skeleton>
        <Skeleton className="mb-1 h-9 w-72"></Skeleton>
        <Skeleton className="h-9 w-40"></Skeleton>
      </div>

      {/* 결과 박스 Skeleton */}
      <div className="border-line-50 rounded-xl border px-6 py-3">
        {/* 퍼센트 표시 영역 */}
        <Skeleton className="mb-3 h-10 w-24"></Skeleton>

        <div className="relative flex flex-col">
          {/* 이미지 영역 대체 */}
          <Skeleton className="absolute right-0 bottom-0 h-24 w-40 translate-x-1/10 -translate-y-2/5"></Skeleton>

          {/* 요약 텍스트 영역 */}
          <Skeleton className="mb-2 h-6 w-20"></Skeleton>
          <Skeleton className="h-6 w-full"></Skeleton>
          <Skeleton className="mt-1 h-6 w-4/5"></Skeleton>
        </div>
      </div>

      {/* 하단 버튼 영역 Skeleton */}
      <div className="flex flex-col gap-3">
        <Skeleton className="h-6 w-48"></Skeleton>
        <Skeleton className="h-14 w-full"></Skeleton>
      </div>
    </div>
  );
};

export default AMDResultSkeleton;
