import { useUserProfile } from '@/hooks/auth/useUserProfile';
import { formatDate } from '@/utils';
import { useMemo, useState } from 'react';
import { useSearchParams } from 'react-router';

const TestUserInfo = () => {
  const [searchParams] = useSearchParams();
  const { profile } = useUserProfile();
  const [hasParam, setHasParam] = useState<boolean>(true);

  const { year, month, day } = useMemo(() => {
    let targetDate = searchParams.get('targetDate');

    if (targetDate == null) {
      setHasParam(false);
      targetDate = formatDate(new Date());
    }

    const splitDate = targetDate.split('T')[0].split('-');

    return { year: splitDate[0], month: splitDate[1], day: splitDate[2] };
  }, [searchParams]);

  return (
    <div className="mb-3 flex flex-col gap-2">
      <span className="text-display-lg text-line-900 font-semibold">{`${profile?.name}님`}</span>
      <p className="text-text-xl text-line-700 font-medium">
        <strong className="text-primary-600">{year}</strong>년{' '}
        <strong className="text-primary-600">{month}</strong>월{' '}
        <strong className="text-primary-600">{day}</strong>
        {hasParam ? '일의 검사 결과' : '일 기준 가장 최근 검사 결과'}
      </p>
    </div>
  );
};

export default TestUserInfo;
