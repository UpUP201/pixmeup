import { ExerciseStepContent, ExerciseStepSkeleton } from '@/components';
import { Suspense } from 'react';

const ExerciseStep = () => {
  return (
    <Suspense fallback={<ExerciseStepSkeleton />}>
      <ExerciseStepContent />
    </Suspense>
  );
};

export default ExerciseStep;
