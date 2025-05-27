import { Skeleton } from '../ui/skeleton';

const ImagePredictionSkeleton = () => {
  return (
    <div className="flex h-full flex-col gap-5 px-4 py-5">
      <Skeleton className="mb-2 h-9 w-full" />
      <Skeleton className="mb-9 h-34 w-full" />
      <Skeleton className="h-24 w-full" />
      <Skeleton className="h-40 w-full" />
      <Skeleton className="h-21 w-full" />
      <Skeleton className="mt-auto h-10 w-full" />
    </div>
  );
};

export default ImagePredictionSkeleton;
