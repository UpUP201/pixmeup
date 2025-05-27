import { IconName } from '../icons/types';

// Footer 아이템 타입
export interface FooterItem {
  id: string;
  label: string;
  icon: IconName;
  path: string;
}

// Footer 아이템 배열 타입
export type FooterItems = FooterItem[];

// Footer 아이템 컴포넌트 props 타입
export interface FooterItemProps {
  item: FooterItem;
}

// Footer 컴포넌트 props 타입
export interface FooterProps {
  className?: string;
}

// Header 컴포넌트 props 타입
export interface HeaderProps {
  path?: string;
  title: string;
  showBackButton?: boolean;
}

// Header 버튼 props 타입
export interface HeaderButtonProps {
  onClick: () => void;
  icon: string;
  label: string;
}
