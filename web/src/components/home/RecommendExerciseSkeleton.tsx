import { Skeleton } from '@/components/ui/skeleton';

const RecommendExerciseSkeleton = () => {
  return (
    <div>
      <Skeleton className="mb-3 h-7 w-40" />
      <Skeleton className="h-36 w-full rounded-sm" />
    </div>
  );
};

export default RecommendExerciseSkeleton;
