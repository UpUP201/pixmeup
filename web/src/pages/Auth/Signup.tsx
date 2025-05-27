import { InputField } from '@/components/auth/InputField';
import { Button } from '@/components/ui/button';
import { AgreementSection } from '@/components/auth/AgreementSection';
import { useSignup } from '@/hooks/auth/useSignup';
import { getPasswordFeedback } from '@/utils/auth';

const SignupPage = () => {
  const {
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
    handleNameChange,
    handlePhoneChange,
    handleVerificationCodeChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    handlePhoneVerification,
    handleVerificationCodeSubmit,
    handleSignup,
    handleAgreementChange,
    isAllRequiredAgreed,
    isLoading,
    error,
  } = useSignup();

  return (
    <div className="flex min-h-screen flex-col">
      <div className="container mx-auto flex-1 px-4 py-14">
        <div className="mx-auto max-w-md">
          <div className="mb-8">
            <h1 className="mb-2 text-2xl font-bold text-gray-900">회원가입</h1>
            <p className="text-gray-600">회원가입에 필요한 정보를 입력해주세요</p>
          </div>

          <div className="space-y-4">
            {/* 이름 입력 */}
            <div>
              <InputField
                type="text"
                value={name}
                onChange={handleNameChange}
                placeholder="이름을 입력해주세요"
              />
            </div>

            {/* 휴대폰 번호 입력 */}
            {
              <div>
                <div className="flex gap-2">
                  <InputField
                    type="tel"
                    value={phoneNumber}
                    onChange={handlePhoneChange}
                    placeholder="휴대폰 번호를 입력해주세요"
                    className="flex-1 px-3"
                  />
                  <Button
                    onClick={handlePhoneVerification}
                    disabled={!isPhoneValid || isPhoneVerified || isLoading}
                    variant="primary"
                    size="default"
                  >
                    {isLoading ? '전송 중...' : '인증하기'}
                  </Button>
                </div>
                {phoneNumber && !isPhoneValid && (
                  <p className="mt-1 text-sm text-red-500">
                    010으로 시작하는 11자리 숫자를 입력해주세요
                  </p>
                )}
                {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
              </div>
            }

            {/* 인증번호 입력 */}
            {isPhoneVerified && (
              <div>
                <div className="flex gap-2">
                  <InputField
                    type="text"
                    value={verificationCode}
                    onChange={handleVerificationCodeChange}
                    placeholder="인증번호를 입력해주세요"
                    className="flex-1 px-3"
                  />
                  <Button
                    onClick={handleVerificationCodeSubmit}
                    disabled={!verificationCode || isVerificationCodeVerified}
                    variant="primary"
                    size="default"
                    className="px-6.5"
                  >
                    확인
                  </Button>
                </div>
              </div>
            )}

            {/* 비밀번호 입력 */}
            {isVerificationCodeVerified && (
              <>
                <div>
                  <InputField
                    type="password"
                    value={password}
                    onChange={handlePasswordChange}
                    placeholder="비밀번호를 입력해주세요"
                  />
                </div>
                <div>
                  <InputField
                    type="password"
                    value={confirmPassword}
                    onChange={handleConfirmPasswordChange}
                    placeholder="비밀번호를 다시 입력해주세요"
                  />
                  {password && (
                    <p
                      className={`mt-1 text-sm ${
                        passwordValidation.isValid && (!confirmPassword || isPasswordMatch)
                          ? 'text-green-500'
                          : 'text-red-500'
                      }`}
                    >
                      {getPasswordFeedback(
                        password,
                        confirmPassword,
                        passwordValidation,
                        isPasswordMatch,
                      )}
                    </p>
                  )}
                </div>
              </>
            )}

            {/* 약관 동의 섹션 */}
            {passwordValidation.isValid && isPasswordMatch && (
              <AgreementSection onAgreementChange={handleAgreementChange} />
            )}
          </div>
        </div>
      </div>

      {/* 회원가입 버튼 */}
      {passwordValidation.isValid && isPasswordMatch && (
        <div className="px-4 pb-8">
          <div className="mx-auto max-w-md">
            <Button
              onClick={handleSignup}
              disabled={!isAllRequiredAgreed}
              variant="primary"
              size="lg"
              className="w-full"
            >
              회원가입
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default SignupPage;
