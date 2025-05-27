import { IconName } from "@/components/common/icons/types";
import { create } from "zustand";

interface Props {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
  modalProps: {
    icon?: IconName;
    title: string;
    description: string;
    confirmText: string;
    denyText: string;
    onConfirm: (e: React.MouseEvent) => void;
    onDeny: (e: React.MouseEvent) => void;
  } | null;
  openModal: (props: Props["modalProps"]) => void;
  closeModal: () => void;
}

const useModalStore = create<Props>(((set) => ({
  isOpen: false,
  modalProps: null,
  setIsOpen: (isOpen: boolean) => set({isOpen}),
  openModal: (props) => set({isOpen: true, modalProps: props}),
  closeModal: () => set({isOpen: false, modalProps: null})
})));

export default useModalStore;