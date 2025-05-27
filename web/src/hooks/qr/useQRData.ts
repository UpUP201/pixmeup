import { usePostQRData } from '@/apis/qr/qrQueries';
import { QRDataRequest } from '@/types/qr';
import { useToast } from '@/components/common/ToastProvider';
import { useState } from 'react';

export const useQRData = () => {
  const { mutate: postQRDataMutate } = usePostQRData();
  const { showToast } = useToast();
  const [isProcessing, setIsProcessing] = useState(false);

  const processQRData = (qrData: QRDataRequest) => {
    // QR 데이터를 localStorage에 저장
    localStorage.setItem('qr_data', JSON.stringify(qrData));
  };

  const checkAndProcessStoredQRData = () => {
    const storedQRData = localStorage.getItem('qr_data');

    if (storedQRData && !isProcessing) {
      // 저장된 QR 데이터가 있고, 현재 처리 중이 아닌 경우에만 처리
      setIsProcessing(true);
      const qrData = JSON.parse(storedQRData);

      postQRDataMutate(qrData, {
        onSuccess: () => {
          console.log('QR 코드가 성공적으로 처리되었습니다.');
          showToast('QR 코드가 성공적으로 처리되었습니다.', 'success');
          clearQRData(); // 성공 시에만 데이터 삭제
        },
        onError: () => {
          console.log('QR 코드 처리 중 오류가 발생했습니다.');
          showToast('QR 코드 처리 중 오류가 발생했습니다.', 'error');
        },
        onSettled: () => {
          setIsProcessing(false); // 성공/실패 상관없이 처리 상태 해제
        },
      });
    }
  };

  const clearQRData = () => {
    localStorage.removeItem('qr_data');
  };

  return {
    processQRData,
    clearQRData,
    checkAndProcessStoredQRData,
  };
};
