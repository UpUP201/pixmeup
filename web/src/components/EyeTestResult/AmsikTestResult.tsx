import { McharReport } from '@/types/report/EyeTestType';
import Icon from '../common/icons/Icon';
import AmsikTable from './AmsikTable';
import { useMemo } from 'react';

interface Props {
  data: McharReport | null;
}

const AmsikTestResult = ({ data }: Props) => {
  const { MchartData, aiResult } = useMemo(() => {
    if (data == null) {
      const MchartData = {
        leftVer: 0,
        rightVer: 0,
        leftHor: 0,
        rightHor: 0,
      };

      const aiResult = '이상 없음';

      return {
        MchartData,
        aiResult,
      };
    }

    const MchartData = {
      leftVer: data.leftEyeVer,
      rightVer: data.rightEyeVer,
      leftHor: data.leftEyeHor,
      rightHor: data.rightEyeHor,
    };

    const aiResult = data.aiResult;

    return {
      MchartData,
      aiResult,
    };
  }, [data]);

  return (
    <div className="flex flex-col gap-3">
      <div className="flex items-center gap-2">
        <Icon name="dry" className="fill-secondory-blue-500" />
        <span className="text-line-900 text-display-sm font-semibold">엠식 변형시 검사 결과</span>
      </div>
      <div className="relative flex flex-col gap-4 rounded-xl bg-white px-4 py-4.5">
        {data == null ? (
          <div className="flex flex-col gap-2">
            <span className="text-line-500 text-text-xl text-center font-semibold">
              검사 결과가 없습니다.
            </span>
            <span className="text-line-500 text-text-md text-center font-semibold">
              <strong className="text-primary-600 font-semibold">내눈 키오스크</strong>를 통해서
              검사를 받아주세요.
            </span>
          </div>
        ) : (
          <>
            <AmsikTable
              test={{
                vertical: { left: MchartData.leftVer, right: MchartData.rightVer },
                horizontaol: { left: MchartData.leftHor, right: MchartData.rightHor },
              }}
            />
            <div className="bg-line-50 flex w-full justify-center rounded-md px-3 py-3">
              <span className="text-text-lg text-line-700 font-medium">{aiResult}</span>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default AmsikTestResult;
