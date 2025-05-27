import { SVGProps } from 'react';
import { IconName } from './types';

interface IconProps extends SVGProps<SVGSVGElement> {
  name: IconName;
  size?: number;
}

const Icon = ({ name, className = 'fill-current', size = 24, ...props }: IconProps) => {
  return (
    <svg
      className={className}
      width={size}
      height={size}
      viewBox="0 0 24 24"
      preserveAspectRatio="xMidYMid meet"
      {...props}
    >
      <use href={`/assets/icons/sprites.svg#${name}`} />
    </svg>
  );
};

export default Icon;
