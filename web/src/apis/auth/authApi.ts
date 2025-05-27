import {
  SendVerificationResponse,
  ErrorResponse,
  ApiError,
  VerificationCodeResponse,
  VerificationCodeError,
  RegisterRequest,
  RegisterResponse,
  RegisterError,
} from './types';
import { LoginRequest, LoginResponse, LoginError } from '@/types/auth/login';
import { fetchAPI } from '@/lib/fetchAPI';
import { ResponseDTO, ErrorResponseDTO } from '@/types/Api';

const API_BASE_URL = 'https://k12s201.p.ssafy.io/api/v1';

// 인증번호 전송
export const sendVerificationCode = async (
  phoneNumber: string,
): Promise<SendVerificationResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/send-code`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer public',
      },
      body: JSON.stringify({ phoneNumber }),
    });

    const data = await response.json();

    if (response.status === 200 || response.status === 201) {
      return data as SendVerificationResponse;
    }

    const errorData = data as ErrorResponse;
    throw {
      name: errorData.name as ApiError['name'],
      message: errorData.message,
      status: errorData.status,
    } as ApiError;
  } catch (error) {
    if (error instanceof Error) {
      throw {
        name: 'INTERNAL_SERVER_ERROR',
        message: '서버와의 통신 중 오류가 발생했습니다.',
        status: 500,
      } as ApiError;
    }
    throw error;
  }
};

// 인증번호 확인
export const verifyCode = async (
  phoneNumber: string,
  verificationCode: string,
): Promise<ResponseDTO<VerificationCodeResponse>> => {
  try {
    return await fetchAPI<VerificationCodeResponse>({
      url: '/auth/verify-code',
      method: 'POST',
      data: {
        phoneNumber,
        verificationCode,
      },
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      const apiError = error as VerificationCodeError;
      throw {
        name: apiError.name,
        message: apiError.message,
        status: apiError.status,
        timestamp: apiError.timestamp,
      } as VerificationCodeError;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    } as VerificationCodeError;
  }
};

// 회원가입
export const register = async (data: RegisterRequest): Promise<ResponseDTO<RegisterResponse>> => {
  try {
    return await fetchAPI<RegisterResponse>({
      url: '/auth/register',
      method: 'POST',
      data,
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      const apiError = error as RegisterError;
      throw {
        name: apiError.name,
        message: apiError.message,
        status: apiError.status,
        timestamp: apiError.timestamp,
      } as RegisterError;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    } as RegisterError;
  }
};

// 로그인
export const login = async (data: LoginRequest): Promise<ResponseDTO<LoginResponse>> => {
  try {
    return await fetchAPI<LoginResponse>({
      url: '/auth/login',
      method: 'POST',
      data,
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      const apiError = error as LoginError;
      throw {
        name: apiError.name,
        message: apiError.message,
        status: apiError.status,
        timestamp: apiError.timestamp,
      } as LoginError;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    } as LoginError;
  }
};

// 로그아웃
export const logout = async (): Promise<ResponseDTO<null>> => {
  try {
    return await fetchAPI<null>({
      url: '/auth/logout',
      method: 'POST',
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      const apiError = error as ErrorResponseDTO;
      throw {
        name: apiError.name,
        message: apiError.message,
        status: apiError.status,
        timestamp: apiError.timestamp,
      } as ErrorResponseDTO;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    } as ErrorResponseDTO;
  }
};

// 회원탈퇴
export const withdraw = async (): Promise<ResponseDTO<null>> => {
  try {
    return await fetchAPI<null>({
      url: '/users/',
      method: 'DELETE',
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      const apiError = error as ErrorResponseDTO;
      throw {
        name: apiError.name,
        message: apiError.message,
        status: apiError.status,
        timestamp: apiError.timestamp,
      } as ErrorResponseDTO;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    } as ErrorResponseDTO;
  }
};

export interface UserProfile {
  name: string;
  daysSinceCheck: number;
  phoneNumber: string;
  gender: string | null;
  age: string | null;
  glasses: boolean | null;
  surgery: string | null;
  smoking: boolean | null;
  diabetes: boolean | null;
}

export const getUserProfile = async (): Promise<ResponseDTO<UserProfile>> => {
  try {
    return await fetchAPI<UserProfile>({
      url: '/users/profile',
      method: 'GET',
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      const apiError = error as ErrorResponseDTO;
      throw {
        name: apiError.name,
        message: apiError.message,
        status: apiError.status,
        timestamp: apiError.timestamp,
      } as ErrorResponseDTO;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    } as ErrorResponseDTO;
  }
};
