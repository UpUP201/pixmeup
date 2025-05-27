import { useQuery, useSuspenseQuery } from '@tanstack/react-query';
import {
  getMacularReportsAPI,
  getMyopiaReportsAPI,
  getPresbyopiaReportsAPI,
  getUserResultReportAPI,
  getUserTotalInfo,
  getVisionReportsAPI,
} from './eyeTestApi';

export const useGetMyopiaReportsData = ({ targetDateTime }: { targetDateTime?: string }) => {
  return useSuspenseQuery({
    queryKey: ['myopia-report', targetDateTime],
    queryFn: () => getMyopiaReportsAPI({ targetDateTime }),
    staleTime: 60 * 1000 * 30,
    retry: (failureCount, error: any) => {
      // 404 에러인 경우 재시도하지 않음
      if (error.status === 404) return false;
      // 다른 에러는 최대 3번까지 재시도
      return failureCount < 3;
    }
  });
};

export const useGetPresbyopiaReportsData = ({ targetDateTime }: { targetDateTime?: string }) =>
  useSuspenseQuery({
    queryKey: ['presbyopia-chart', targetDateTime],
    queryFn: () => getPresbyopiaReportsAPI({ targetDateTime }),
    staleTime: 60 * 1000 * 30,
    retry: 3
  });

export const useGetUserResultReportData = () =>
  useSuspenseQuery({
    queryKey: ['user-result-report'],
    queryFn: getUserResultReportAPI,
    staleTime: 60 * 1000 * 30,
    retry: 3
  });

export const useGetVisionReportData = ({ selectedDateTime }: { selectedDateTime?: string }) =>
  useSuspenseQuery({
    queryKey: ['vision-report', selectedDateTime?.split("T")[0]],
    queryFn: () => getVisionReportsAPI({ selectedDateTime }),
    staleTime: 60 * 1000 * 30,
    retry: 3
  });


export const useGetMacularReportData = ({ targetDateTime }: { targetDateTime?: string }) =>
  useSuspenseQuery({
    queryKey: ['macular-report', targetDateTime],
    queryFn: () => getMacularReportsAPI({ targetDateTime }),
    staleTime: 60 * 1000 * 30,
    retry: 3
  });


export const useGetUserTotalInfoData = () => useQuery({
  queryKey: ["user-total-info"],
  queryFn: getUserTotalInfo,
})