import useQueryStringLocation from '@/hooks/useQueryStringLocation';
import { NavLink } from 'react-router';

const EyeTestResultTab = () => {
  const nearVisionPath = useQueryStringLocation('near-vision');
  const presbyopiaPath = useQueryStringLocation('presbyopia');
  const amdPath = useQueryStringLocation('amd');

  return (
    <div className="flex w-full justify-center">
      <NavLink
        to={nearVisionPath}
        className={({ isActive }) =>
          `text-text-xl border-b-2 px-4 pt-1 pb-2 font-semibold ${isActive ? 'text-line-900 border-primary-500' : 'text-line-700 border-line-10'}`
        }
      >
        <span>근거리 시력</span>
      </NavLink>
      <NavLink
        to={presbyopiaPath}
        className={({ isActive }) =>
          `text-text-xl border-b-2 px-4 pt-1 pb-2 font-semibold ${isActive ? 'text-line-900 border-primary-500' : 'text-line-700 border-line-10'}`
        }
      >
        <span>노안 검사</span>
      </NavLink>
      <NavLink
        to={amdPath}
        className={({ isActive }) =>
          `text-text-xl border-b-2 px-4 pt-1 pb-2 font-semibold ${isActive ? 'text-line-900 border-primary-500' : 'text-line-700 border-line-10'}`
        }
      >
        <span>황반변성 검사</span>
      </NavLink>
    </div>
  );
};

export default EyeTestResultTab;
