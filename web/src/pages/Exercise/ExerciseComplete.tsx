import { Icon } from '@/components';
import { Button } from '@/components/ui/button';
import { useNavigate, useParams } from 'react-router-dom';
import { usePostExerciseComplete } from '@/apis/exercise/exerciseQueries';

const ExerciseComplete = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const { mutate: postExerciseComplete } = usePostExerciseComplete();

  const handleComplete = () => {
    if (id) {
      postExerciseComplete(id, {
        onSuccess: () => {
          navigate('/exercise');
        },
      });
    }
  };

  return (
    <div className="bg-primary-50 flex h-full flex-col">
      <div className="mb-16 flex flex-1 flex-col items-center justify-between p-5">
        <div className="bg-primary-400 h-1 w-full rounded-lg"></div>
        <div>
          <Icon name="check-circle" className="fill-primary-800 mx-auto my-2" size={44} />
          <span className="text-primary-800 text-3xl font-bold">수고하셨습니다!</span>
        </div>
        <Button
          className="bg-primary-900 mb-6 w-[240px] cursor-pointer rounded-4xl py-4"
          onClick={handleComplete}
        >
          끝내기
        </Button>
      </div>
    </div>
  );
};

export default ExerciseComplete;
