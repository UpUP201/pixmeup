import { EyeTestResults } from '@/types/report/EyeTestType';
import EyeResultBox from './EyeResultBox';

interface Props {
  data: EyeTestResults;
}

const EyeResultBoxSet = ({ data }: Props) => {
  return (
    <div className="mb-3 flex w-full flex-col gap-2.5">
      <div className="flex w-full gap-2.5">
        <EyeResultBox type={'안구 나이'} result={{ age: data.age }} />
        <EyeResultBox
          type={'시력'}
          result={{ leftSight: data.leftSight, rightSight: data.rightSight }}
        />
      </div>
      <div className="flex w-full gap-2.5">
        <EyeResultBox type={'암슬러'} result={{ amslerStatus: data.amslerStatus }} />
        <EyeResultBox
          type={'황반 변성'}
          result={{
            leftEyeHor: data.leftEyeHor,
            leftEyeVer: data.leftEyeVer,
            rightEyeHor: data.rightEyeHor,
            rightEyeVer: data.rightEyeVer,
          }}
        />
      </div>
    </div>
  );
};

export default EyeResultBoxSet;
