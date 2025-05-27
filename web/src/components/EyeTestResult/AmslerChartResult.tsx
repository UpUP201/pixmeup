import { Amsler, AmslerReport } from '@/types/report/EyeTestType';
import AmslerBox from './AmslerBox';
import Icon from '../common/icons/Icon';
import { useMemo } from 'react';

// interface Props {
//   left: Amsler[];
//   right: Amsler[];
//   result: string;
// }

interface Props {
  data: AmslerReport | null;
}

const AmslerChartResult = ({ data }: Props) => {
  const { leftMacular, rightMacular, aiResult } = useMemo(() => {
    if (data == null) {
      return {
        leftMacular: ['n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'] as Amsler[],
        rightMacular: ['n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'] as Amsler[],
        aiResult: '이상 없음',
      };
    }

    return {
      leftMacular: data.leftMacularLoc.split(',') as Amsler[],
      rightMacular: data.rightMacularLoc.split(',') as Amsler[],
      aiResult: data.aiResult,
    };
  }, [data]);
  return (
    <div className="flex flex-col gap-3">
      <div className="flex items-center gap-2">
        <Icon name="ai" className="fill-secondory-blue-500" />
        <span className="text-line-900 text-display-sm font-semibold">암슬러 차트 검사 결과</span>
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
            <div className="flex items-end justify-between">
              <AmslerBox result={leftMacular} type="left" />
              <div className="bg-line-200 h-28 w-0.5"></div>
              <AmslerBox result={rightMacular} type="right" />
            </div>
            <div className="bg-line-50 flex w-full justify-center rounded-md px-3 py-3">
              <span className="text-text-lg text-line-700 font-medium">{aiResult}</span>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default AmslerChartResult;
