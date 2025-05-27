interface ExerciseTimelineItemProps {
  time: string;
  title: string;
  description: string;
  isLast?: boolean;
}

const ExerciseTimelineItem = ({
  time,
  title,
  description,
  isLast = false,
}: ExerciseTimelineItemProps) => (
  <div className="mb-2 flex items-start gap-4">
    {/* 타임라인 점 */}
    <div className="flex h-full min-w-[16px] flex-col items-center">
      {/* 초록색 원 */}
      <div className="bg-primary-500 my-2 h-2 w-2 rounded-full" />
      {/* 아래 회색 점 */}
      {!isLast && (
        <div className="flex flex-1 flex-col">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="bg-line-200 my-2 h-1 w-1 rounded-full" />
          ))}
        </div>
      )}
    </div>
    {/* 내용 */}
    <div className="flex flex-col gap-2">
      <div className="flex items-center gap-3">
        <div className="text-primary-500 border-primary-500 rounded-sm border bg-green-50 px-2 py-1 text-xs">
          {time}
        </div>
        <div className="text-lg font-bold">{title}</div>
      </div>
      <div className="text-line-600">{description}</div>
    </div>
  </div>
);

export default ExerciseTimelineItem;
