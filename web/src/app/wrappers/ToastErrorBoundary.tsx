import { useNavigate } from 'react-router';
import { DotLottieReact } from '@lottiefiles/dotlottie-react';
import { useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';

const ToastErrorBoundary = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  useEffect(() => {
    queryClient.clear();
  }, []);

  return (
    <div className="flex h-full flex-col items-center justify-center bg-white p-4">
      <div className="mb-6 flex w-full max-w-md justify-center">
        <DotLottieReact
          src="https://lottie.host/d88b9b35-49af-4e97-9944-1f6619cee135/d9EJdZqj1e.lottie"
          autoplay
          className="h-32 w-44"
        />
      </div>
      <p className="text-line-600 mb-6 text-lg">서버와의 통신에 오류가 발생했습니다.</p>
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

export default ToastErrorBoundary;
