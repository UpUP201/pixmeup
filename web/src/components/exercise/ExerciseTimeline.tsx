import { ExerciseStep } from '@/types/exercise';
import ExerciseTimelineItem from './ExerciseTimelineItem';
import { formatTimelineStepTime } from '@/utils';

interface ExerciseTimelineProps {
  steps: ExerciseStep[];
}

const ExerciseTimeline = ({ steps }: ExerciseTimelineProps) => (
  <div className="flex flex-col gap-6">
    <div className="text-line-900 text-xl font-bold">운동 순서</div>
    <div>
      {steps.map((step) => (
        <ExerciseTimelineItem
          key={step.stepOrder}
          time={formatTimelineStepTime(step.stepDuration)}
          title={step.title}
          description={step.instruction}
        />
      ))}
      {/* 운동 종료 스텝 */}
      <ExerciseTimelineItem
        time="END"
        title="운동 종료"
        description="모든 동작이 끝났습니다. 수고하셨습니다!"
        isLast={true}
      />
    </div>
  </div>
);

export default ExerciseTimeline;
