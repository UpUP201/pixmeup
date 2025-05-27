import { Skeleton } from '@/components/ui/skeleton';
import { ProgressBar } from '@/components';

const ExerciseStepSkeleton = () => {
  return (
    <div className="flex h-full flex-col">
      <div className="mb-16 flex flex-1 flex-col justify-between p-5">
        <ProgressBar steps={[]} currentStep={0} />
        <div className="flex w-full flex-1 flex-col items-center justify-between gap-5 py-8">
          <div className="flex w-full justify-end">
            <Skeleton className="h-20 w-20 rounded-full" />
          </div>
          <div className="flex flex-col items-center gap-6">
            <Skeleton className="h-50 w-50 rounded-full" />
            <div className="flex flex-col items-center gap-2">
              <Skeleton className="h-6 w-72" />
              <Skeleton className="h-6 w-64" />
            </div>
          </div>
          <div className="flex w-full items-center justify-between">
            <Skeleton className="h-21 w-21 rounded-full" />
            <Skeleton className="h-21 w-21 rounded-full" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExerciseStepSkeleton;
