interface Props {
  text: string;
}

const PreciseDiagnosisThumbnail = ({ text }: Props) => {
  return (
    <div className="border-primary-500 bg-primary-50 text-primary-500 text-display-sm flex flex-col gap-2 rounded-md border px-6.5 py-3 font-medium">
      <span>안구 상태 요약</span>
      <span className="text-primary-800">{text}</span>
    </div>
  );
};

export default PreciseDiagnosisThumbnail;
