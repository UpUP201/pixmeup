import { BasicModal } from '@/components/common';
import { useAIPredictionStore } from '@/stores';
import { useEffect, useRef } from 'react';
import { Outlet, useLocation } from 'react-router';
import { ToastProvider } from '@/components/common/ToastProvider';

interface Props {
  headerSlot?: React.ReactNode;
  navbarSlot?: React.ReactNode;
}

// 기본 베이스 레이아웃

const Layout = ({ headerSlot, navbarSlot }: Props) => {
  // pathname 변경 시 scroll을 위로 보내기
  const { pathname } = useLocation();
  const mainRef = useRef<HTMLDivElement>(null);

  // 개별적으로 값을 구독 (객체 대신)
  const streamData = useAIPredictionStore((state) => state.streamData);
  const progress = useAIPredictionStore((state) => state.progress);
  const isStreaming = useAIPredictionStore((state) => state.isStreaming);
  const error = useAIPredictionStore((state) => state.error);

  useEffect(() => {
    if (mainRef.current) {
      mainRef.current.scrollTop = 0;
    }
  }, [pathname]);

  // streamData 변경 감지하여 콘솔에 출력
  useEffect(() => {
    if (streamData.length > 0) {
      console.log('SSE 스트림 데이터:', streamData);
      // 가장 최근에 받은 데이터 항목
      console.log('최근 수신 데이터:', streamData[streamData.length - 1]);
    }
  }, [streamData]);

  // 진행 상황 변경 감지하여 콘솔에 출력
  useEffect(() => {
    if (isStreaming) {
      console.log('SSE 진행 상황:', progress + '%');
    }
  }, [progress, isStreaming]);

  // 에러 감지하여 콘솔에 출력
  useEffect(() => {
    if (error) {
      console.error('SSE 에러 발생:', error);
    }
  }, [error]);

  // 스트리밍 상태 변경 감지
  useEffect(() => {
    console.log('SSE 스트리밍 상태:', isStreaming ? '연결됨' : '연결 안됨');
  }, [isStreaming]);

  return (
    <div className="h-dvh w-dvw bg-gray-100">
      <div className="relative mx-auto flex h-full max-w-[393px] min-w-[350px] flex-col overflow-hidden bg-white">
        <ToastProvider>
          {headerSlot}
          <main className="h-full flex-grow overflow-auto" ref={mainRef}>
            <Outlet />
          </main>
          {navbarSlot}
          <BasicModal />
        </ToastProvider>
      </div>
    </div>
  );
};

export default Layout;
