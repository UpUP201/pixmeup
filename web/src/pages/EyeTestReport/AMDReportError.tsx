import { Icon } from '@/components';
import { useDiagnosisReportStore } from '@/stores';
import { DotLottieReact } from '@lottiefiles/dotlottie-react';
import { useMemo } from 'react';
import { Link } from 'react-router';

const AMDReportError = () => {
  const aredsError = useDiagnosisReportStore((state) => state.aredsError);

  const displayMessage = useMemo(() => {
    if (!aredsError) return '에러';

    return aredsError.replace(/\n/g, '\n');
  }, [aredsError]);

  return (
    <div className="flex h-full flex-col justify-between gap-4 p-5">
      <div className="flex h-full flex-col justify-center gap-4">
        <div className="flex w-full max-w-md justify-center">
          <DotLottieReact
            src="https://lottie.host/d88b9b35-49af-4e97-9944-1f6619cee135/d9EJdZqj1e.lottie"
            autoplay
            className="h-16 w-22"
          />
        </div>
        <span className="text-display-md text-line-800 text-center font-bold">
          {displayMessage}
        </span>
        <span className="text-center text-lg font-semibold">
          이미지 기반으로 하는 진단이 궁금하신가요?
        </span>
        <Link
          to="/image-prediction"
          className="bg-line-900 text-line-50 flex w-full items-center justify-between rounded-lg px-6.5 py-3"
        >
          <span className="text-display-sm font-semibold">정밀 진단하기</span>
          <Icon name="right" />
        </Link>
      </div>
      <div className="flex flex-col gap-3"></div>
    </div>
  );
};

export default AMDReportError;
