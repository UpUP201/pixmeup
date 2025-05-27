export interface LoginRequest {
  phoneNumber: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  name: string;
  accessToken: string;
}

export type LoginError = {
  name: 'INVALID_INPUT_VALUE' | 'LOGIN_BAD_CREDENTIALS' | 'INTERNAL_SERVER_ERROR';
  message: string;
  status: number;
  timestamp: string;
};

export interface LoginState {
  phoneNumber: string;
  password: string;
  isPhoneValid: boolean;
} 