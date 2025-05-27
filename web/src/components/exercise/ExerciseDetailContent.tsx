import { ExerciseTimeline, Icon } from '@/components';
import { Button } from '@/components/ui/button';
import { formatDurationToKorean } from '@/utils';
import { EXERCISE_META } from '@/utils/exerciseMeta';
import { useNavigate, useParams } from 'react-router-dom';
import { useGetExerciseDetail } from '@/apis/exercise/exerciseQueries';

const ExerciseDetailContent = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const exerciseMeta = id && EXERCISE_META[id] ? EXERCISE_META[id] : EXERCISE_META['1'];

  const {
    data: { data: exerciseDetail },
  } = useGetExerciseDetail(id!);

  return (
    <div className="relative flex h-full flex-col overflow-hidden">
      <div className="flex flex-1 flex-col overflow-y-auto px-4 pb-20">
        {/* 상단 운동 정보 */}
        <div className={`${exerciseMeta.bgClass} flex items-center justify-between rounded-lg p-5`}>
          <div className="flex flex-col">
            <span className="text-line-900 text-2xl font-bold">
              {exerciseDetail.eyeExerciseName}
            </span>
            <span className="text-line-700 font-medium">{exerciseDetail.eyeExerciseSummary}</span>
          </div>
          <div
            className={`${exerciseMeta.tgClass} flex h-fit items-center gap-1 rounded-sm px-2 py-1`}
          >
            <Icon name="time" className={`${exerciseMeta.tgIconClass}`} size={14} />
            <span className="text-sm">{formatDurationToKorean(exerciseDetail.totalDuration)}</span>
          </div>
        </div>

        {/* 운동 이미지 */}
        <div className="my-5 flex justify-center">
          <img
            src={exerciseDetail.thumbnailUrl.replace(/&amp;/g, '&')}
            alt={`${exerciseDetail.eyeExerciseName} 운동 이미지`}
            className="h-44 w-44"
          />
        </div>

        {/* 운동 설명 */}
        <div className="flex flex-col gap-2">
          <span className="text-line-900 text-xl font-bold">
            {exerciseDetail.eyeExerciseName} 운동이란?
          </span>
          <p className="text-line-700 break-keep whitespace-pre-wrap">
            {exerciseDetail.description}
          </p>
        </div>

        {/* 구분선 */}
        <div className="border-line-100 my-7 border-t" />

        {/* 운동 순서 */}
        <ExerciseTimeline steps={exerciseDetail.eyeExerciseStepList} />

        <div className="border-line-100 my-4 border-t" />

        <div className="mb-4 text-sm">
          {/* 안내 */}
          <span className="text-line-600 mb-2 font-bold">안내</span>
          <p className="text-line-500 mb-4 break-keep whitespace-pre-wrap">
            {exerciseDetail.guidelines}
          </p>
          {/* 주의사항 */}
          <span className="text-secondory-red-600 mb-2 font-bold">주의사항</span>
          <p className="text-line-500 break-keep whitespace-pre-wrap">
            {exerciseDetail.precautions}
          </p>
        </div>
      </div>

      {/* 운동 시작 버튼 - Floating */}
      <div className="absolute right-0 bottom-0 left-0 bg-white p-2 pb-4 shadow-[0_-4px_6px_-1px_rgba(0,0,0,0.05)]">
        <Button
          variant="primary"
          onClick={() => navigate('intro')}
          className="w-full cursor-pointer"
        >
          운동 시작
        </Button>
      </div>
    </div>
  );
};

export default ExerciseDetailContent;
