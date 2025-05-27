import { useState, useEffect, Suspense } from 'react';
import { useQRData } from '@/hooks/qr/useQRData';
import { useToast } from '@/components/common/ToastProvider';
import { QRDataRequest } from '@/types/qr';
import QRScannerModal from '@/components/qr/QRScannerModal';
import UserProfile from '@/components/home/UserProfile';
import UserProfileSkeleton from '@/components/home/UserProfileSkeleton';
import QRScannerBtn from '@/components/home/QRScannerBtn';
import EyeResultSection from '@/components/home/EyeResultSection';
import EyeResultSectionSkeleton from '@/components/home/EyeResultSectionSkeleton';
import RecommendExercise from '@/components/home/RecommendExercise';
import RecommendExerciseSkeleton from '@/components/home/RecommendExerciseSkeleton';

const Home = () => {
  const [showQRScanner, setShowQRScanner] = useState(false);
  const { processQRData, checkAndProcessStoredQRData } = useQRData();
  const { showToast } = useToast();

  // 페이지 로드 시 저장된 QR 데이터 확인
  useEffect(() => {
    checkAndProcessStoredQRData();
  }, [checkAndProcessStoredQRData]);

  const handleQRScan = (qrData: QRDataRequest) => {
    try {
      processQRData(qrData);
      setShowQRScanner(false);
    } catch {
      showToast('QR 코드 처리 중 오류가 발생했습니다.', 'error');
    }
  };

  return (
    <div className="h-full pb-16">
      <div className="mb-8 h-full px-5">
        <div className="flex flex-col gap-8">
          <div className="flex flex-col gap-3">
            {/* 사용자 정보 */}
            <Suspense fallback={<UserProfileSkeleton />}>
              <UserProfile />
            </Suspense>

            {/* QR 코드로 검사 결과 불러오기 */}
            <QRScannerBtn onClick={() => setShowQRScanner((prev) => !prev)} />

            {/* QR 스캐너 모달 */}
            {showQRScanner && (
              <div className="rounded-sm">
                <QRScannerModal onScan={handleQRScan} onClose={() => setShowQRScanner(false)} />
              </div>
            )}
          </div>

          {/* 최근 검사 결과 */}
          <Suspense fallback={<EyeResultSectionSkeleton />}>
            <EyeResultSection />
          </Suspense>

          {/* 추천 눈 운동 */}
          <Suspense fallback={<RecommendExerciseSkeleton />}>
            <RecommendExercise />
          </Suspense>
        </div>
      </div>
    </div>
  );
};

export default Home;
