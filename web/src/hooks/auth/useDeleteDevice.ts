import { authenticateStart, authenticateFinish, deleteCredential } from '@/apis/auth/webauthnApi';
import { base64ToBuffer, bufferToBase64Url } from '@/utils/webauthn';
import { useToast } from '@/components/common/ToastProvider';

export const useDeleteDevice = () => {
  const { showToast } = useToast();

  const handleDeleteDevice = async () => {
    try {
      // 1. 인증 시작
      const startResponse = await authenticateStart();
      const { data: authData, sessionId } = startResponse.data;

      if (!authData || !sessionId) {
        throw new Error('인증 시작 실패: 데이터 누락');
      }

      // 2. WebAuthn 인증
      const publicKeyOptions: PublicKeyCredentialRequestOptions = {
        challenge: base64ToBuffer(authData.challenge),
        timeout: authData.timeout,
        allowCredentials: authData.allowCredentials.map((cred) => ({
          id: base64ToBuffer(cred.id),
          type: 'public-key',
          transports: cred.transports,
        })),
        userVerification: authData.userVerification || 'preferred',
      };

      const assertion = (await navigator.credentials.get({
        publicKey: publicKeyOptions,
      })) as PublicKeyCredential;

      if (!assertion) throw new Error('WebAuthn 인증 실패');

      const assertionResponse = assertion.response as AuthenticatorAssertionResponse;

      const credential = {
        id: assertion.id,
        rawId: bufferToBase64Url(assertion.rawId),
        type: assertion.type,
        response: {
          authenticatorData: bufferToBase64Url(assertionResponse.authenticatorData),
          clientDataJSON: bufferToBase64Url(assertionResponse.clientDataJSON),
          signature: bufferToBase64Url(assertionResponse.signature),
          userHandle: assertionResponse.userHandle
            ? bufferToBase64Url(assertionResponse.userHandle)
            : '',
        },
        clientExtensionResults: assertion.getClientExtensionResults(),
      };

      // 3. 인증 완료
      await authenticateFinish({
        sessionId,
        credential,
        deviceName: navigator.userAgent,
      });

      // 4. 기기 삭제 API 호출
      const deleteResponse = await deleteCredential({ credentialId: assertion.id });

      if (deleteResponse.status === 200) {
        showToast('간편 로그인 정보가 삭제되었습니다.', 'success');
        return true;
      } else {
        throw new Error(deleteResponse.message || '삭제 실패');
      }
    } catch (error) {
      console.error('기기 삭제 에러:', error);
      showToast('간편 로그인 삭제에 실패했습니다.', 'error');
      return false;
    }
  };

  return { handleDeleteDevice };
};
