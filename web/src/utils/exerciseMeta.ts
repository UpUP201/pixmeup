import type { IconName } from '@/components/common/icons/types';

export const EXERCISE_META: {
  [key: string]: {
    iconName: IconName;
    iconClass: string;
    bgClass: string;
    tgClass: string;
    tgIconClass?: string;
  };
} = {
  '1': {
    iconName: 'eye-open',
    iconClass: 'fill-line-100',
    bgClass: 'bg-gray-100',
    tgClass: 'bg-line-100 text-line-500',
    tgIconClass: 'fill-line-500',
  },
  '2': {
    iconName: 'transfer',
    iconClass: 'fill-secondory-blue-200',
    bgClass: 'bg-secondory-blue-50',
    tgClass: 'bg-secondory-blue-100 text-secondory-blue-500',
    tgIconClass: 'fill-secondory-blue-500',
  },
  '3': {
    iconName: 'camcorder',
    iconClass: 'fill-secondory-lemon-300',
    bgClass: 'bg-secondory-lemon-50',
    tgClass: 'bg-secondory-lemon-200 text-secondory-lemon-700',
    tgIconClass: 'fill-secondory-lemon-700',
  },
  '4': {
    iconName: 'balloon',
    iconClass: 'fill-primary-200',
    bgClass: 'bg-primary-50',
    tgClass: 'bg-primary-100 text-primary-600',
    tgIconClass: 'fill-primary-600',
  },
};
