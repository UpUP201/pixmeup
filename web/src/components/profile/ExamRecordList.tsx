import { ExamRecord, ExamRecordGroup } from '@/types/Profile';
import { ExamRecordItem } from './ExamRecordItem';

interface ExamRecordListProps {
  records: ExamRecordGroup[];
  onRecordClick: (record: ExamRecord) => void;
  lastItemId?: string;
  lastItemRef?: (node: HTMLElement | null) => void;
}

export const ExamRecordList = ({
  records,
  onRecordClick,
  lastItemId,
  lastItemRef,
}: ExamRecordListProps) => {
  return (
    <div className="space-y-6">
      {records.map((group) => (
        <div key={group.month} className="space-y-1">
          <h3 className="py-2 text-sm font-semibold text-gray-900">{group.month}</h3>
          <div className="space-y-2">
            {group.records.map((record, index) => (
              <div
                key={`${record.id}-${record.type}-${record.date.getTime()}-${index}`}
                className="bg-primary-50 rounded-sm"
                ref={record.id === lastItemId ? lastItemRef : undefined}
              >
                <ExamRecordItem record={record} onClick={() => onRecordClick(record)} />
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};
