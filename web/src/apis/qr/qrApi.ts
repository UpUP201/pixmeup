import { fetchAPI } from '@/lib/fetchAPI';
import { QRDataRequest } from '@/types/qr';

// QR 데이터 전송 API
export const postQRData = (qrData: QRDataRequest) =>
  fetchAPI<QRDataRequest>({
    method: 'POST',
    url: '/reports/qr',
    data: qrData,
  });
