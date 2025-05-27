import { useGetUserResultReportData } from '@/apis/eyeTest/eyeTestQueries';
import { EyeResultBoxSet } from '@/components';
import { Link } from 'react-router';

const EyeResultSection = () => {
  const { data: resultData } = useGetUserResultReportData();

  return (
    <div>
      <p className="pb-3 text-xl font-semibold">
        최근 <span className="text-primary-600">검사 결과</span>
      </p>

      {/* 최근 검사 결과 */}
      <EyeResultBoxSet data={resultData.data} />

      {/* AI 눈 건강 예측 배너 */}
      <Link
        to="/amd-report/loading"
        replace={true}
        className="to-primary-50 from-secondory-lemon-50 mt-3 flex w-full justify-between rounded-sm bg-gradient-to-r px-5 py-3 shadow-md"
      >
        <div className="flex flex-col justify-center">
          <p className="pb-2 text-xl font-semibold">AI 눈 건강 예측</p>
          <p className="text-sm text-gray-700">검사 결과를 바탕으로</p>
          <p className="text-sm text-gray-700">AI가 눈 건강 정보를 알려줘요!</p>
        </div>
        <div>
          <img src="assets/images/ai-eye-home.png" className="w-22" alt="AI 눈 건강 예측" />
        </div>
      </Link>
    </div>
  );
};

export default EyeResultSection;
