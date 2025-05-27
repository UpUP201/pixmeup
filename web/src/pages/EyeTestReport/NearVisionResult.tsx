import { useGetMyopiaReportsData } from '@/apis/eyeTest/eyeTestQueries';
import { AITestComment } from '@/components';
import NearVisionChart from '@/components/EyeTestResult/NearVisionChart';
import { formatDate } from '@/utils';
import convertDate from '@/utils/convertDate';
import { useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router';

const NearVisionResult = () => {
  // 쿼리 파람으로 넘어오는 날짜 값 잡기
  const [searchParams] = useSearchParams();
  const [targetDate, setTargetDate] = useState<string | undefined>(undefined);

  useEffect(() => {
    const date = searchParams.get('targetDate');

    if (date == null) return;

    setTargetDate(date);
  }, []);

  // 근거리 시력 데이터 요청
  const { data: nearVisionData } = useGetMyopiaReportsData({
    targetDateTime: targetDate,
  });

  // 데이터 가공
  const { leftEyeData, rightEyeData, currentResult } = useMemo(() => {
    if (!nearVisionData.data || !nearVisionData.data.length) {
      return { leftEyeData: [], rightEyeData: [] };
    }

    const currentResult =
      nearVisionData.data.length > 1
        ? {
            aiResult: nearVisionData.data[nearVisionData.data.length - 2].aiResult,
            status: nearVisionData.data[nearVisionData.data.length - 2].status,
          }
        : {
            aiResult: nearVisionData.data[nearVisionData.data.length - 1].aiResult,
            status: nearVisionData.data[nearVisionData.data.length - 1].status,
          };

    const leftEyeData = nearVisionData.data.map((item) => {
      if (item.createdAt.split('-')[0] === '9999') {
        return {
          시력: item.leftSight / 10,
          date: item.createdAt,
          날짜: '예측',
        };
      }

      return {
        시력: item.leftSight / 10,
        date: item.createdAt,
        날짜: convertDate(item.createdAt),
      };
    });

    const rightEyeData = nearVisionData.data.map((item) => {
      if (item.createdAt.split('-')[0] === '9999') {
        return {
          시력: item.rightSight / 10,
          date: item.createdAt,
          날짜: '예측',
        };
      }

      return {
        시력: item.rightSight / 10,
        date: item.createdAt,
        날짜: convertDate(item.createdAt),
      };
    });

    return { leftEyeData, rightEyeData, currentResult };
  }, [nearVisionData]);

  return (
    <>
      <AITestComment
        tag={currentResult?.status ?? 'NORMAL'}
        text={currentResult?.aiResult ?? '정보가 없습니다.'}
      />
      {/* 측정 결과 추이 */}
      <NearVisionChart
        type="왼쪽 눈"
        data={leftEyeData}
        targetDate={targetDate ?? formatDate(new Date())}
      />
      <NearVisionChart
        type="오른쪽 눈"
        data={rightEyeData}
        targetDate={targetDate ?? formatDate(new Date())}
      />
    </>
  );
};

export default NearVisionResult;
