import { Skeleton } from '@/components/ui/skeleton';
import Icon from '@/components/common/icons/Icon';

const ExerciseRecommendSkeleton = () => {
  return (
    <div className="from-primary-100/60 to-secondory-lemon-100 bg-gradient-to-br px-5 py-8">
      {/* AI 아이콘과 사용자 이름 */}
      <div className="mb-2 flex items-center gap-2 text-lg font-semibold">
        <Icon name="ai" className="fill-primary-500" size={20} />
        <div className="flex items-center gap-1">
          <Skeleton className="bg-line-700/10 h-6 w-30" />
        </div>
      </div>

      {/* 눈 이미지와 추천 운동명 */}
      <div className="mb-2 flex items-center gap-2 py-3">
        <img src="/assets/images/eye-open.png" alt="eye-open" className="w-24" />
        <div className="flex flex-col gap-1">
          <div className="flex items-center gap-1">
            <Skeleton className="bg-line-700/10 h-8 w-36" />
          </div>
          <Skeleton className="bg-line-700/10 h-8 w-24" />
        </div>
      </div>

      {/* 시작하기 버튼 */}
      <Skeleton className="bg-line-900/90 h-10 w-full rounded-lg" />
    </div>
  );
};

export default ExerciseRecommendSkeleton;
