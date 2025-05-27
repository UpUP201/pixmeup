import { Link } from 'react-router';
import Icon from '../common/icons/Icon';

const EyeExerciseButton = () => {
  return (
    <Link to="/exercise" className="bg-secondory-lemon-50 flex w-full items-center rounded-lg p-3">
      <img src="/assets/images/bead.png" alt="AI 아이콘" className="h-15 w-15 contain-content" />
      <span className="text-line-800 text-display-sm font-bold">안구 운동 하러가기</span>
      <Icon name="right" className="mr-2 ml-auto" />
    </Link>
  );
};

export default EyeExerciseButton;
