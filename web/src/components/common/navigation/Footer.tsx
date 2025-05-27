import { FooterItem } from './FooterItem';
import { FooterItems, FooterProps } from './types';

const footerItems: FooterItems = [
  {
    id: 'home',
    label: '홈',
    icon: 'home',
    path: '/',
  },
  {
    id: 'eye-exercise',
    label: '눈 운동',
    icon: 'play-circle',
    path: '/exercise',
  },
  {
    id: 'report',
    label: '리포트',
    icon: 'chart-vertical',
    path: '/report',
  },
  {
    id: 'profile',
    label: '내 정보',
    icon: 'user',
    path: '/profile',
  },
];

export const Footer = ({ className }: FooterProps) => {
  return (
    <nav className={`h-16 border-t border-gray-200 bg-white py-2.5 ${className || ''}`}>
      <div className="grid h-full grid-cols-4">
        {footerItems.map((item) => (
          <FooterItem key={item.id} item={item} />
        ))}
      </div>
    </nav>
  );
};
