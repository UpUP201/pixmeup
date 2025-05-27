import { Skeleton } from '@/components/ui/skeleton';

const ProfileActionsSkeleton = () => {
  return (
    <div className="space-y-2">
      <Skeleton className="h-12 w-full rounded-md" />
      <Skeleton className="h-12 w-full rounded-md" />

      <div className="m-3 flex">
        <Skeleton className="mr-1 h-6 w-full flex-1" />
        <Skeleton className="ml-1 h-6 w-full flex-1" />
      </div>
    </div>
  );
};

export default ProfileActionsSkeleton;
