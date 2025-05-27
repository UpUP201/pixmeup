import { useRef } from 'react';

export const useIntersection = (
  options = { threshold: 0.1, rootMargin: '0px' },
  callback: () => void,
) => {
  const observerRef = useRef<IntersectionObserver | null>(null);

  function elementRef(node: HTMLElement | null) {
    if (observerRef.current) {
      observerRef.current.disconnect();
    }

    if (node) {
      observerRef.current = new IntersectionObserver(([entry]) => {
        if (entry.isIntersecting) {
          callback();
        }
      }, options);

      observerRef.current.observe(node);
    }
  }

  return elementRef;
};
