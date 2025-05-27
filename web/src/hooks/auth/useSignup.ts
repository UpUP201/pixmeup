import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { InputChangeEvent, PasswordValidationResult } from '@/types/auth/common';
import { getUserIdFromAccessToken, validatePassword } from '@/utils/auth';
import { useModalStore } from '@/stores';
import { verifyCode, register } from '@/apis/auth/authApi';
import { VerificationCodeError, RegisterError } from '@/apis/auth/types';
import { sendVerificationCode } from '@/apis/auth/authApi';
import { ApiError } from '@/apis/auth/types';
import { useToast } from '@/components/common/ToastProvider';
import {
  registerStart,
  registerFinish,
  authenticateStart,
  authenticateFinish,
} from '@/apis/auth/webauthnApi';
import {
  base64ToBuffer,
  bufferToBase64Url,
  getDeviceName,
  isWebAuthnSupported,
} from '@/utils/webauthn';
import {
  WebAuthnAuthenticateFinishResponse,
  WebAuthnRegisterStartResponse,
} from '@/types/auth/webauthn';
import { ResponseDTO } from '@/types/Api';

export const useSignup = () => {
  const { openModal, closeModal } = useModalStore();

  const navigate = useNavigate();
  const { showToast } = useToast();
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isAllRequiredAgreed, setIsAllRequiredAgreed] = useState(false);

  const [isPhoneValid, setIsPhoneValid] = useState(false);
  const [isPhoneVerified, setIsPhoneVerified] = useState(false);
  const [isVerificationCodeVerified, setIsVerificationCodeVerified] = useState(false);
  const [isPasswordMatch, setIsPasswordMatch] = useState(false);
  const [passwordValidation, setPasswordValidation] = useState<PasswordValidationResult>({
    isValid: false,
    hasEnglish: false,
    hasNumber: false,
    hasSpecial: false,
    isLongEnough: false,
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [agreements, setAgreements] = useState<{
    service: boolean;
    privacy: boolean;
    sensitiveInfo: boolean;
    thirdParty: boolean;
    consignment: boolean;
    marketing: boolean;
  }>({
    service: false,
    privacy: false,
    sensitiveInfo: false,
    thirdParty: false,
    consignment: false,
    marketing: false,
  });

  // userId 상태 추가
  const [userId, setUserId] = useState<number | null>(null);

  const handleNameChange = (e: InputChangeEvent) => {
    setName(e.target.value);
  };

  const handlePhoneChange = (e: InputChangeEvent) => {
    const value = e.target.value.replace(/[^0-9]/g, '');
    if (value.length <= 11) {
      setPhoneNumber(value);
      setIsPhoneValid(value.length === 11 && value.startsWith('010'));
    }
  };

  const handleVerificationCodeChange = (e: InputChangeEvent) => {
    const value = e.target.value.replace(/[^0-9]/g, '');
    setVerificationCode(value);
  };

  const handlePasswordChange = (e: InputChangeEvent) => {
    const newPassword = e.target.value;
    setPassword(newPassword);
    const validation = validatePassword(newPassword);
    setPasswordValidation(validation);
    if (confirmPassword) {
      setIsPasswordMatch(newPassword === confirmPassword);
    }
  };

  const handleConfirmPasswordChange = (e: InputChangeEvent) => {
    const newConfirmPassword = e.target.value;
    setConfirmPassword(newConfirmPassword);
    setIsPasswordMatch(newConfirmPassword === password);
  };

  // 전화번호 인증코드 요청
  const handlePhoneVerification = async () => {
    try {
      setIsLoading(true);
      setError(null);

      const response = await sendVerificationCode(phoneNumber);

      // 성공 응답인 경우
      if (response.data?.success) {
        setIsPhoneVerified(true);
        setError(null);
      } else {
        setError('인증번호 전송에 실패했습니다.');
        setIsPhoneVerified(false);
      }
    } catch (error) {
      const apiError = error as ApiError;
      setError(apiError.message);
      setIsPhoneVerified(false);
    } finally {
      setIsLoading(false);
    }
  };

  // 전화번호 인증코드 확인
  const handleVerificationCodeSubmit = async () => {
    setIsVerificationCodeVerified(true);
    try {
      setIsLoading(true);
      setError(null);

      const response = await verifyCode(phoneNumber, verificationCode);

      if (response.data.success) {
        setIsVerificationCodeVerified(true);
        setError(null);
        // 임시 인증 토큰 저장
        localStorage.setItem('temporaryAuthToken', response.data.temporaryAuthToken);
      } else {
        setError('인증에 실패했습니다.');
        await new Promise((resolve) => setTimeout(resolve, 4000)); // 3초(토스트 표시) + 1초(추가 대기)

        setIsVerificationCodeVerified(false);
      }
    } catch (error) {
      const apiError = error as VerificationCodeError;
      setError(apiError.message);
      setIsVerificationCodeVerified(false);
    } finally {
      setIsLoading(false);
    }
  };

  // - 동의 항목 변경
  const handleAgreementChange = (
    isAllRequiredChecked: boolean,
    newAgreements: {
      service: boolean;
      privacy: boolean;
      sensitiveInfo: boolean;
      thirdParty: boolean;
      consignment: boolean;
      marketing: boolean;
    },
  ) => {
    setIsAllRequiredAgreed(isAllRequiredChecked);
    setAgreements(newAgreements);
  };

  //  - 회원가입
  const handleSignup = async () => {
    try {
      setIsLoading(true);
      const response = await register({
        name,
        phoneNumber,
        password,
        passwordConfirm: confirmPassword,
        agreements,
      });

      if (response.status === 201) {
        // userId를 먼저 설정
        const newUserId = response.data.userId;
        setUserId(newUserId);

        // 모달을 열 때 userId를 직접 전달
        openModal({
          title: '기기에 간편 로그인 등록',
          description:
            '정보 보호를 위해 기기 정보를 등록하여 비밀번호 입력 대신 사용할 수 있습니다.',
          confirmText: '등록',
          denyText: '취소',
          onConfirm: async () => {
            const isSuccess = await handleDeviceRegistration(newUserId); // userId를 직접 전달
            if (isSuccess) {
              closeModal();
              openModal({
                title: '기기 등록 완료',
                description:
                  '기기가 성공적으로 등록되었습니다.\n지금 바로 간편 로그인을 시도해보세요!',
                confirmText: '간편 로그인 시도',
                denyText: '닫기',
                onConfirm: handleDeviceAuthenticate,
                onDeny: () => {
                  closeModal();
                  navigate('/login');
                },
              });
            }
          },
          onDeny: handleDeviceRegistrationCancel,
        });
      }
    } catch (error) {
      const apiError = error as RegisterError;
      showToast(apiError.message, 'error');
      // 토스트가 사라진 후 1초 대기
      await new Promise((resolve) => setTimeout(resolve, 4000));
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeviceRegistrationCancel = () => {
    closeModal();
    navigate('/login');
  };

  const handleDeviceRegistration = async (newUserId?: number): Promise<boolean> => {
    try {
      if (!isWebAuthnSupported()) {
        showToast('이 브라우저는 WebAuthn을 지원하지 않습니다.', 'error');
        return false;
      }

      // 함수 파라미터로 전달받은 userId를 우선 사용
      let targetUserId = newUserId ?? userId;

      if (!targetUserId) {
        targetUserId = getUserIdFromAccessToken();
        if (!targetUserId) {
          showToast('사용자 정보를 찾을 수 없습니다.', 'error');
          return false;
        }
      }

      // 1. 등록 시작
      console.log('WebAuthn 등록 시작 - userId:', targetUserId);
      const registerStartResponse: ResponseDTO<WebAuthnRegisterStartResponse> =
        await registerStart(targetUserId);
      console.log('registerStart 응답 전체:', JSON.stringify(registerStartResponse, null, 2));

      // 응답 구조 검증
      if (!registerStartResponse.data) {
        console.error('registerStart 응답에 data 필드가 없습니다:', registerStartResponse);
        throw new Error('서버 응답이 올바르지 않습니다: data 필드가 없습니다.');
      }

      // ResponseDTO<WebAuthnRegisterStartResponse> 구조에 맞게 데이터 추출
      const responseData = registerStartResponse.data; // WebAuthnRegisterStartResponse
      if (!responseData.data || !responseData.sessionId) {
        console.error('응답 데이터 구조가 올바르지 않습니다:', responseData);
        throw new Error('서버 응답이 올바르지 않습니다: 데이터 구조가 올바르지 않습니다.');
      }

      const webauthnData = responseData.data; // WebAuthnRegisterStartResponse.data
      const sessionId = responseData.sessionId; // WebAuthnRegisterStartResponse.sessionId

      console.log('추출된 데이터:', {
        sessionId,
        webauthnData: {
          challenge: webauthnData.challenge,
          rp: webauthnData.rp,
          user: webauthnData.user,
        },
      });

      // WebAuthn 등록에는 현재 도메인 사용
      const currentRpId = import.meta.env.PROD
        ? 'k12s201.p.ssafy.io' // 프로덕션 도메인
        : window.location.hostname;
      console.log('Using RP ID for WebAuthn:', currentRpId);

      // 2. WebAuthn 등록
      console.log('WebAuthn 등록 시작 - 서버 응답:', webauthnData);

      const publicKeyOptions: PublicKeyCredentialCreationOptions = {
        challenge: base64ToBuffer(webauthnData.challenge),
        rp: {
          id: currentRpId,
          name: webauthnData.rp.name,
        },
        user: {
          id: base64ToBuffer(webauthnData.user.id),
          name: webauthnData.user.name,
          displayName: webauthnData.user.displayName,
        },
        pubKeyCredParams: webauthnData.pubKeyCredParams,
        timeout: webauthnData.timeout,
        excludeCredentials:
          webauthnData.excludeCredentials?.map((cred) => ({
            type: 'public-key' as const,
            id: base64ToBuffer(cred.id),
            ...(cred.transports && { transports: cred.transports }),
          })) || [],
        authenticatorSelection: webauthnData.authenticatorSelection,
        attestation: webauthnData.attestation,
      };

      console.log('WebAuthn 등록 옵션:', {
        ...publicKeyOptions,
        challenge: webauthnData.challenge,
        user: {
          ...publicKeyOptions.user,
          id: webauthnData.user.id,
        },
      });

      // WebAuthn API 호출 직전에 origin 확인
      console.log('WebAuthn 호출 시점 origin:', window.location.origin);

      const credential = (await navigator.credentials.create({
        publicKey: publicKeyOptions,
      })) as PublicKeyCredential;

      if (!credential) {
        throw new Error('등록에 실패했습니다.');
      }

      console.log('WebAuthn 등록 완료 - credential:', {
        type: credential.type,
        id: bufferToBase64Url(credential.rawId),
      });

      const attestationResponse = credential.response as AuthenticatorAttestationResponse;

      console.log('attestationResponse:', {
        attestationObject:
          bufferToBase64Url(attestationResponse.attestationObject).substring(0, 100) + '...',
        clientDataJSON:
          bufferToBase64Url(attestationResponse.clientDataJSON).substring(0, 100) + '...',
      });

      // 3. 등록 완료
      const registerFinishRequest = {
        userId: targetUserId,
        sessionId,
        credential: {
          id: bufferToBase64Url(credential.rawId),
          rawId: bufferToBase64Url(credential.rawId),
          type: credential.type,
          response: {
            attestationObject: bufferToBase64Url(attestationResponse.attestationObject),
            clientDataJSON: bufferToBase64Url(attestationResponse.clientDataJSON),
          },
          clientExtensionResults: credential.getClientExtensionResults(),
        },
        deviceName: getDeviceName(),
      };

      // 디버깅을 위한 상세 로깅 추가
      console.log('registerFinish 요청 상세:', JSON.stringify(registerFinishRequest, null, 2));

      // clientDataJSON 디코딩하여 내용 확인
      try {
        const decodedClientData = JSON.parse(
          atob(registerFinishRequest.credential.response.clientDataJSON),
        );
        console.log('Decoded clientDataJSON:', {
          challenge: decodedClientData.challenge,
          origin: decodedClientData.origin,
          type: decodedClientData.type,
        });
      } catch (e) {
        console.error('clientDataJSON 디코딩 실패:', e);
      }

      const registerFinishResponse = await registerFinish(registerFinishRequest);
      console.log('registerFinish 응답:', registerFinishResponse);

      // closeModal();
      showToast('성공적으로 등록되었습니다.', 'success');

      // // 인증용 새로운 모달
      // openModal({
      //   title: '기기 등록 완료',
      //   description: '기기가 성공적으로 등록되었습니다.\n지금 바로 간편 로그인을 시도해보세요!',
      //   confirmText: '간편 로그인 시도',
      //   denyText: '닫기',
      //   onConfirm: handleDeviceAuthenticate,
      //   onDeny: () => {
      //     closeModal();
      //     navigate('/login');
      //   },
      // });
      return true;
    } catch (error) {
      console.error('WebAuthn error:', error);
      return false;
    }
  };

  const handleDeviceAuthenticate = async () => {
    try {
      // 4. 인증 시작
      const authStartResponse = await authenticateStart();

      if (!authStartResponse?.data?.data) {
        console.error('인증 시작 응답 구조가 올바르지 않습니다:', authStartResponse);
        throw new Error('서버 응답이 올바르지 않습니다: data.data 필드가 없습니다.');
      }

      const authData = authStartResponse.data.data;
      const authSessionId = authStartResponse.data.sessionId;

      if (!authSessionId) {
        console.error('인증 시작 응답에 sessionId가 없습니다:', authStartResponse);
        throw new Error('서버 응답이 올바르지 않습니다: sessionId가 없습니다.');
      }

      console.log('추출된 인증 데이터:', { authData, authSessionId });

      // 5. WebAuthn 인증
      console.log('WebAuthn 인증 시작...');

      try {
        const publicKeyOptions: PublicKeyCredentialRequestOptions = {
          challenge: base64ToBuffer(authData.challenge),
          timeout: authData.timeout,
          allowCredentials: authData.allowCredentials.map(
            (allow: { type: string; id: string; transports?: string[] }) => ({
              type: 'public-key',
              id: base64ToBuffer(allow.id),
              transports: allow.transports as AuthenticatorTransport[],
            }),
          ),
          userVerification: authData.userVerification || 'preferred',
        };

        console.log('publicKey 옵션:', {
          ...publicKeyOptions,
          challenge: authData.challenge,
          allowCredentials: authData.allowCredentials,
        });

        const assertion = (await navigator.credentials.get({
          publicKey: publicKeyOptions,
        })) as PublicKeyCredential;

        const assertionResponse = assertion.response as AuthenticatorAssertionResponse;

        // 6. 인증 완료
        console.log('인증 완료 API 호출 시작...');

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

        console.log('인증 완료 요청 데이터:', {
          sessionId: authSessionId,
          credential: {
            ...credential,
            response: {
              ...credential.response,
              authenticatorData: credential.response.authenticatorData.substring(0, 50) + '...',
              clientDataJSON: credential.response.clientDataJSON.substring(0, 50) + '...',
              signature: credential.response.signature.substring(0, 50) + '...',
            },
          },
          deviceName: navigator.userAgent,
        });

        const authenticateFinishRequest = {
          sessionId: authSessionId,
          credential,
          deviceName: navigator.userAgent,
        };

        const apiResponse = await authenticateFinish(authenticateFinishRequest);
        console.log('인증 완료 API 응답 (전체):', apiResponse);

        const authenticateFinishData: WebAuthnAuthenticateFinishResponse = apiResponse.data;

        // 7. 로그인 처리
        if (!authenticateFinishData.accessToken) {
          console.error(
            '인증 완료 응답 (내부 데이터)에 accessToken이 없습니다:',
            authenticateFinishData,
          );
          throw new Error('인증 완료 응답에 accessToken이 없습니다.');
        }
        const { accessToken } = authenticateFinishData;
        localStorage.setItem('accessToken', accessToken);

        closeModal();
        navigate('/');
        showToast('인증이 완료되었습니다.', 'success');
        // 토스트가 사라진 후 1초 대기
        await new Promise((resolve) => setTimeout(resolve, 4000)); // 3초(토스트 표시) + 1초(추가 대기)
      } catch (error) {
        console.error('WebAuthn 인증 과정에서 에러 발생:', error);
        throw error;
      }
    } catch (error) {
      console.error('WebAuthn error:', error);
      navigate('/landing');
      showToast('인증에 실패했습니다.', 'error');
    }
  };

  return {
    name,
    phoneNumber,
    verificationCode,
    password,
    confirmPassword,
    isPhoneValid,
    isPhoneVerified,
    isVerificationCodeVerified,
    isPasswordMatch,
    passwordValidation,
    isLoading,
    error,
    agreements,
    isAllRequiredAgreed,
    handleNameChange,
    handlePhoneChange,
    handleVerificationCodeChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    handlePhoneVerification,
    handleVerificationCodeSubmit,
    handleSignup,
    handleDeviceRegistrationCancel,
    handleDeviceRegistration,
    handleDeviceAuthenticate,
    handleAgreementChange,
  };
};
