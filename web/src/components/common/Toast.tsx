import { useEffect, useState, useRef } from 'react';
import Icon from '@/components/common/icons/Icon';

interface ToastProps {
  message: string;
  type?: 'success' | 'error' | 'info';
  duration?: number;
  onClose: () => void;
}

const Toast = ({ message, type = 'success', duration = 3000, onClose }: ToastProps) => {
  const [isVisible, setIsVisible] = useState(true);
  const [touchStart, setTouchStart] = useState<number | null>(null);
  const [touchEnd, setTouchEnd] = useState<number | null>(null);
  const toastRef = useRef<HTMLDivElement>(null);

  // 최소 스와이프 거리 (픽셀)
  const minSwipeDistance = 50;

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      // 애니메이션 완료 후 onClose 호출
      const closeTimer = setTimeout(onClose, 300);
      return () => clearTimeout(closeTimer);
    }, duration);

    return () => clearTimeout(timer);
  }, [duration, onClose]);

  const onTouchStart = (e: React.TouchEvent) => {
    setTouchEnd(null);
    setTouchStart(e.targetTouches[0].clientY);
  };

  const onTouchMove = (e: React.TouchEvent) => {
    setTouchEnd(e.targetTouches[0].clientY);
  };

  const onTouchEnd = () => {
    if (!touchStart || !touchEnd) return;

    const distance = touchStart - touchEnd;
    const isSwipeUp = distance > minSwipeDistance;

    if (isSwipeUp) {
      setIsVisible(false);
      setTimeout(onClose, 300);
    }
  };

  const textColor = {
    success: 'text-primary-900',
    error: 'text-secondory-red-900',
    info: 'text-secondory-blue-800',
  }[type];

  const iconColor = {
    success: 'fill-primary-500',
    error: 'fill-secondory-red-700',
    info: 'fill-secondory-blue-500',
  }[type];

  return (
    <div
      ref={toastRef}
      className={`ring-line-500/5 w-full rounded-lg bg-white px-5 py-5 shadow-md ring-1 transition-all duration-300 ${
        isVisible ? 'translate-y-0 opacity-100' : '-translate-y-full opacity-0'
      } [animation:var(--animation-${isVisible ? 'slide-in' : 'slide-out'})]`}
      style={{
        touchAction: 'pan-y',
      }}
      onTouchStart={onTouchStart}
      onTouchMove={onTouchMove}
      onTouchEnd={onTouchEnd}
    >
      <div className="flex items-center gap-2">
        <Icon name="check-circle-fill" className={`${iconColor} flex-shrink-0`} />
        <span className={`${textColor} break-words`}>{message}</span>
      </div>
    </div>
  );
};

export default Toast;
