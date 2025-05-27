import Icon from '@/components/common/icons/Icon';

interface QRScannerBtnProps {
  onClick: () => void;
}

const QRScannerBtn = ({ onClick }: QRScannerBtnProps) => {
  return (
    <div
      onClick={onClick}
      className="bg-primary-100 flex h-14 w-full cursor-pointer items-center justify-between gap-2 rounded-sm px-3"
    >
      <div className="flex items-center justify-start gap-2">
        <div className="bg-primary-50 flex h-8 w-8 items-center justify-center rounded-sm">
          <Icon name="qrcode"></Icon>
        </div>
        <p className="font-semibold">QR 코드로 검사 결과 불러오기</p>
      </div>
      <Icon name="right"></Icon>
    </div>
  );
};

export default QRScannerBtn;
