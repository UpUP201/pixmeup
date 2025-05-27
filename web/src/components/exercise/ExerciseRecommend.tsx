import { Button } from '@/components/ui/button';
import { useNavigate } from 'react-router';
import { Icon } from '@/components';
import { useGetExerciseRecommend } from '@/apis/exercise/exerciseQueries';

const ExerciseRecommend = () => {
  const navigate = useNavigate();
  const {
    data: { data: recommendExercise },
  } = useGetExerciseRecommend();

  return (
    <div className="from-primary-100/60 to-secondory-lemon-100 bg-gradient-to-br px-5 py-8">
      <div className="mb-2 flex items-center gap-2 text-lg font-semibold">
        <Icon name="ai" className="fill-primary-500" size={20} />
        <p className="text-line-700">
          <span className="text-primary-600">{recommendExercise.userName}</span>님 맞춤 분석
        </p>
      </div>

      <div className="mb-2 flex items-center gap-2 py-3">
        <img src="/assets/images/eye-open.png" alt="eye-open" className="w-24" />
        <div className="text-line-800 flex flex-col text-[28px] font-medium">
          <p>
            <span className="text-primary-500">{recommendExercise.eyeExerciseName}</span>을
          </p>
          <div>추천해요!</div>
        </div>
      </div>
      <Button
        className="w-full cursor-pointer rounded-lg text-lg"
        onClick={() => navigate(`/exercise/${recommendExercise.eyeExerciseId}`)}
      >
        지금 시작하기
      </Button>
    </div>
  );
};

export default ExerciseRecommend;
