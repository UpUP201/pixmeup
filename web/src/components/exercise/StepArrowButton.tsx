import Icon from '../common/icons/Icon';
interface StepArrowButtonProps {
  direction: 'prev' | 'next';
  onClick: () => void;
  label: string;
}
const StepArrowButton = ({ direction, onClick, label }: StepArrowButtonProps) => {
  return (
    <button onClick={onClick} className="cursor-pointer focus:outline-none" aria-label={label}>
      <Icon
        name={direction === 'prev' ? 'arrow-left-circle-fill' : 'arrow-right-circle-fill'}
        className="fill-primary-400"
        size={84}
      />
    </button>
  );
};

export default StepArrowButton;
