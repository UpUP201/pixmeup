import { PasswordValidationResult } from '@/types/auth/common';

// 비밀번호 유효성 검사
export const validatePassword = (password: string): PasswordValidationResult => {
  const hasEnglish = /[a-zA-Z]/.test(password);
  const hasNumber = /[0-9]/.test(password);
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
  const isLongEnough = password.length >= 7;
  
  return {
    isValid: hasEnglish && hasNumber && hasSpecial && isLongEnough,
    hasEnglish,
    hasNumber,
    hasSpecial,
    isLongEnough
  };
};

// 비밀번호 피드백 메시지
export const getPasswordFeedback = (
  password: string,
  confirmPassword: string,
  validationResult: PasswordValidationResult,
  isPasswordMatch: boolean
): string => {
  if (!password) return '';
  
  if (!validationResult.isValid) {
    return '영어, 숫자, 특수문자를 포함한 7자리 이상의 비밀번호를 입력해주세요';
  }
  
  if (confirmPassword && !isPasswordMatch) {
    return '비밀번호가 일치하지 않습니다';
  }
  
  if (confirmPassword && isPasswordMatch) {
    return '사용 가능한 비밀번호입니다';
  }
  
  return '';
};

export const getUserIdFromAccessToken = (): number | null => {
  const token = localStorage.getItem('accessToken');
  if (!token) return null;

  try {
    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    return decodedPayload.userId ?? null;
  } catch (e) {
    console.error('JWT 파싱 실패:', e);
    return null;
  }
}