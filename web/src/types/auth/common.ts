import { ChangeEvent } from 'react';

export type InputChangeEvent = ChangeEvent<HTMLInputElement>;

export interface PasswordValidationResult {
  isValid: boolean;
  hasEnglish: boolean;
  hasNumber: boolean;
  hasSpecial: boolean;
  isLongEnough: boolean;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  data?: any;
} 