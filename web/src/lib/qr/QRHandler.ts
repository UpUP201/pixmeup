export function handleQRData() {
  const params = new URLSearchParams(window.location.search);
  const qrData = params.get('qr');
  // qr데이터가 있는 경우 로컬에 저장
  if (qrData) {
    const decodedQRData = decodeURIComponent(qrData);
    localStorage.setItem('qr_data', decodedQRData);
  }
}
