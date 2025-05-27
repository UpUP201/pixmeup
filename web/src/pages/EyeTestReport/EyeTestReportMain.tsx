import { useGetVisionReportData } from '@/apis/eyeTest/eyeTestQueries';
import { AIButton, EyeResultBoxSet, EyeTestResultTab, TestUserInfo } from '@/components';
import { formatDate } from '@/utils';
import { Outlet, useSearchParams } from 'react-router';

const EyeTestReportMain = () => {
  const currentDate = new Date();
  const [searchParams] = useSearchParams();

  const { data: EyeResultData } = useGetVisionReportData({
    selectedDateTime: searchParams.get('targetDate') ?? formatDate(currentDate),
  });

  return (
    <div className="flex h-full flex-col">
      <div className="flex flex-col px-3">
        <TestUserInfo />
        <EyeResultBoxSet data={EyeResultData.data} />
        <AIButton />
        <EyeTestResultTab />
      </div>
      <div className="bg-line-10 flex flex-1 flex-col gap-5 px-3 py-5">
        <Outlet />
      </div>
    </div>
  );
};

export default EyeTestReportMain;
