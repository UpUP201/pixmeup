interface Props {
  text: string;
}

const PreciseDiagnosisDetail = ({ text }: Props) => {
  return (
    <div className="border-line-50 flex max-h-200 w-full flex-col gap-2 rounded-xl border px-6.5 py-6">
      <h3 className="text-display-sm text-line-900 font-semibold">자세한 상태</h3>
      <p className="text-text-lg text-line-900">{text}</p>
    </div>
  );
};

export default PreciseDiagnosisDetail;
