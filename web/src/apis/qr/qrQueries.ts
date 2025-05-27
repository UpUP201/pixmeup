import { useMutation } from '@tanstack/react-query';
import { postQRData } from './qrApi';

// QR 데이터 전송
export const usePostQRData = () => {
  return useMutation({
    mutationFn: postQRData,
    onSuccess: () => {
      console.log('QR 데이터 저장 성공');
      localStorage.removeItem('qr_data');
    },
    onError: (error) => {
      console.error('QR 데이터 저장 실패:', error);
    },
  });
};
