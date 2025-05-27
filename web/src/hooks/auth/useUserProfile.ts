import { useQuery } from '@tanstack/react-query';
import { getUserProfile, UserProfile } from '@/apis/auth/authApi';
import { useToast } from '@/components/common/ToastProvider';

export const useUserProfile = () => {
  const { showToast } = useToast();
  const accessToken = localStorage.getItem('accessToken');

  const {
    data: profile,
    isLoading,
    error,
    refetch,
  } = useQuery<UserProfile>({
    queryKey: ['userProfile'],
    queryFn: async () => {
      const response = await getUserProfile();
      return response.data;
    },
    enabled: !!accessToken,
    staleTime: 1000 * 60 * 5, // 5분 동안 데이터를 신선한 상태로 유지
    retry: 1, // 실패 시 1번만 재시도
  });

  // 에러 발생 시 토스트 메시지 표시
  if (error) {
    showToast('사용자 정보를 불러오는데 실패했습니다.', 'error');
  }

  // 문진표 작성 여부 확인
  const hasMedicalQuestionnaire = profile?.daysSinceCheck !== -1;

  return {
    profile,
    isLoading,
    error,
    refetch,
    hasMedicalQuestionnaire,
  };
}; 