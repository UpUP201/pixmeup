import { api } from '@/lib/fetchAPI';
import { ResponseDTO } from '@/types/Api';

export const refreshTokenApi = async (): Promise<ResponseDTO<{accessToken: string}>> => {
  const response = await fetch(api('/auth/refresh-token'), {
    method: 'POST',
    credentials: 'include',
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`토큰 재발급 실패: ${errorText}`);
  }

  const responseData: ResponseDTO<{accessToken: string}> = await response.json();
  
  return responseData;
};
