import { useGetDiagnosisResultQuery } from '@/apis/aiTest/aiTestQueries';
import { AMDResultBox, Icon } from '@/components';
import ErrorPage from '@/components/common/Error';
import { useEffect } from 'react';
import { Link, useParams } from 'react-router';

const AMDReport = () => {
  const { id } = useParams();
  const { data: diagnosisData } = useGetDiagnosisResultQuery({ resultId: id ?? '' });

  useEffect(() => {
    console.log(diagnosisData);
  }, [diagnosisData]);

  if (!id) return <ErrorPage />;

  const RISK_MAPPING = {
    Medium: '보통입니다.',
    Low: '낮습니다',
    High: '높습니다.',
  };

  return (
    <div className="flex flex-col gap-4 p-5">
      <div className="mb-1 flex flex-col">
        <span className="text-display-sm text-line-900 font-semibold">{`${diagnosisData.data.user_name}님의 AI 예측 결과`}</span>
        <span className="text-display-lg text-line-800 font-bold">
          <span className="text-secondory-blue-600">AMD 질병 리스크</span>가
        </span>
        <span className="text-display-lg text-line-800 font-bold">
          {RISK_MAPPING[diagnosisData.data.risk]}
        </span>
      </div>
      <AMDResultBox score={diagnosisData.data.risk_percent} text={diagnosisData.data.summary} />
      <div className="mt-6 flex flex-col gap-3">
        <span className="text-lg font-semibold">더 자세한 결과가 궁금하신가요?</span>
        <Link
          to="/image-prediction"
          className="bg-line-900 text-line-50 flex w-full items-center justify-between rounded-lg px-6.5 py-3"
        >
          <span className="text-display-sm font-semibold">정밀 진단하기</span>
          <Icon name="right" />
        </Link>
      </div>
    </div>
  );
};

export default AMDReport;
