import { Button } from '@/components/ui/button';
import { useLogout } from '@/hooks/auth/useLogout';
import { useWithdraw } from '@/hooks/auth/useWithdraw';
import { useSignup } from '@/hooks/auth/useSignup.ts';
import { useModalStore } from '@/stores';
import { useDeleteDevice } from '@/hooks/auth/useDeleteDevice';

const ProfileActions = () => {
  const { openModal, closeModal } = useModalStore();
  const { handleLogout } = useLogout();
  const { handleWithdraw, isWithdrawing } = useWithdraw();
  const { handleDeviceRegistration } = useSignup();
  const { handleDeleteDevice } = useDeleteDevice();

  const handleWithdrawClick = () => {
    openModal({
      icon: 'warning',
      title: '회원 탈퇴',
      description: '정말로 탈퇴하시겠습니까? 탈퇴 시 모든 데이터가 삭제되며 복구할 수 없습니다.',
      confirmText: '탈퇴하기',
      denyText: '취소',
      onConfirm: (e) => {
        e.stopPropagation();
        handleWithdraw();
        closeModal();
      },
      onDeny: (e) => {
        e.stopPropagation();
        closeModal();
      },
    });
  };

  return (
    <div className="space-y-2">
      <Button
        variant="primary"
        className="w-full cursor-pointer"
        size="lg"
        onClick={() => handleDeviceRegistration()}
      >
        간편 로그인 등록
      </Button>

      <Button
        variant="secondary_red"
        className="w-full cursor-pointer"
        size="lg"
        onClick={handleLogout}
      >
        로그아웃
      </Button>

      <div className="m-3 flex">
        <button
          onClick={handleWithdrawClick}
          className="border-line-100 text-line-400 w-full flex-1 cursor-pointer border-r-1 text-sm"
          disabled={isWithdrawing}
        >
          {isWithdrawing ? '처리중...' : '회원 탈퇴'}
        </button>

        <button
          onClick={handleDeleteDevice}
          className="text-line-400 w-full flex-1 cursor-pointer text-sm"
        >
          간편 등록 삭제
        </button>
      </div>
    </div>
  );
};

export default ProfileActions;
