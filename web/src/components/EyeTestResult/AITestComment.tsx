import Icon from '../common/icons/Icon';
import ResultTag from '../common/ResultTag';

interface Props {
  tag: 'BAD' | 'NORMAL' | 'GOOD';
  text: string;
}

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

const AITestComment = ({ tag, text }: Props) => {
  return (
    <div className="flex flex-col gap-3">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Icon name="ai" className="fill-secondory-blue-500" />
          <span className="text-line-900 font-semibold">AI 예측 결과</span>
        </div>
        <ResultTag tag={tag} />
      </div>
      <div
        className={`${TAG_BG_COLOR[tag]} ${TAG_STROKE_COLOR[tag]} ${TAG_TEXT_COLOR[tag]} w-full rounded-xl border px-5 py-5.5`}
      >
        <span className="text-text-lg font-semibold">{text}</span>
      </div>
    </div>
  );
};

export default AITestComment;
