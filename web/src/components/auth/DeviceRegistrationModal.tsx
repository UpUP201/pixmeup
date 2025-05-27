import { Button } from '@/components/ui/button';

interface DeviceRegistrationModalProps {
  isOpen: boolean;
  onClose: () => void;
  onRegister: () => void;
}

export const DeviceRegistrationModal = ({
  isOpen,
  onClose,
  onRegister,
}: DeviceRegistrationModalProps) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="mx-4 w-full max-w-md rounded-lg bg-white p-8">
        <div className="space-y-4 text-center">
          <h2 className="text-xl font-bold">기기에 간편 로그인 등록</h2>
          <p className="text-sm text-gray-500">
            정보 보호를 위해 기기 정보를 등록하여 비밀번호 입력 대신 사용할 수 있습니다.
          </p>
        </div>

        <div className="mt-6 flex gap-3">
          <Button onClick={onClose} variant="outline" size="lg" className="flex-1">
            취소
          </Button>
          <Button onClick={onRegister} variant="primary" size="lg" className="flex-1">
            등록
          </Button>
        </div>
      </div>
    </div>
  );
};
