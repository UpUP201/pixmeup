import { Skeleton } from '@/components/ui/skeleton';

const ExerciseListSkeleton = () => {
  return (
    <div className="grid grid-cols-2 gap-3">
      {Array.from({ length: 4 }).map((_, index) => (
        <div key={index} className="bg-line-50 flex flex-col rounded-xl p-4">
          <Skeleton className="bg-line-100 h-7 w-3/4" />
          <div className="mt-1 flex items-center gap-1">
            <Skeleton className="bg-line-100 h-4 w-16" />
          </div>
          <div className="mt-7 flex w-full justify-end">
            <div className="bg-line-100 h-20 w-20 rounded-full" />
          </div>
        </div>
      ))}
    </div>
  );
};

export default ExerciseListSkeleton;
