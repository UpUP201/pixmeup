import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import { useIntersection } from '@/hooks/useIntersection';
import { ExamRecordList } from '@/components/profile/ExamRecordList';
import { ExamRecordGroup, ExamRecord, AIExamRecord } from '@/types/Profile';
import { fetchAPI } from '@/lib/fetchAPI';
import { useNavigate } from 'react-router';

const PAGE_SIZE = 10;

// interface
interface AIDiagnosisItem {
  id: string;
  type: 'IMAGE' | 'AREDS';
  created_at: string;
}

interface AIDiagnosisResponse {
  status: number;
  message: string;
  data: {
    content: AIDiagnosisItem[];
  };
  timestamp: string;
}

// api
const getAIDiagnosisList = async (page: number = 0, size: number = PAGE_SIZE) => {
  const response = await fetchAPI<AIDiagnosisResponse['data']>({
    method: 'GET',
    url: `/total-diagnosis?page=${page}&size=${size}`,
  });
  return response.data;
};

const AIHistoryPage = () => {
  const navigate = useNavigate();
  const { data, fetchNextPage, hasNextPage } = useSuspenseInfiniteQuery({
    queryKey: ['aiDiagnosisList'],
    queryFn: ({ pageParam }) => getAIDiagnosisList(pageParam.page, PAGE_SIZE),
    initialPageParam: { page: 0 },
    getNextPageParam: (lastPage, _, lastPageParam) => {
      const contentLength = lastPage?.content?.length ?? 0;
      const isLastPage = contentLength < PAGE_SIZE;
      return isLastPage ? undefined : { page: lastPageParam.page + 1 };
    },
  });

  // 모든 페이지 데이터 합치기
  const allRecords = data.pages.flatMap((page) => page?.content || []);

  // ExamRecord로 변환
  const examRecords: ExamRecord[] = allRecords.map((item) => {
    const tags: string[] = [];
    let type = '';
    if (item.type === 'IMAGE') {
      tags.push('AI 정밀 진단');
      type = 'img';
    }
    if (item.type === 'AREDS') {
      tags.push('AMD 검사');
      type = 'ai';
    }

    return {
      id: item.id,
      date: new Date(item.created_at),
      type,
      tags,
    } as AIExamRecord;
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

  // 정렬
  groupedRecords.sort((a, b) => b.month.localeCompare(a.month));
  groupedRecords.forEach((group) => {
    group.records.sort((a, b) => b.date.getTime() - a.date.getTime());
  });

  // 마지막 요소 감지용 ref
  const lastItemRef = useIntersection({ threshold: 0.1, rootMargin: '0px' }, () => {
    if (hasNextPage) fetchNextPage();
  });

  const lastGroup = groupedRecords[groupedRecords.length - 1];
  const lastRecordId = lastGroup?.records[lastGroup.records.length - 1]?.id;

  const handleRecordClick = (record: ExamRecord) => {
    if (record.type == 'ai') {
      navigate(`/amd-report/${record.id}`);
    } else {
      navigate(`/image-prediction/report/${record.id}`);
    }
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
        <div className="text-line-400 pt-10 text-center">검사 기록이 없습니다.</div>
      )}
    </div>
  );
};

export default AIHistoryPage;
