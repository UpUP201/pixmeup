import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { InputChangeEvent } from '@/types/auth/common';
import { login } from '@/apis/auth/authApi';
import { LoginError } from '@/types/auth/login';
import { useToast } from '@/components/common/ToastProvider';

export const useLogin = () => {
  const navigate = useNavigate();
  const { showToast } = useToast();
  const queryClient = useQueryClient();
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const [isPhoneValid, setIsPhoneValid] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handlePhoneChange = (e: InputChangeEvent) => {
    const value = e.target.value.replace(/[^0-9]/g, '');
    if (value.length <= 11) {
      setPhoneNumber(value);
      setIsPhoneValid(value.length === 11 && value.startsWith('010'));
    }
  };

  const handlePasswordChange = (e: InputChangeEvent) => {
    setPassword(e.target.value);
  };

  const handleLogin = async () => {
    try {
      setIsLoading(true);
      const response = await login({ phoneNumber, password });

      if (response.status === 200) {
        // 로그인 성공
        localStorage.setItem('accessToken', response.data.accessToken);

        // 유저 정보 전역 상태 업데이트
        queryClient.setQueryData(['user'], {
          userId: response.data.userId,
          name: response.data.name,
        });

        navigate('/');
      } else if(response.status == 401){

        showToast(response.message, "error");

        setPassword("");

      }
    } catch (error) {
      const apiError = error as LoginError;
      showToast(apiError.message, 'error');
      // 토스트가 사라진 후 1초 대기
      await new Promise((resolve) => setTimeout(resolve, 4000));
    } finally {
      setIsLoading(false);
    }
  };

  return {
    phoneNumber,
    password,
    isPhoneValid,
    isLoading,
    handlePhoneChange,
    handlePasswordChange,
    handleLogin,
  };
};
