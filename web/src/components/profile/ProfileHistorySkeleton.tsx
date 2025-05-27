import { Skeleton } from '@/components/ui/skeleton';

const ProfileHistorySkeleton = () => {
  return (
    <div className="rounded-sm bg-gray-100">
      <div className="p-4">
        <div className="flex w-full items-center justify-between">
          <div className="flex items-center gap-2">
            <Skeleton className="h-5 w-5 rounded-sm" />
            <Skeleton className="h-5 w-24" />
          </div>
          <Skeleton className="h-5 w-5" />
        </div>
      </div>

      <div className="px-4">
        <div className="mx-auto border-b border-gray-200"></div>
      </div>

      <div className="p-4">
        <div className="flex w-full items-center justify-between">
          <div className="flex items-center gap-2">
            <Skeleton className="h-5 w-5 rounded-sm" />
            <Skeleton className="h-5 w-24" />
          </div>
          <Skeleton className="h-5 w-5" />
        </div>
      </div>
    </div>
  );
};

export default ProfileHistorySkeleton;
