import { ExamRecord } from '@/types/Profile';
import Icon from '../common/icons/Icon';

interface ExamRecordItemProps {
  record: ExamRecord;

  // TODO: 각 검사 항목 별 이동 페이지 경로 적용
  onClick: () => void;
}

export const ExamRecordItem = ({ record, onClick }: ExamRecordItemProps) => {
  const formatDate = (date: Date) => {
    return new Date(date).toLocaleDateString('ko-KR', {
      month: 'long',
      day: 'numeric',
    });
  };

  return (
    <button
      onClick={onClick}
      className="flex w-full items-end justify-between rounded-sm bg-gray-50 p-4 hover:bg-gray-100"
    >
      <div className="flex flex-col items-start gap-1">
        <span className="pb-1 text-sm font-semibold text-gray-600">{formatDate(record.date)}</span>
        <div className="flex flex-wrap gap-1">
          {record.tags.map((tag, index) => (
            <span
              key={index}
              className="bg-primary-100 text-primary-800 rounded-sm px-2 py-1 text-xs"
            >
              {tag}
            </span>
          ))}
        </div>
      </div>
      <Icon name="right" className="h-5 w-5 text-gray-400" />
    </button>
  );
};
