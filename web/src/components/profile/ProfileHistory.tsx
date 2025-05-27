import { useNavigate } from 'react-router-dom';
import Icon from '@/components/common/icons/Icon';

const ProfileHistory = () => {
  const navigate = useNavigate();

  return (
    <div className="bg-primary-50 rounded-sm">
      <button
        onClick={() => navigate('/profile/eyehistory')}
        className="flex w-full cursor-pointer items-center justify-between p-4"
      >
        <div className="flex items-center gap-2">
          <Icon name="clipboard" className="text-line-900 h-5 w-5" />
          <span className="font-semibold">눈 검사 기록</span>
        </div>

        <Icon name="right" className="text-line-900 h-5 w-5" />
      </button>
      <div className="px-4">
        <div className="border-primary-100 mx-auto border-b"></div>
      </div>
      <button
        onClick={() => navigate('/profile/aihistory')}
        className="flex w-full cursor-pointer items-center justify-between p-4"
      >
        <div className="flex items-center gap-2">
          <Icon name="robot" className="text-line-900 h-5 w-5" />
          <span className="font-semibold">AI 진단 기록</span>
        </div>
        <Icon name="right" className="text-line-900 h-5 w-5" />
      </button>
    </div>
  );
};

export default ProfileHistory;
