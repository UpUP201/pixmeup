import { useNavigate } from 'react-router';
import { Button } from '@/components/ui/button';
import { useUserProfile } from '@/hooks/auth/useUserProfile';
import { useGetExerciseRecommend } from '@/apis/exercise/exerciseQueries';

const RecommendExercise = () => {
  const navigate = useNavigate();
  const { profile } = useUserProfile();
  const {
    data: { data: recommendExercise },
  } = useGetExerciseRecommend();

  return (
    <div>
      <p className="pb-3 text-xl font-semibold">
        추천 <span className="text-primary-600">눈 운동</span>
      </p>

      <div className="bg-line-800 w-full flex-col items-center rounded-sm px-4 py-3 shadow-md">
        <div className="flex w-full items-center justify-between pb-2">
          <div className="text-lg text-white">
            <p>{profile?.name}님에게</p>
            <p>
              {' '}
              <span className="text-primary-400 font-semibold">
                {recommendExercise?.eyeExerciseName}
              </span>
              을
            </p>
            <p>추천해요!</p>
          </div>
          <div>
            <img src="assets/images/eye-open-white.png" className="w-22" alt="눈운동" />
          </div>
        </div>

        <div>
          <Button
            variant="primary"
            className="w-full cursor-pointer"
            onClick={() => navigate(`/exercise/${recommendExercise?.eyeExerciseId}`)}
          >
            바로 시작하기
          </Button>
        </div>
      </div>
    </div>
  );
};

export default RecommendExercise;
