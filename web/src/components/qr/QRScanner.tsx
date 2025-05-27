import { useEffect, useRef, useState } from 'react';
import jsQR from 'jsqr';
import { QRDataRequest } from '@/types/qr';
import { useToast } from '../common/ToastProvider';

interface QRScannerProps {
  onScan: (data: QRDataRequest) => void;
  onClose: () => void;
}

const QRScanner = ({ onScan, onClose }: QRScannerProps) => {
  const videoRef = useRef<HTMLVideoElement | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [stream, setStream] = useState<MediaStream | null>(null);
  const { showToast } = useToast();

  useEffect(() => {
    startCamera();
    return () => stopCamera();
  }, []);

  const startCamera = async () => {
    try {
      const mediaStream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: { ideal: 'environment' } },
      });
      setStream(mediaStream);
      if (videoRef.current) {
        videoRef.current.srcObject = mediaStream;
        videoRef.current.play();
      }
      scanLoop(); // QR 스캔 시작
    } catch (err) {
      console.error('카메라 접근 오류:', err);
      onClose();
    }
  };

  const stopCamera = () => {
    if (stream) {
      stream.getTracks().forEach((track) => track.stop());
      setStream(null);
    }
  };

  const scanLoop = () => {
    const interval = setInterval(() => {
      if (!videoRef.current || !canvasRef.current) return;

      const canvas = canvasRef.current;
      const context = canvas.getContext('2d');
      const video = videoRef.current;

      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      context?.drawImage(video, 0, 0, canvas.width, canvas.height);

      const imageData = context?.getImageData(0, 0, canvas.width, canvas.height);
      if (!imageData) return;

      const code = jsQR(imageData.data, imageData.width, imageData.height);
      if (code) {
        clearInterval(interval);
        stopCamera();

        if (code.data.startsWith('http')) {
          window.location.href = code.data;
        } else {
          try {
            const qrData = JSON.parse(code.data) as QRDataRequest;
            onScan(qrData);
          } catch (error) {
            console.error('QR 코드 파싱 오류:', error);
            setTimeout(() => {
              showToast('잘못된 QR 코드입니다.', 'error');
              onClose();
            }, 1500);
          }
        }
      }
    }, 500); // 0.5초마다 분석
  };

  return (
    <div className="flex w-full flex-col items-center">
      <video ref={videoRef} autoPlay playsInline className="w-full max-w-md rounded-sm" />
      <canvas ref={canvasRef} className="hidden" />
    </div>
  );
};

export default QRScanner;
