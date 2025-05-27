import { useModalStore } from '@/stores';
import { Button } from '../ui/button';
import Icon from './icons/Icon';

const BasicModal = () => {
  const { isOpen, modalProps, closeModal } = useModalStore();

  if (modalProps == null) return null;

  return (
    <div
      onClick={() => {
        closeModal();
      }}
      className={`absolute inset-0 z-50 flex h-full w-full items-center justify-center bg-black/50 ${isOpen ? 'block' : 'hidden'}`}
    >
      <div
        onClick={(e) => {
          e.stopPropagation();
        }}
        className="mx-4 w-full max-w-md rounded-lg bg-white p-8"
      >
        <div className="space-y-4 text-center">
          {modalProps.icon != undefined ? (
            <Icon name={modalProps.icon} className="mx-auto mb-2" />
          ) : null}
          <h2 className="text-xl font-bold">{modalProps.title}</h2>
          <p>{modalProps.description}</p>
        </div>
        <div className="mt-6 flex gap-3">
          <Button onClick={modalProps.onDeny} variant="outline" size="lg" className="flex-1">
            {modalProps.denyText}
          </Button>
          <Button onClick={modalProps.onConfirm} variant="primary" size="lg" className="flex-1">
            {modalProps.confirmText}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default BasicModal;
