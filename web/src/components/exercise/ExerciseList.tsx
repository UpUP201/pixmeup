import { ExerciseCard } from '@/components';
import { EXERCISE_META } from '@/utils/exerciseMeta';
import { formatDurationToKorean } from '@/utils';
import { useGetExerciseList } from '@/apis/exercise/exerciseQueries';

const ExerciseList = () => {
  const {
    data: { data: exerciseList },
  } = useGetExerciseList();

  return (
    <div className="grid grid-cols-2 gap-3">
      {exerciseList?.map((ex) => (
        <ExerciseCard
          key={ex.eyeExerciseId}
          id={ex.eyeExerciseId}
          name={ex.eyeExerciseName}
          time={formatDurationToKorean(ex.totalDuration)}
          iconName={EXERCISE_META[ex.eyeExerciseId].iconName}
          iconClass={EXERCISE_META[ex.eyeExerciseId].iconClass}
          bgClass={EXERCISE_META[ex.eyeExerciseId].bgClass}
        />
      ))}
    </div>
  );
};

export default ExerciseList;
