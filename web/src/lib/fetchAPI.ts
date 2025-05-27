import { refreshTokenApi } from '@/apis/auth/refreshTokenApi';
import { ErrorResponseDTO, ResponseDTO } from '@/types/Api';

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

interface FetchAPIParams<
  // T = any,
  P = unknown,
  H extends Record<string, string> = Record<string, string>,
> {
  url: string;
  method: HttpMethod;
  credentials?: RequestCredentials;
  data?: P;
  headers?: H;
}

export const api = (url: string) => `https://k12s201.p.ssafy.io/api/v1${url}`;

export const fetchAPI = async <T, P = unknown>({
  url,
  method,
  credentials = 'same-origin',
  data,
  headers,
  //}: FetchAPIParams<T, P>): Promise<ResponseDTO<T>> => {
}: FetchAPIParams<P>): Promise<ResponseDTO<T>> => {
  const accessToken = localStorage.getItem('accessToken');
  const body = data ? JSON.stringify(data) : null;
  const fullURL = api(url);

  const defaultHeaders: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
    ...headers,
  };

  try {
    const res = await fetch(fullURL, {
      method,
      headers: defaultHeaders,
      credentials,
      body,
    });

    if (!res.ok) {
      const errorData: ErrorResponseDTO = await res.json();

      // 401 Unauthorized: 토큰 재발급 시도
      if (errorData.status === 401 && errorData.name == "INVALID_REFRESH_TOKEN") {

        try {
          const refreshRes = await refreshTokenApi();
          if (refreshRes.data) {
            localStorage.setItem('accessToken', refreshRes.data.accessToken);
            return fetchAPI<T, P>({ url, method, credentials, data, headers });
          }
        } catch {
          localStorage.removeItem('accessToken');
          setTimeout(() => (window.location.href = '/login'), 0);

          throw {
            status: 401,
            name: 'AuthRequiredError',
            message: '토큰이 만료되었습니다. 다시 로그인 해주세요.',
            timestamp: new Date().toISOString(),
          } as ErrorResponseDTO;
        }
      }

      const error = new Error(errorData.message) as Error & {
        status: number;
        name: string;
        timestamp: string;
      };
      
      error.status = errorData.status;
      error.name = errorData.name;
      error.timestamp = errorData.timestamp;
      throw error;
    }

    const responseData: ResponseDTO<T> = await res.json();
    return responseData;
  } catch (error) {
    if (error instanceof Error && 'status' in error) {
      throw error;
    }

    const fallbackError: ErrorResponseDTO = {
      status: 500,
      name: 'NetworkError',
      message: '예기치 못한 오류가 발생했습니다.',
      timestamp: new Date().toISOString(),
    };
    throw fallbackError;
  }
};
