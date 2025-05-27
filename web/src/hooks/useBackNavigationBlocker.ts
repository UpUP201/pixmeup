import { useCallback, useEffect } from 'react';
import { useBlocker } from 'react-router-dom';
import { useModalStore } from '@/stores';

const useBackNavigationBlocker = () => {
  const { openModal, closeModal } = useModalStore();

  const blocker = useBlocker(
    ({ currentLocation, nextLocation }) =>
      currentLocation.pathname !== nextLocation.pathname &&
      !nextLocation.pathname.endsWith('/complete'),
  );

  const showModal = useCallback(() => {
    openModal({
      title: '눈 운동을 그만할까요?',
      description: '중단할 경우 운동 기록이 저장되지 않습니다.',
      confirmText: '확인',
      denyText: '취소',
      onConfirm: () => {
        if (blocker.state === 'blocked' && blocker.proceed) {
          blocker.proceed();
        }
        closeModal();
      },
      onDeny: () => {
        if (blocker.state === 'blocked') {
          blocker.reset();
        }
        closeModal();
      },
    });
  }, [openModal, closeModal, blocker]);

  useEffect(() => {
    if (blocker.state === 'blocked') {
      showModal();
    }
  }, [blocker.state, showModal]);
};

export default useBackNavigationBlocker;
