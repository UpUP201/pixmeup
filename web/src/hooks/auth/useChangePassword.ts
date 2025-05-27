import { useState } from 'react';
import { fetchAPI } from '@/lib/fetchAPI';

interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

interface UserProfile {
  name: string;
  daysSinceCheck: number;
  phoneNumber: string;
  gender: string | null;
  age: number | null;
  glasses: boolean | null;
  surgery: boolean | null;
  smoking: boolean | null;
  diabetes: boolean | null;
}

export const useChangePassword = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const changePassword = async (data: ChangePasswordRequest) => {
    setIsLoading(true);
    setError(null);
    
    try {
      const response = await fetchAPI<UserProfile>({
        url: '/users/password',
        method: 'PATCH',
        data,
      });
      return response;
    } catch (err) {
      const errorMessage = err instanceof Error 
        ? err.message 
        : '비밀번호 변경에 실패했습니다.';
      setError(errorMessage);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    changePassword,
    isLoading,
    error,
  };
}; 