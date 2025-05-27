interface Props {
  score: number;
  text: string;
}

const AMDResultBox = ({ text, score }: Props) => {
  return (
    <div className="border-line-50 flex flex-col gap-6 rounded-xl border px-6 py-6">
      <span className="text-primary-600 text-5xl font-extrabold">
        <strong className="font-semibold">{score}</strong>%
      </span>
      <div className="relative flex flex-col">
        <img
          src="/assets/images/shining-bead.png"
          alt="반짝이는 구"
          className="absolute right-0 bottom-0 w-40 translate-x-1/10 -translate-y-3/5"
        />
        <div className="flex flex-col gap-2">
          <span className="text-display-sm text-line-900 font-semibold">요약</span>
          <span className="text-text-lg text-line-900 font-medium">{text}</span>
        </div>
      </div>
    </div>
  );
};

export default AMDResultBox;
