import { Skeleton } from '@/components/ui/skeleton';

const ProfileInfoSkeleton = () => {
  return (
    <div className="mt-2">
      <Skeleton className="mb-4 h-6 w-28" />
      <Skeleton className="h-40 w-full" />

      <Skeleton className="my-4 h-6 w-28" />
      <Skeleton className="h-20 w-full" />
    </div>
  );
};

export default ProfileInfoSkeleton;
