import { InputField } from '@/components/auth/InputField';
import { Button } from '@/components/ui/button';
import { useLogin } from '@/hooks/auth/useLogin';

const LoginPage = () => {
  const {
    phoneNumber,
    password,
    isPhoneValid,
    handlePhoneChange,
    handlePasswordChange,
    handleLogin,
  } = useLogin();

  return (
    <div className="flex min-h-screen flex-col">
      <div className="container mx-auto flex-1 px-4 py-14">
        <div className="mx-auto max-w-md">
          <div className="mb-8">
            <h1 className="mb-2 text-2xl font-bold text-gray-900">사용자 로그인</h1>
            <p className="text-gray-600">휴대폰 번호와 비밀번호를 입력해주세요</p>
          </div>

          <div className="space-y-4">
            <div>
              <InputField
                type="tel"
                value={phoneNumber}
                onChange={handlePhoneChange}
                placeholder="휴대폰 번호를 입력해주세요"
              />
              {phoneNumber && !isPhoneValid && (
                <p className="mt-1 text-sm text-red-500">
                  010으로 시작하는 11자리 숫자를 입력해주세요
                </p>
              )}
            </div>

            <div>
              <InputField
                type="password"
                value={password}
                onChange={handlePasswordChange}
                placeholder="비밀번호를 입력해주세요"
              />
            </div>
          </div>
        </div>
      </div>

      <div className="px-4 pb-8">
        <div className="mx-auto max-w-md">
          <Button
            onClick={handleLogin}
            disabled={!isPhoneValid || !password}
            variant="primary"
            size="lg"
            className="w-full"
          >
            로그인
          </Button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
