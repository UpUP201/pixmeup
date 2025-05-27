import { useNavigate } from 'react-router-dom';
import { IconName } from '../common/icons/types';
import Icon from '../common/icons/Icon';

interface ExerciseCardProps {
  id: string;
  name: string;
  time: string;
  iconName: IconName;
  iconClass: string;
  bgClass: string;
}

const ExerciseCard = ({ id, name, time, iconName, iconClass, bgClass }: ExerciseCardProps) => {
  const navigate = useNavigate();

  return (
    <div
      className={`${bgClass} text-line-900 flex cursor-pointer flex-col rounded-xl p-4`}
      onClick={() => navigate(`/exercise/${id}`)}
    >
      <span className="text-lg font-semibold">{name}</span>
      <div className="text-line-500 flex items-center gap-1">
        {/* 시간 정보는 추후 props로 */}
        <Icon name="time" size={14} />
        {time}
      </div>
      <div className="mt-7 flex w-full justify-end">
        <Icon name={iconName} size={80} className={iconClass} />
      </div>
    </div>
  );
};

export default ExerciseCard;
