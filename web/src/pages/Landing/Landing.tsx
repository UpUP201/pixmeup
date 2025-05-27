import { useNavigate } from 'react-router-dom';
import { useSignup } from '@/hooks/auth/useSignup.ts';

const LandingPage = () => {
  const navigate = useNavigate();
  const { handleDeviceAuthenticate } = useSignup();

  return (
    <div className="from-secondory-lemon-50 to-primary-50 flex min-h-screen flex-col justify-between bg-gradient-to-tl via-white text-center">
      {/* 상단 영역 */}
      <div className="container mx-auto px-4 pt-34">
        <div className="space-y-2 text-center">
          <h1 className="text-4xl font-bold text-gray-900">PixmeUp</h1>
          <p className="mb-10 text-lg">똑똑한 건강 관리, 픽미업</p>
        </div>
      </div>

      {/* 버튼 영역 */}
      <div className="flex flex-col items-center space-y-3 pt-22">
        <button
          onClick={handleDeviceAuthenticate}
          className="bg-primary-500 w-60 rounded-lg px-8 py-3 font-semibold text-white transition-colors"
        >
          간편 로그인
        </button>
        <button
          onClick={() => navigate('/login')}
          className="border-primary-100 bg-primary-100 text-primary-600 w-60 rounded-lg border-2 px-8 py-3 font-semibold transition-colors"
        >
          로그인
        </button>
        <button
          onClick={() => navigate('/signup')}
          className="border-primary-500 text-primary-500 w-60 rounded-lg border-2 bg-white px-8 py-3 font-semibold transition-colors"
        >
          회원가입
        </button>
      </div>

      {/* 푸터 문구 */}
      <p className="text-line-300 pb-10 text-sm">with @pixelro</p>
    </div>
  );
};

export default LandingPage;
