import { Link } from 'react-router';
import Icon from '../common/icons/Icon';

const AIButton = () => {
  //"/amd-report/loading"
  // "/image-prediction"
  return (
    <Link
      to="/amd-report/loading"
      replace={true}
      className="to-primary-50 from-secondory-lemon-50 mb-5 flex w-full items-center rounded-lg bg-gradient-to-r p-1"
    >
      <img src="/assets/images/bead.png" alt="AI 아이콘" className="h-15 w-15 contain-content" />
      <span className="text-line-800 text-display-sm font-medium">AI 진단 받아보기</span>
      <Icon name="right" className="mr-2 ml-auto" />
    </Link>
  );
};

export default AIButton;
