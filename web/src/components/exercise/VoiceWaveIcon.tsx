import { Icon } from '@/components';

interface VoiceWaveIconProps {
  size?: number; // 아이콘 크기 (기본값 60)
}

const VoiceWaveIcon = ({ size = 60 }: VoiceWaveIconProps) => {
  const outerPaddingRatio = 0.8;
  const pingRatio = 1.5;
  const innerPaddingRatio = 0.18;

  // 실제 px값 계산
  const outerPadding = size * outerPaddingRatio;
  const pingSize = size * pingRatio;
  const innerPadding = size * innerPaddingRatio;

  // 전체 컴포넌트 크기
  const totalSize = size + 2 * outerPadding;

  return (
    <div
      className="relative flex items-center justify-center"
      style={{ width: totalSize, height: totalSize }}
    >
      <div
        className="bg-primary-50 relative z-10 flex items-center justify-center rounded-full"
        style={{
          width: totalSize,
          height: totalSize,
          padding: outerPadding,
        }}
      >
        {/* ping 효과가 들어가는 중간 원 */}
        <div
          className="bg-primary-200 absolute z-0 animate-[ping_1.2s_ease-in-out_infinite] rounded-full opacity-40"
          style={{
            width: pingSize,
            height: pingSize,
            top: `calc(50% - ${pingSize / 2}px)`,
            left: `calc(50% - ${pingSize / 2}px)`,
            padding: innerPadding,
          }}
        />
        <div
          className="bg-primary-100 relative z-10 flex items-center justify-center rounded-full"
          style={{
            width: size + 2 * innerPadding,
            height: size + 2 * innerPadding,
            padding: innerPadding,
          }}
        >
          <Icon name="voice" size={size} className="fill-line-800" />
        </div>
      </div>
    </div>
  );
};

export default VoiceWaveIcon;
