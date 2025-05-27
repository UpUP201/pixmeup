import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { withdraw } from '@/apis/auth/authApi';

export const useWithdraw = () => {
  const navigate = useNavigate();

  const { mutate: handleWithdraw, isPending } = useMutation({
    mutationFn: withdraw,
    onSuccess: () => {
      localStorage.clear();
      navigate('/landing');
    },
    onError: (error) => {
      console.error('Withdrawal failed:', error);
      // 토스트 UI 고민필요
    },
  });

  return {
    handleWithdraw,
    isWithdrawing: isPending,
  };
}; 