import { ExerciseStepWithMedia } from '@/types/exercise';

const ProgressBar = ({
  steps,
  currentStep,
}: {
  steps: ExerciseStepWithMedia[];
  currentStep: number;
}) => {
  return (
    <div className="flex w-full items-center gap-1">
      {steps.map((_, idx) => (
        <div
          key={idx}
          className={`h-1 flex-1 rounded-lg ${idx <= currentStep ? 'bg-primary-400' : 'bg-line-100'}`}
        />
      ))}
    </div>
  );
};

export default ProgressBar;
