import { useGetMacularReportData } from '@/apis/eyeTest/eyeTestQueries';
import { AmsikTestResult, AmslerChartResult } from '@/components';
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router';

const AMDResult = () => {
  const [searchParams] = useSearchParams();
  const [targetDate, setTargetDate] = useState<string | undefined>(undefined);

  useEffect(() => {
    const date = searchParams.get('targetDate');

    if (date == null) return;

    setTargetDate(date);
  }, [searchParams]);

  const { data: AMDReportData } = useGetMacularReportData({ targetDateTime: targetDate });

  return (
    <>
      <AmslerChartResult data={AMDReportData.data.amsler} />
      <AmsikTestResult data={AMDReportData.data.mchart} />
    </>
  );
};

export default AMDResult;
