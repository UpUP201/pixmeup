// 00분 00초 형식으로 변환
export const formatDurationToKorean = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;

  return `${minutes.toString().padStart(2, '0')}분 ${remainingSeconds.toString().padStart(2, '0')}초`;
};

export const formatTimelineStepTime = (stepDuration: number): string => {
  const minutes = Math.floor(stepDuration / 60);
  const seconds = stepDuration % 60;

  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}~`;
};
