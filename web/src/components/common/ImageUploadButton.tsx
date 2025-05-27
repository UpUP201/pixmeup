import { useEffect, useRef, useState } from 'react';
import Icon from './icons/Icon';
import { Button } from '../ui/button';
import dataURLtoFile from '@/utils/dataURLtoFile';
import { formatDate } from '@/utils';
import Cropper from 'react-cropper';
import 'cropperjs/dist/cropper.css';
import { useUserProfile } from '@/hooks/auth/useUserProfile';

interface Props {
  setPostImg: React.Dispatch<React.SetStateAction<File | null>>;
  capturedImage: string | null;
  setCapturedImage: React.Dispatch<React.SetStateAction<string | null>>;
}

const ImageUploadButton = ({ capturedImage, setCapturedImage, setPostImg }: Props) => {
  const videoRef = useRef<HTMLVideoElement | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [stream, setStream] = useState<MediaStream | null>(null);
  const [isCameraOn, setIsCameraOn] = useState<boolean>(false);
  const [cropError, setCropError] = useState<string>('');
  const [isCropMode, setIsCropMode] = useState<boolean>(false);
  const [originalImage, setOriginalImage] = useState<string | null>(null); // 원본 이미지 저장용 상태
  const { profile } = useUserProfile();

  // 단순화: cropper ref만 사용하고 상태는 제거
  const cropperRef = useRef<any>(null);

  // 컴포넌트 마운트 시 capturedImage가 있다면 originalImage에 복사
  useEffect(() => {
    if (capturedImage && !originalImage) {
      setOriginalImage(capturedImage);
    }
  }, [capturedImage]);

  const startCamera = async () => {
    try {
      const mediaStream = await navigator.mediaDevices.getUserMedia({
        video: {
          width: { ideal: 1600 },
          height: { ideal: 900 },
          facingMode: 'user',
        },
      });

      setStream(mediaStream);
      setIsCameraOn(true);
      setCropError('');

      setTimeout(() => {
        if (videoRef.current) {
          videoRef.current.srcObject = mediaStream;
          console.log('비디오 참조 설정됨:', videoRef.current);
        } else {
          console.error('비디오 요소가 없습니다');
        }
      }, 100);
    } catch (error) {
      console.error('카메라 접근 오류:', error);
    }
  };

  const stopCamera = () => {
    if (stream) {
      stream.getTracks().forEach((track) => track.stop());
      if (videoRef.current) {
        videoRef.current.srcObject = null;
      }
      setStream(null);
      setIsCameraOn(false);
    }
  };

  useEffect(() => {
    return () => {
      stopCamera();
    };
  }, []);

  const captureImage = () => {
    if (videoRef.current && canvasRef.current) {
      const canvas = canvasRef.current;
      const video = videoRef.current;

      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      const ctx = canvas.getContext('2d');
      if (ctx && profile) {
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

        const imageData = canvas.toDataURL('image/jpeg');
        setOriginalImage(imageData); // 원본 이미지 저장
        setCapturedImage(imageData);
        setPostImg(
          dataURLtoFile(imageData, `${profile.phoneNumber}_${formatDate(new Date())}.jpeg`),
        );
        setCropError('');
      } else {
        console.error('캔버스 컨텍스트를 가져올 수 없습니다');
      }
    }
  };

  const resetImage = () => {
    setCapturedImage(null);
    setOriginalImage(null);
    setCropError('');
    setIsCropMode(false);
    startCamera();
  };

  // 크롭 모드 토글 함수
  const toggleCropMode = () => {
    setIsCropMode(!isCropMode);
    setCropError('');
  };

  // 원본 이미지로 복원 함수
  const restoreOriginalImage = () => {
    if (originalImage && profile) {
      setCapturedImage(originalImage);
      setPostImg(
        dataURLtoFile(originalImage, `${profile.phoneNumber}_${formatDate(new Date())}.jpeg`),
      );
      setIsCropMode(false); // 크롭 모드 비활성화
    }
  };

  // 대체 접근법: 크롭된 이미지를 처리하기 위한 대안적 방법
  const getCroppedImage = () => {
    try {
      setCropError('');

      // cropperRef를 직접 사용
      const cropperInstance = cropperRef.current?.cropper;
      if (!cropperInstance) {
        console.error('크로퍼 인스턴스를 찾을 수 없습니다.');
        setCropError('크로퍼를 초기화할 수 없습니다. 페이지를 새로고침 후 다시 시도해 주세요.');
        return;
      }

      // 크롭 데이터 확인 (디버깅용)
      const cropData = cropperInstance.getData();
      console.log('크롭 데이터:', cropData);

      // 대안 방법: 직접 캔버스 생성
      const tempCanvas = document.createElement('canvas');
      const tempCtx = tempCanvas.getContext('2d');

      if (!tempCtx) {
        console.error('캔버스 컨텍스트를 생성할 수 없습니다.');
        setCropError('이미지 처리 중 오류가 발생했습니다.');
        return;
      }

      // 이미지 로드
      const img = new Image();
      img.crossOrigin = 'anonymous';

      img.onload = () => {
        try {
          // 크롭 데이터 가져오기
          const data = cropperInstance.getData();

          // 캔버스 크기 설정
          tempCanvas.width = data.width;
          tempCanvas.height = data.height;

          // 이미지의 크롭된 부분만 그리기
          tempCtx.drawImage(
            img,
            data.x,
            data.y,
            data.width,
            data.height,
            0,
            0,
            data.width,
            data.height,
          );

          // 캔버스를 데이터 URL로 변환
          const croppedImageData = tempCanvas.toDataURL('image/jpeg', 0.9);

          // 파일명 생성
          const fileName = `${profile?.phoneNumber}_${formatDate(new Date())}.jpeg`;

          // 데이터 URL을 File 객체로 변환
          const file = dataURLtoFile(croppedImageData, fileName);

          // 상태 업데이트
          setCapturedImage(croppedImageData);
          setPostImg(file);

          // 카메라 모드 종료 및 크롭 모드 비활성화
          setIsCameraOn(false);
          setIsCropMode(false);
          stopCamera();

          console.log('이미지 크롭 완료 (대체 방법):', fileName);
        } catch (error) {
          console.error('크롭 처리 중 오류:', error);
          setCropError('이미지 크롭 중 오류가 발생했습니다.');
        }
      };

      img.onerror = () => {
        console.error('이미지 로드 오류');
        setCropError('이미지를 로드할 수 없습니다.');
      };

      // 이미지 소스 설정
      if (capturedImage) {
        img.src = capturedImage;
      } else {
        setCropError('처리할 이미지가 없습니다.');
      }
    } catch (error) {
      console.error('크롭 기능 오류:', error);
      setCropError('예상치 못한 오류가 발생했습니다.');
    }
  };

  // 크롭 영역 리셋
  const resetCropArea = () => {
    try {
      const cropperInstance = cropperRef.current?.cropper;
      if (cropperInstance) {
        cropperInstance.reset();
        setCropError('');
      }
    } catch (error) {
      console.error('크롭 영역 초기화 오류:', error);
    }
  };

  // 크롭 취소 - 크롭 모드만 종료
  const cancelCrop = () => {
    setIsCropMode(false);
    setCropError('');
  };

  // 이미지 수정 시작 함수 - 카메라가 꺼진 상태에서 기존 이미지 편집
  const startEditingImage = () => {
    if (capturedImage) {
      // 원본 이미지가 없으면 현재 이미지를 원본으로 저장
      if (!originalImage) {
        setOriginalImage(capturedImage);
      }
      setIsCropMode(true);
    }
  };

  return (
    <div className="w-full rounded-lg">
      {isCameraOn ? (
        <div className="flex w-full flex-col gap-5">
          {!capturedImage ? (
            <>
              <div className="absolute inset-0 z-50 flex items-center justify-center bg-black/60 p-3">
                <div className="relative w-full max-w-md rounded-md bg-white p-3 shadow-lg">
                  <button
                    onClick={() => {
                      stopCamera();
                    }}
                    className="absolute top-3 right-3 text-gray-500 hover:text-black"
                  >
                    <Icon name="close" size={24} />
                  </button>

                  <h2 className="mb-3 text-center text-lg font-semibold">안구 사진 촬영</h2>
                  <video
                    ref={videoRef}
                    autoPlay
                    playsInline
                    className="w-full"
                    style={{ transform: 'scaleX(-1)' }}
                  />
                  <Button variant={'primary'} onClick={captureImage} className="mt-3 w-full py-3">
                    촬영하기
                  </Button>
                </div>
              </div>
            </>
          ) : (
            <>
              <div className="w-full overflow-hidden rounded-md">
                {isCropMode ? (
                  <Cropper
                    ref={cropperRef}
                    initialAspectRatio={1}
                    src={capturedImage}
                    viewMode={1}
                    minCropBoxHeight={10}
                    minCropBoxWidth={10}
                    background={false}
                    responsive={true}
                    autoCropArea={1}
                    checkOrientation={false}
                    guides={true}
                  />
                ) : (
                  <img src={capturedImage} alt="촬영된 사진" className="w-full object-contain" />
                )}
              </div>
              {cropError && <div className="mt-1 mb-1 text-sm text-red-500">{cropError}</div>}
              <div className="flex flex-col gap-2">
                {isCropMode ? (
                  // 크롭 모드일 때 표시할 버튼들
                  <>
                    <div className="flex gap-2">
                      <Button variant={'primary'} onClick={getCroppedImage} className="flex-1 py-3">
                        완료
                      </Button>
                      <Button variant={'outline'} onClick={resetCropArea} className="py-3">
                        초기화
                      </Button>
                    </div>
                    <Button variant={'secondary'} onClick={cancelCrop} className="w-full py-3">
                      취소
                    </Button>
                  </>
                ) : (
                  // 크롭 모드가 아닐 때 표시할 버튼들
                  <>
                    <div className="flex gap-2">
                      <Button variant={'primary'} onClick={toggleCropMode} className="flex-1 py-3">
                        자르기
                      </Button>
                      <Button
                        variant={'secondary'}
                        onClick={() => {
                          stopCamera();
                        }}
                        className="flex-1 py-3"
                      >
                        완료
                      </Button>
                    </div>
                    <div className="flex gap-2">
                      <Button variant={'secondary'} onClick={resetImage} className="flex-1 py-3">
                        다시 촬영하기
                      </Button>
                      {originalImage !== capturedImage && (
                        <Button
                          variant={'outline'}
                          onClick={restoreOriginalImage}
                          className="flex-1 py-3"
                        >
                          복원
                        </Button>
                      )}
                    </div>
                  </>
                )}
              </div>
            </>
          )}
        </div>
      ) : capturedImage == null ? (
        <button
          type="button"
          onClick={startCamera}
          className="bg-primary-50 border-primary-500 flex w-full flex-col items-center gap-3 rounded-lg border pt-20 pb-15"
        >
          <Icon name="plus" size={40} className="fill-primary-500" />
          <span className="text-primary-500 text-text-xl font-medium">안구 사진 촬영하기</span>
        </button>
      ) : (
        // 카메라가 꺼진 상태에서의 이미지 표시
        <div className="flex flex-col gap-2">
          <div className="h-fit w-full rounded-md">
            {isCropMode ? (
              <Cropper
                ref={cropperRef}
                initialAspectRatio={1}
                src={capturedImage}
                viewMode={1}
                minCropBoxHeight={10}
                minCropBoxWidth={10}
                background={false}
                responsive={true}
                autoCropArea={1}
                checkOrientation={false}
                guides={true}
              />
            ) : (
              <img src={capturedImage} alt="찍은 사진" className="w-full object-contain" />
            )}
          </div>
          {cropError && <div className="mt-1 mb-1 text-sm text-red-500">{cropError}</div>}

          {/* 카메라 꺼진 상태에서의 버튼들 */}
          {isCropMode ? (
            // 크롭 모드일 때 (카메라 꺼진 상태)
            <div className="mt-2 flex flex-col gap-2">
              <div className="flex gap-2">
                <Button variant={'primary'} onClick={getCroppedImage} className="flex-1 py-2">
                  완료
                </Button>
                <Button variant={'outline'} onClick={resetCropArea} className="flex-1 py-2">
                  초기화
                </Button>
              </div>
              <Button variant={'secondary'} onClick={cancelCrop} className="w-full py-2">
                취소
              </Button>
            </div>
          ) : (
            // 일반 모드일 때 (카메라 꺼진 상태)
            <div className="mt-2 flex gap-2">
              <Button variant={'outline'} onClick={startEditingImage} className="flex-1 py-2">
                자르기
              </Button>
              <Button variant={'primary'} onClick={startCamera} className="flex-1 py-2">
                다시 촬영하기
              </Button>
              {originalImage !== capturedImage && originalImage && (
                <Button
                  variant={'secondary'}
                  onClick={restoreOriginalImage}
                  className="flex-1 py-2"
                >
                  복원
                </Button>
              )}
            </div>
          )}
        </div>
      )}

      {/* 숨겨진 캔버스 */}
      <canvas ref={canvasRef} className="hidden" />
    </div>
  );
};

export default ImageUploadButton;
