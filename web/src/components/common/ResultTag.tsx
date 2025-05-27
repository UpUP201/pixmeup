import Icon from './icons/Icon';

interface Props {
  tag: 'BAD' | 'NORMAL' | 'GOOD';
}

const TAG_COMMENT = {
  NORMAL: '양호',
  GOOD: '정상',
  BAD: '위험',
};

const TAG_BG_COLOR = {
  NORMAL: 'bg-secondory-blue-50',
  GOOD: 'bg-primary-50',
  BAD: 'bg-secondory-red-50',
};

const TAG_TEXT_COLOR = {
  NORMAL: 'text-secondory-blue-600',
  GOOD: 'text-primary-600',
  BAD: 'text-secondory-red-600',
};

const TAG_STROKE_COLOR = {
  NORMAL: 'border-secondory-blue-200',
  GOOD: 'border-primary-200',
  BAD: 'border-secondory-red-200',
};

const TAG_CONVERT = {
  NORMAL: 'normal',
  GOOD: 'good',
  BAD: 'bad',
};

const ResultTag = ({ tag }: Props) => {
  const convertTagName = TAG_CONVERT[tag] as 'good' | 'normal' | 'bad';

  return (
    <div
      className={`${TAG_TEXT_COLOR[tag]} ${TAG_BG_COLOR[tag]} ${TAG_STROKE_COLOR[tag]} flex w-fit items-center gap-1 rounded-md border px-3`}
    >
      <div className="h-4 w-4">
        <Icon name={`${convertTagName}-face`} />
      </div>
      <span className="text-text-lg font-semibold">{TAG_COMMENT[tag]}</span>
    </div>
  );
};

export default ResultTag;
