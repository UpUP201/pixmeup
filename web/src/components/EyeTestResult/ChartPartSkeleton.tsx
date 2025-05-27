import { Skeleton } from '../ui/skeleton';

const ChartPartSkeleton = () => {
  return (
    <>
      <div className="flex w-full flex-col gap-3">
        <div className="flex items-center justify-between">
          <Skeleton className="h-6 w-25" />
          <Skeleton className="h-6 w-18" />
        </div>
        <Skeleton className="h-15 w-full" />
      </div>
      <div
        data-slot="card"
        className="bg-card text-card-foreground flex flex-col gap-6 rounded-xl border-0 py-6 shadow-sm"
      >
        <div
          data-slot="card-header"
          className="grid auto-rows-min grid-rows-[auto_auto] items-start gap-1.5 px-6"
        >
          {/* 카드 타이틀 스켈레톤 */}
          <Skeleton className="h-6 w-20" />
        </div>
        <div data-slot="card-content" className="px-6">
          {/* 차트 영역 스켈레톤 */}
          <Skeleton className="aspect-video h-[172px] w-full" />
        </div>
      </div>
      <div
        data-slot="card"
        className="bg-card text-card-foreground flex flex-col gap-6 rounded-xl border-0 py-6 shadow-sm"
      >
        <div
          data-slot="card-header"
          className="grid auto-rows-min grid-rows-[auto_auto] items-start gap-1.5 px-6"
        >
          {/* 카드 타이틀 스켈레톤 */}
          <Skeleton className="h-6 w-20" />
        </div>
        <div data-slot="card-content" className="px-6">
          {/* 차트 영역 스켈레톤 */}
          <Skeleton className="aspect-video h-[172px] w-full" />
        </div>
      </div>
    </>
  );
};

export default ChartPartSkeleton;
