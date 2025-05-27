import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { logout } from '@/apis/auth/authApi';

export const useLogout = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const handleLogout = async () => {
    try {
      await logout();
      localStorage.removeItem('accessToken');

      // 전역 상태 초기화
      queryClient.setQueryData(['user'], null);
      queryClient.clear();

      navigate('/landing');
    } catch (error) {
      console.error('Logout failed:', error);
      navigate('/landing');
    }
  };

  return {
    handleLogout,
  };
};
