import { useUserProfile } from '@/hooks/auth/useUserProfile';
import { useAIPredictionStore } from '@/stores';
import { useEffect } from 'react';
import { useNavigate } from 'react-router';

const ImagePredictionLoading = () => {
  const navigate = useNavigate();
  const isStreaming = useAIPredictionStore((state) => state.isStreaming);
  const imagePredictionResult = useAIPredictionStore((state) => state.imagePredictResult);

  const { profile } = useUserProfile();

  useEffect(() => {
    // isStreaming이 false이고 분석 결과가 있으면 결과 페이지로, 없으면 에러 페이지로 이동
    if (!isStreaming) {
      const redirectTimer = setTimeout(() => {
        if (imagePredictionResult) {
          // 결과가 있으면 결과 페이지로 이동 (경로는 실제 앱에 맞게 수정)
          navigate(`/image-prediction/report/${imagePredictionResult.id}`);
        }
      }, 3000); // 3초 후 리다이렉트

      // 컴포넌트 언마운트 시 타이머 정리
      return () => clearTimeout(redirectTimer);
    }
  }, [isStreaming, imagePredictionResult, navigate]); // 의존성 배열에 필요한 변수들 추가

  return (
    <div className="to-primary-50 from-secondory-lemon-50 flex h-full w-full flex-col items-center justify-center gap-7.5 bg-gradient-to-tl via-white">
      <div className="relative flex w-full items-center justify-center">
        {/* 문서 이미지 */}
        <img
          src="/assets/images/ai-eye-prediction.png"
          alt="amd 예측 로딩"
          className="relative z-10 h-auto w-75"
        />

        {/* 돋보기 이미지 */}
        <img
          src="/assets/images/reading-glasses.png"
          alt="돋보기"
          className="absolute right-0 bottom-0 z-10 h-auto w-32 -translate-x-2/3 -translate-y-1/4 [animation:var(--animation-spin-around)]"
        />
      </div>
      <div className="flex flex-col items-center">
        <span className="text-line-900 text-display-sm font-semibold">
          {profile?.name}님의 안구 이미지를 바탕으로
        </span>
        <div className="mt-3 flex">
          <strong className="text-display-md text-secondory-blue-500 font-semibold">
            AI 정밀진단
          </strong>
          <span className="text-display-md text-line-900 font-semibold">를 진행하고 있어요</span>
        </div>
      </div>
    </div>
  );
};

export default ImagePredictionLoading;
