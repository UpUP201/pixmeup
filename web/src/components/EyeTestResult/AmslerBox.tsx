import { Amsler } from '@/types/report/EyeTestType';

interface Props {
  type: 'left' | 'right';
  result: Amsler[];
}

const AmslerResult = ({ type }: { type: Amsler }) => {
  const AMSLER_BG = {
    n: 'bg-white',
    w: 'bg-secondory-lemon-300',
    b: 'bg-secondory-red-300',
    d: 'bg-line-900',
  };

  return <div className={`h-9 w-9 ${AMSLER_BG[type]} border-line-500 box-border border`}></div>;
};

const AmslerBox = ({ type, result }: Props) => {
  return (
    <div className="flex flex-col items-center gap-1">
      <span className="text-text-lg font-semibold">{type === 'left' ? '왼쪽' : '오른쪽'}</span>
      <div className="border-line-100 box-border h-fit w-fit border-4">
        <div className="grid grid-cols-3">
          {result.map((type, index) => (
            <AmslerResult key={index} type={type} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default AmslerBox;
