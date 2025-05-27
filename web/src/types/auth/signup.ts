import { AuthResponse, PasswordValidationResult } from './common';

export interface SignupRequest {
  name: string;
  phoneNumber: string;
  password: string;
}

export interface SignupResponse extends AuthResponse {
  data?: {
    userId: string;
  };
}

export interface SignupState {
  name: string;
  phoneNumber: string;
  verificationCode: string;
  password: string;
  confirmPassword: string;
  isPhoneValid: boolean;
  isPhoneVerified: boolean;
  isVerificationCodeVerified: boolean;
  isPasswordMatch: boolean;
  passwordValidation: PasswordValidationResult;
  isBottomSheetOpen: boolean;
  isModalOpen: boolean;
}

export interface VerificationRequest {
  phoneNumber: string;
  code: string;
}

export interface VerificationResponse extends AuthResponse {
  data?: {
    isVerified: boolean;
  };
} 