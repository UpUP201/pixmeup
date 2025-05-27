import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import { useIntersection } from '@/hooks/useIntersection';
import { getReportTotalList } from '@/apis/eyeTest/eyeTestApi';
import { ExamRecordList } from '@/components/profile/ExamRecordList';
import { ExamRecordGroup, EyeExamTag, ExamRecord, EyeExamRecord } from '@/types/Profile';
import { formatDate } from '@/utils';
import { useNavigate } from 'react-router';

const PAGE_SIZE = 10;

const EyeHistoryPage = () => {
  const navigate = useNavigate();

  const { data, fetchNextPage, hasNextPage } = useSuspenseInfiniteQuery({
    queryKey: ['reportTotalList'],
    queryFn: ({ pageParam }) =>
      getReportTotalList(pageParam.page, PAGE_SIZE).then((res) => res.data),
    initialPageParam: { page: 0 },
    getNextPageParam: (lastPage) => {
      if (lastPage.last) return undefined;
      return { page: lastPage.number + 1 };
    },
  });

  // 모든 페이지의 content 합치기
  const allRecords = data.pages.flatMap((page) => page.content);

  // 날짜 데이터를 ExamRecord 형식으로 변환
  const examRecords: ExamRecord[] = allRecords.map((report) => {
    const tags: EyeExamTag[] = [];
    if (report.hasSight) tags.push('근거리 시력');
    if (report.hasPresbyopia) tags.push('노안 조절력');
    if (report.hasAmsler) tags.push('암슬러 차트');
    if (report.hasMChart) tags.push('엠식 변형시');

    return {
      id: report.dateTime,
      date: new Date(report.dateTime),
      type: 'eye',
      tags,
    } as EyeExamRecord;
  });

  // 월별 그룹화
  const groupedRecords = examRecords.reduce((groups: ExamRecordGroup[], record) => {
    const month = new Date(record.date)
      .toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
      })
      .replace('년', '년 ');

    const existingGroup = groups.find((group) => group.month === month);
    if (existingGroup) {
      existingGroup.records.push(record);
    } else {
      groups.push({ month, records: [record] });
    }
    return groups;
  }, []);

  groupedRecords.sort((a, b) => b.month.localeCompare(a.month));
  groupedRecords.forEach((group) => {
    group.records.sort((a, b) => b.date.getTime() - a.date.getTime());
  });

  // 무한 스크롤 마지막 요소 감지
  const lastItemRef = useIntersection({ threshold: 0.1, rootMargin: '0px' }, () => {
    if (hasNextPage) fetchNextPage();
  });

  // 마지막 ref를 가장 마지막 그룹의 마지막 record에 부착
  const lastGroup = groupedRecords[groupedRecords.length - 1];
  const lastRecordId = lastGroup?.records[lastGroup.records.length - 1]?.id;

  const handleRecordClick = (record: ExamRecord) => {
    const targetDate = formatDate(record.date);

    navigate(`/report?targetDate=${targetDate}`);
  };

  return (
    <div className="h-full px-5 pb-16">
      {groupedRecords.length > 0 ? (
        <ExamRecordList
          records={groupedRecords}
          onRecordClick={handleRecordClick}
          lastItemId={lastRecordId}
          lastItemRef={lastItemRef}
        />
      ) : (
        <div className="pt-10 text-center text-gray-400">검사 기록이 없습니다.</div>
      )}
    </div>
  );
};

export default EyeHistoryPage;
