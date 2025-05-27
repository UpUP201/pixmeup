import { Skeleton } from '@/components/ui/skeleton';

const ProfileHeaderSkeleton = () => {
  return (
    <div>
      <Skeleton className="mb-1 h-7 w-36" />
      <Skeleton className="h-5 w-48" />
    </div>
  );
};

export default ProfileHeaderSkeleton;
