import { ExerciseDetailContent, ExerciseDetailSkeleton } from '@/components';
import { Suspense } from 'react';

const ExerciseDetail = () => {
  return (
    <Suspense fallback={<ExerciseDetailSkeleton />}>
      <ExerciseDetailContent />
    </Suspense>
  );
};

export default ExerciseDetail;
