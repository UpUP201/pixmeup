import { fetchAPI } from '@/lib/fetchAPI';
import { ResponseDTO } from '@/types/Api';

import {
  WebAuthnRegisterStartResponse,
  WebAuthnRegisterFinishRequest,
  WebAuthnRegisterFinishResponse,
  WebAuthnAuthenticateStartResponse,
  WebAuthnAuthenticateFinishRequest,
  WebAuthnAuthenticateFinishResponse,
} from '@/types/auth/webauthn';

export const registerStart = async (
  userId: number,
): Promise<ResponseDTO<WebAuthnRegisterStartResponse>> => {
  try {
    return await fetchAPI<WebAuthnRegisterStartResponse>({
      url: '/webauthn/register/start',
      method: 'POST',
      data: { userId },
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      throw error;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    };
  }
};

export const registerFinish = async (
  data: WebAuthnRegisterFinishRequest,
): Promise<ResponseDTO<WebAuthnRegisterFinishResponse>> => {
  try {
    return await fetchAPI<WebAuthnRegisterFinishResponse>({
      url: '/webauthn/register/finish',
      method: 'POST',
      data,
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      throw error;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    };
  }
};

export const authenticateStart = async (): Promise<
  ResponseDTO<WebAuthnAuthenticateStartResponse>
> => {
  try {
    return await fetchAPI<WebAuthnAuthenticateStartResponse>({
      url: '/webauthn/authenticate/start',
      method: 'POST',
      data: {
        userVerification: 'preferred',
      },
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      throw error;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    };
  }
};

export const authenticateFinish = async (
  data: WebAuthnAuthenticateFinishRequest,
): Promise<ResponseDTO<WebAuthnAuthenticateFinishResponse>> => {
  try {
    return await fetchAPI<WebAuthnAuthenticateFinishResponse>({
      url: '/webauthn/authenticate/finish',
      method: 'POST',
      data,
    });
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      throw error;
    }
    throw {
      name: 'INTERNAL_SERVER_ERROR',
      message: '서버와의 통신 중 오류가 발생했습니다.',
      status: 500,
      timestamp: new Date().toISOString(),
    };
  }
};

export const deleteCredential = (data: { credentialId: string }) =>
  fetchAPI<ResponseDTO<string>>({
    method: 'DELETE',
    url: '/webauthn/credentials',
    data,
  });
