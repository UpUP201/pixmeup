import { useNavigate } from 'react-router-dom';
import Icon from '../icons/Icon';
import { HeaderProps } from './types';

export const Header = ({ path, title, showBackButton = true }: HeaderProps) => {
  const navigate = useNavigate();

  // 이전 페이지 이동
  const onNavigateBack = () => {
    if (path) {
      navigate(path);
    } else {
      navigate(-1);
    }
  };

  return (
    <header className="flex w-full items-center justify-between px-5 py-4">
      <div className="flex items-center gap-1">
        {showBackButton && (
          <button
            aria-label="뒤로가기"
            onClick={onNavigateBack}
            className="h-5 w-5 cursor-pointer rounded-full pr-1 transition-colors hover:bg-gray-100"
          >
            <Icon name="right" className="h-5 w-5 scale-x-[-1] fill-gray-700" />
          </button>
        )}
        <h1 className="text-lg font-semibold text-gray-900">{title}</h1>
      </div>
    </header>
  );
};
