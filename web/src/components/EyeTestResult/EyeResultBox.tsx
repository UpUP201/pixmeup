import { EyeTest, EyeTestResultTypes, EyeTestTypeLabel } from '@/types/report/EyeTestType';

// 안구 나이
const EyeAgeTestResult = ({
  data,
  type,
}: {
  data: EyeTestResultTypes['안구 나이'];
  type: EyeTest;
}) => (
  <div className="bg-primary-50 flex w-full flex-col justify-between rounded-lg px-3 py-2">
    <div className="w-full">
      <span className="text-line-700 text-text-lg font-semibold">{type}</span>
    </div>
    <div className="text-display-sm mt-9 flex w-full justify-end gap-1">
      <span className="text-primary-600 font-bold">
        {data.age == '-' || data.age < 0 ? '-' : data.age}
      </span>
      <span className="text-line-700 font-bold">{EyeTestTypeLabel['안구 나이'].age}</span>
    </div>
  </div>
);

// 시력
const VisionTestResult = ({ data, type }: { data: EyeTestResultTypes['시력']; type: EyeTest }) => (
  <div className="bg-primary-50 flex w-full flex-col justify-between rounded-lg px-3 py-2">
    <div className="w-full">
      <span className="text-line-700 text-text-lg font-semibold">{type}</span>
    </div>
    <div className="text-display-sm flex w-full justify-end gap-2">
      <span className="text-line-700 font-bold">{EyeTestTypeLabel['시력'].left}</span>
      <span className="text-primary-600 font-bold">
        {data.leftSight == '-' || data.leftSight < 0 ? '-' : data.leftSight / 10}
      </span>
    </div>
    <div className="text-display-sm flex w-full justify-end gap-2">
      <span className="text-line-700 font-bold">{EyeTestTypeLabel['시력'].right}</span>
      <span className="text-primary-600 font-bold">
        {data.rightSight == '-' || data.rightSight < 0 ? '-' : data.rightSight / 10}
      </span>
    </div>
  </div>
);

// 황반 변성
const MacularTestResult = ({
  data,
  type,
}: {
  data: EyeTestResultTypes['황반 변성'];
  type: EyeTest;
}) =>
  data.leftEyeVer && data.rightEyeVer && data.leftEyeHor && data.rightEyeHor ? (
    <div className="bg-primary-50 flex w-full flex-col justify-between gap-3 rounded-lg px-3 py-2">
      <div className="w-full">
        <span className="text-line-700 text-text-lg font-semibold">{type}</span>
      </div>
      <div className="flex w-full justify-end gap-1">
        <div className="text-display-sm flex flex-col justify-between gap-1">
          <div className="flex gap-2">
            <span className="text-line-700 font-bold">
              {EyeTestTypeLabel['황반 변성'].leftEyeVer}
            </span>
            <span className="text-primary-600 font-bold">
              {data.leftEyeVer == '-' || data.leftEyeVer < 0 ? '-' : data.leftEyeVer / 10}
            </span>
          </div>
          <div className="flex gap-2">
            <span className="text-line-700 font-bold">
              {EyeTestTypeLabel['황반 변성'].leftEyeHor}
            </span>
            <span className="text-primary-600 font-bold">
              {data.leftEyeHor == '-' || data.leftEyeHor < 0 ? '-' : data.leftEyeHor / 10}
            </span>
          </div>
        </div>
        <div className="text-display-sm flex flex-col justify-between gap-1">
          <div className="flex gap-2">
            <span className="text-line-700 font-bold">
              {EyeTestTypeLabel['황반 변성'].rightEyeVer}
            </span>
            <span className="text-primary-600 font-bold">
              {data.rightEyeVer == '-' || data.rightEyeVer < 0 ? '-' : data.rightEyeVer / 10}
            </span>
          </div>
          <div className="flex gap-2">
            <span className="text-line-700 font-bold">
              {EyeTestTypeLabel['황반 변성'].rightEyeHor}
            </span>
            <span className="text-primary-600 font-bold">
              {data.rightEyeHor == '-' || data.rightEyeHor < 0 ? '-' : data.rightEyeHor / 10}
            </span>
          </div>
        </div>
      </div>
    </div>
  ) : null;

// 암슬러
const AmslerTestResult = ({ data, type }: { data: EyeTestResultTypes['암슬러']; type: EyeTest }) =>
  data.amslerStatus ? (
    <div className="bg-primary-50 flex w-full flex-col justify-between rounded-lg px-3 py-2">
      <div className="w-full">
        <span className="text-line-700 text-text-lg font-semibold">{type}</span>
      </div>
      <div className="text-display-sm mt-9 flex w-full justify-end gap-1">
        <span className="text-primary-600 font-bold">{data.amslerStatus}</span>
      </div>
    </div>
  ) : null;

const isVisionTest = (type: EyeTest): type is '시력' => type === '시력';
const isEyeAgeTest = (type: EyeTest): type is '안구 나이' => type === '안구 나이';
const isMacularTest = (type: EyeTest): type is '황반 변성' => type === '황반 변성';
const isAmslerTest = (type: EyeTest): type is '암슬러' => type === '암슬러';

interface Props {
  type: EyeTest;
  result?: EyeTestResultTypes[EyeTest];
}

const EyeResultBox = ({ type, result }: Props) => {
  const resultRender = () => {
    if (isVisionTest(type)) {
      return <VisionTestResult data={result as EyeTestResultTypes['시력']} type={type} />;
    }

    if (isEyeAgeTest(type)) {
      return <EyeAgeTestResult data={result as EyeTestResultTypes['안구 나이']} type={type} />;
    }

    if (isMacularTest(type)) {
      return <MacularTestResult data={result as EyeTestResultTypes['황반 변성']} type={type} />;
    }

    if (isAmslerTest(type)) {
      return <AmslerTestResult data={result as EyeTestResultTypes['암슬러']} type={type} />;
    }
  };

  return resultRender();
};

export default EyeResultBox;
