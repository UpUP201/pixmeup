import { useGetAIImagePredictionResultQuery } from '@/apis/aiTest/aiTestQueries';
import { EyeExerciseButton, PreciseDiagnosisDetail, PreciseDiagnosisThumbnail } from '@/components';
import { Button } from '@/components/ui/button';
import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router';

const ImagePredictionReport = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const { data: predictionReport } = useGetAIImagePredictionResultQuery({ resultId: id ?? '' });

  useEffect(() => {
    console.log(predictionReport);
  }, [predictionReport]);

  if (!id) return null;

  return (
    <div className="flex h-full flex-col gap-5 px-4 py-5">
      <h2 className="text-display-md mb-2 font-medium">정밀 진단 검사 결과</h2>
      <div className="bg-line-50 mb-9 flex justify-center gap-9 rounded-md p-2">
        <img
          src={predictionReport.data.imageUrl.replace(/&amp;/g, '&')}
          alt="눈"
          className="h-30 w-full object-contain"
        />
      </div>
      <PreciseDiagnosisThumbnail text={predictionReport.data.summary} />
      <PreciseDiagnosisDetail text={predictionReport.data.description} />
      <EyeExerciseButton />
      <Button
        className="text-display-sm text-line-100 mt-auto font-bold"
        onClick={() => {
          navigate('/');
        }}
      >
        홈으로
      </Button>
    </div>
  );
};

export default ImagePredictionReport;
