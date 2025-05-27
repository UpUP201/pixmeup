import { VoiceWaveIcon } from '@/components';
import { Button } from '@/components/ui/button';
import { useNavigate, useParams } from 'react-router-dom';
import { useState, useRef } from 'react';
import { Icon } from '@/components';

const ExerciseIntro = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isPlaying, setIsPlaying] = useState(true);
  const audioRef = useRef<HTMLAudioElement>(null);

  const handleStart = () => {
    navigate(`/exercise/${id}/step`);
  };

  const handleAudioControl = () => {
    if (audioRef.current) {
      if (isPlaying) {
        audioRef.current.pause();
      } else {
        audioRef.current.play();
      }
      setIsPlaying(!isPlaying);
    }
  };

  return (
    <div className="flex min-h-full flex-col">
      <div className="text-line-900 flex flex-1 flex-col items-center justify-center gap-10 bg-white px-4 text-xl">
        <p className="flex flex-col gap-3 text-center">
          <span className="text-3xl font-bold">눈 운동 가이드</span>
          <span>
            화면의 이미지와 음성을 기반으로 <br />눈 운동을 진행합니다.
          </span>
        </p>
        <div className="flex flex-col items-center gap-5">
          {/* 음성 안내 애니메이션 아이콘 */}
          <VoiceWaveIcon size={60} />
          <audio
            ref={audioRef}
            src="/assets/audios/guide.mp3"
            autoPlay
            onEnded={() => setIsPlaying(false)}
          />
          <Button onClick={handleAudioControl} size="lg" className="cursor-pointer">
            <Icon name={isPlaying ? 'pause-circle-line' : 'play-circle-line'} size={24} />
            <span>{isPlaying ? '일시정지' : '재생하기'}</span>
          </Button>
        </div>
        <p className="text-primary-700 flex flex-col gap-1 text-center">
          <span className="font-bold">소리를 켜 주세요</span>
          <span>운동 방법이 음성으로 안내됩니다</span>
        </p>

        <Button
          variant="primary"
          className="my-10 w-[240px] cursor-pointer rounded-4xl py-4"
          onClick={handleStart}
        >
          시작
        </Button>
      </div>
    </div>
  );
};

export default ExerciseIntro;
