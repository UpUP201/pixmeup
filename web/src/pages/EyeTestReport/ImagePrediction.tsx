import { useImageAIPredictionMutation } from '@/apis/aiTest/aiTestMutate';
import { useGetS3UploadURLData, useUploadS3ImageMutation } from '@/apis/s3/s3Queries';
import { ImageUploadButton } from '@/components';
import { Button } from '@/components/ui/button';
import { useUserProfile } from '@/hooks/auth/useUserProfile';
import { formatDate } from '@/utils';
import { useEffect, useState } from 'react';

interface FileREaderEventTarget extends EventTarget {
  result: string | ArrayBuffer | null;
}

interface FileReaderEvent extends ProgressEvent {
  target: FileREaderEventTarget | null;
}

const ImagePrediction = () => {
  // key 는 file
  const [postImg, setPostImg] = useState<File | null>(null);
  const [capturedImage, setCapturedImage] = useState<string | null>(null);
  const [s3URL, setS3URL] = useState<string | null>(null);
  const [s3Key, setS3Key] = useState<string | null>(null);
  const { profile } = useUserProfile();

  const uploadFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;

    if (!files) return;

    const selectedFile = files[0];

    setPostImg(selectedFile);

    const fileRead = new FileReader();

    fileRead.onload = (e: FileReaderEvent) => {
      if (e.target && e.target.result) {
        if (typeof e.target.result === 'string') {
          // Base64 문자열 처리
          console.log('이미지 결과:', e.target.result);
          setCapturedImage(e.target.result);
        } else {
          // ArrayBuffer 처리
          const arrayBuffer = e.target.result as ArrayBuffer;
          const blob = new Blob([arrayBuffer], { type: selectedFile.type });
          const objectUrl = URL.createObjectURL(blob);
          console.log('이미지 결과:', objectUrl);
          setCapturedImage(objectUrl);
        }
      }
    };

    fileRead.readAsDataURL(selectedFile);
  };

  useEffect(() => {
    console.log(capturedImage);
    console.log(postImg);
  }, [capturedImage]);

  const { data: S3UploadData } = useGetS3UploadURLData({
    fileName: postImg?.name ?? `${formatDate(new Date())}`,
    contentType: postImg?.type ?? 'image/jpeg',
    enabled: postImg != null,
  });

  useEffect(() => {
    if (!S3UploadData) return;

    setS3URL(S3UploadData.data.presignedUrl);
    setS3Key(S3UploadData.data.s3Key);
  }, [S3UploadData]);

  const s3Mutation = useUploadS3ImageMutation();
  const AIPredictMutation = useImageAIPredictionMutation();

  const handleImageToS3 = () => {
    performAISequentialMutations();
  };

  const performAISequentialMutations = async () => {
    if (postImg == null) return;
    if (s3URL == null) return;
    if (s3Key == null) return;
    if (!profile) return;

    try {
      const formData = new FormData();
      const modifiedFile = new File([postImg], `${profile.phoneNumber}_${formatDate(new Date())}`, {
        type: postImg.type,
      });
      formData.append('file', modifiedFile);

      const s3Upload = await s3Mutation.mutateAsync({
        formData,
        s3Url: s3URL,
        contentType: postImg.type,
      });

      console.log('s3 upload', s3Upload);

      const AIPredict = await AIPredictMutation.mutateAsync({
        s3Key: s3Key,
      });

      console.log('ai predict', AIPredict);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="flex h-full flex-col gap-3 px-5 py-5">
      <div className="mt-11 flex flex-col items-center gap-2">
        <span className="text-display-sm font-medium">정밀 진단을 위해 얼굴 사진이 필요해요</span>
        <span className="text-text-lg">아래와 같이 양쪽 눈과 얼굴이 모두 나오게</span>
        <span className="text-text-lg">사진을 찍어서 첨부해주세요.</span>
      </div>
      <div className="mb-4.5 flex justify-center gap-9">
        <img src="/assets/images/face.png" alt="눈" className="w-full" />
      </div>
      <ImageUploadButton
        setPostImg={setPostImg}
        capturedImage={capturedImage}
        setCapturedImage={setCapturedImage}
      />
      <input
        type="file"
        id="file-upload"
        className="sr-only"
        accept=".png, .jpeg, .jpg"
        onChange={uploadFile}
      />
      <label
        htmlFor="file-upload"
        className="bg-primary-500 text-line-10 text-display-sm hover:bg-primary-600 w-full cursor-pointer rounded-lg py-3 text-center font-bold"
      >
        사진 첨부하기
      </label>
      {/* 버튼 */}
      <div className="text-text-md mt-4.5 flex flex-col items-center">
        <div className="text-line-900 flex gap-1">
          <span>AI 눈 검사는 자가 검사로</span>
          <span className="text-secondory-red-600">의학적 소견이 아니기에</span>
          <span></span>
        </div>
        <span>정확한 진단은 병원을 방문하시길 바랍니다.</span>
        <span>눈의 상태와 주변 환경에 따라 검사 결과가 달라질 수 있습니다.</span>
      </div>
      <Button className="mt-auto cursor-pointer py-4 font-bold" onClick={handleImageToS3}>
        진단받기
      </Button>
    </div>
  );
};

export default ImagePrediction;
