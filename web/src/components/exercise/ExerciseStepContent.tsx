import { ProgressBar, StepArrowButton, VoiceWaveIcon } from '@/components';
import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useGetExerciseStart } from '@/apis/exercise/exerciseQueries';
import BasicModal from '@/components/common/BasicModal';
import useBackNavigationBlocker from '@/hooks/useBackNavigationBlocker';

const ExerciseStepContent = () => {
  const [currentStep, setCurrentStep] = useState(0);
  const navigate = useNavigate();
  const { id } = useParams();
  const {
    data: { data: exerciseData },
  } = useGetExerciseStart(id!);
  useBackNavigationBlocker();

  // 이전, 다음 스텝으로 이동
  const goPrev = () => {
    if (currentStep === 0) {
      return;
    }
    setCurrentStep((prev) => Math.max(prev - 1, 0));
  };
  const goNext = () => {
    if (currentStep === exerciseData.eyeExerciseStepList.length - 1) {
      navigate(`/exercise/${id}/complete`);
    } else {
      setCurrentStep((prev) => Math.min(prev + 1, exerciseData.eyeExerciseStepList.length - 1));
    }
  };

  const currentStepData = exerciseData.eyeExerciseStepList[currentStep];

  return (
    <div className="flex h-full flex-col">
      <BasicModal />
      <div className="mb-16 flex flex-1 flex-col justify-between p-5">
        <ProgressBar steps={exerciseData.eyeExerciseStepList} currentStep={currentStep} />
        <div className="flex w-full flex-1 flex-col items-center justify-between gap-5 py-8">
          <div className="flex w-full justify-end">
            <VoiceWaveIcon size={32} />
          </div>
          <div className="flex flex-col items-center gap-6">
            <img
              src={currentStepData.stepImageUrl.replace(/&amp;/g, '&')}
              alt={currentStepData.title}
              className="h-50 w-50 object-contain"
            />
            <p
              className="text-line-900 text-center text-xl font-bold"
              style={{ whiteSpace: 'pre-line' }}
            >
              {currentStepData.instruction}
            </p>
            <audio src={currentStepData.stepTtsUrl.replace(/&amp;/g, '&')} autoPlay />
          </div>
          <div className="flex w-full items-center justify-between">
            <div className={currentStep === 0 ? 'invisible' : ''}>
              <StepArrowButton direction="prev" onClick={goPrev} label="이전 스텝" />
            </div>
            <StepArrowButton direction="next" onClick={goNext} label="다음 스텝" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExerciseStepContent;
