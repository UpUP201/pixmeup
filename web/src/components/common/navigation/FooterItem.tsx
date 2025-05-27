import { FooterItemProps } from './types';
import { NavLink } from 'react-router-dom';
import Icon from '../icons/Icon';

export const FooterItem = ({ item }: FooterItemProps) => {
  return (
    <NavLink 
      to={item.path}
      className={({ isActive }) =>
        `flex flex-col items-center justify-center w-full h-full space-y-1
         ${isActive ? 'text-primary-500' : 'text-gray-500'}`
      }
    >
      {({ isActive }) => (
        <>
          <Icon name={item.icon} className={`w-6 h-6 ${isActive ? 'fill-primary-500' : 'fill-gray-500'}`} />
          <span className="text-xs">{item.label}</span>
        </>
      )}
    </NavLink>
  );
}; 