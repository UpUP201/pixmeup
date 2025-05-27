import { QRDataRequest } from '@/types/qr';
import Icon from '../common/icons/Icon';
import QRScanner from './QRScanner';

interface QRScannerModalProps {
  onScan: (data: QRDataRequest) => void;
  onClose: () => void;
}

const QRScannerModal = ({ onScan, onClose }: QRScannerModalProps) => {
  return (
    <div className="absolute inset-0 z-50 flex items-center justify-center bg-black/60 p-3">
      <div className="relative w-full max-w-md rounded-md bg-white p-3 shadow-lg">
        <button onClick={onClose} className="absolute top-3 right-3 text-gray-500 hover:text-black">
          <Icon name="close" size={24} />
        </button>

        <h2 className="mb-3 text-center text-lg font-semibold">QR 스캔</h2>
        <QRScanner onScan={onScan} onClose={onClose} />
      </div>
    </div>
  );
};

export default QRScannerModal;
