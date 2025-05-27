import { useGetPresbyopiaReportsData } from '@/apis/eyeTest/eyeTestQueries';
import { AITestComment, PresbyopiaChart } from '@/components';
import convertDate from '@/utils/convertDate';
import { useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router';

const PresbyopiaResult = () => {
  const [searchParams] = useSearchParams();
  const [targetDate, setTargetDate] = useState<string | undefined>(undefined);

  useEffect(() => {
    const date = searchParams.get('targetDate');

    if (date == null) return;

    setTargetDate(date);
  }, []);

  const { data: PresbyopiaResultData } = useGetPresbyopiaReportsData({
    targetDateTime: targetDate,
  });

  const { PresbyopiaChartData, aiComment, agePrediction } = useMemo(() => {
    if (!PresbyopiaResultData.data || !PresbyopiaResultData.data.length) {
      return { PresbyopiaChartData: [], agePrediction: undefined };
    }

    const aiComment =
      PresbyopiaResultData.data.length > 1
        ? {
            aiResult: PresbyopiaResultData.data[PresbyopiaResultData.data.length - 2].aiResult,
            status: PresbyopiaResultData.data[PresbyopiaResultData.data.length - 2].status,
          }
        : {
            aiResult: PresbyopiaResultData.data[PresbyopiaResultData.data.length - 1].aiResult,
            status: PresbyopiaResultData.data[PresbyopiaResultData.data.length - 1].status,
          };

    const PresbyopiaChartData = PresbyopiaResultData.data.map((item) => {
      if (item.createdAt.slice(0, 4) === '9999') {
        return {
          date: '예측',
          '안구 나이': item.agePrediction,
        };
      }

      return {
        date: convertDate(item.createdAt),
        '안구 나이': item.age,
      };
    });

    const agePrediction =
      PresbyopiaResultData.data[PresbyopiaResultData.data.length - 1].agePrediction;

    return { PresbyopiaChartData, aiComment, agePrediction };
  }, [PresbyopiaResultData, targetDate]);

  return (
    <>
      <AITestComment
        tag={aiComment?.status ?? 'NORMAL'}
        text={aiComment?.aiResult ?? '진단 결과 없음'}
      />
      {/* 측정 결과 추이 */}
      <PresbyopiaChart
        chartData={PresbyopiaChartData}
        agePrediction={agePrediction}
        targetDate="05-01"
      />
    </>
  );
};

export default PresbyopiaResult;
