import { DotLottieReact } from '@lottiefiles/dotlottie-react';
import { useNavigate } from 'react-router-dom';

const ErrorPage = () => {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-white p-4">
      <div className="mb-6 w-full max-w-md">
        <DotLottieReact
          src="https://lottie.host/b3a39df3-a615-4dc6-9434-e69b7fedfbd8/4J0QuzXNkJ.lottie"
          loop
          autoplay
          className="h-64 w-full"
        />
      </div>

      <p className="text-line-600 mb-6 text-lg">요청하신 페이지를 찾을 수 없습니다.</p>

      <div className="flex space-x-4">
        <button
          onClick={() => {
            navigate(-1);
          }}
          className="bg-primary-500 rounded-sm px-6 py-2 text-white transition-colors"
        >
          뒤로가기
        </button>
      </div>
    </div>
  );
};

export default ErrorPage;
