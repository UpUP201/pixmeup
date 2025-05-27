import { useState, FormEvent } from 'react';
import { Button } from '@/components/ui/button';
import { InputField } from '@/components/auth/InputField';
import { useChangePassword } from '@/hooks/auth/useChangePassword';
import { InputChangeEvent } from '@/types/auth/common';

interface ChangePasswordModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const ChangePasswordModal = ({ isOpen, onClose }: ChangePasswordModalProps) => {
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState<string | null>(null);

  const { changePassword, isLoading } = useChangePassword();

  if (!isOpen) return null;

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    if (newPassword !== confirmPassword) {
      setError('새 비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      await changePassword({
        currentPassword,
        newPassword,
      });
      onClose();
    } catch (err) {
      console.log(err);
    }
  };

  const handleInputChange = (setter: (value: string) => void) => (e: InputChangeEvent) => {
    setter(e.target.value);
  };

  return (
    <div className="bg-opacity-50 fixed inset-0 z-50 flex items-center justify-center bg-black">
      <div className="w-[90%] max-w-md rounded-lg bg-white p-6">
        <h3 className="mb-4 text-lg font-semibold">비밀번호 변경</h3>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="mb-1 block text-sm font-medium">현재 비밀번호</label>
            <InputField
              type="password"
              value={currentPassword}
              onChange={handleInputChange(setCurrentPassword)}
              placeholder="현재 비밀번호를 입력해주세요"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium">새 비밀번호</label>
            <InputField
              type="password"
              value={newPassword}
              onChange={handleInputChange(setNewPassword)}
              placeholder="새 비밀번호를 입력해주세요"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium">새 비밀번호 확인</label>
            <InputField
              type="password"
              value={confirmPassword}
              onChange={handleInputChange(setConfirmPassword)}
              placeholder="새 비밀번호를 다시 입력해주세요"
            />
          </div>

          {error && <p className="text-sm text-red-500">{error}</p>}

          <div className="flex justify-center gap-2 px-12">
            <Button variant="outline" size="lg" className="flex-1/2" onClick={onClose}>
              취소
            </Button>
            <Button variant="primary" size="lg" className="flex-1/2" disabled={isLoading}>
              {isLoading ? '처리중...' : '변경하기'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};
