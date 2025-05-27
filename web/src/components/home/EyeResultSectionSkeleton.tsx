import { Skeleton } from '@/components/ui/skeleton';

const EyeResultSectionSkeleton = () => {
  return (
    <div>
      <Skeleton className="mb-3 h-7 w-40" />

      <div className="flex gap-2">
        <Skeleton className="mt-3 h-24 w-full rounded-sm" />
        <Skeleton className="mt-3 h-24 w-full rounded-sm" />
      </div>
      <div className="flex gap-2">
        <Skeleton className="mt-3 h-24 w-full rounded-sm" />
        <Skeleton className="mt-3 h-24 w-full rounded-sm" />
      </div>

      <Skeleton className="mt-3 h-24 w-full rounded-sm" />
    </div>
  );
};

export default EyeResultSectionSkeleton;
