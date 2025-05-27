import { Header } from '@/components/common/navigation/Header';
import Layout from './Layout';
import { RouteMatch } from '@/types/CommonType';
import { useMatches } from 'react-router';

const HeaderLayout = () => {
  const matches = useMatches() as RouteMatch[];

  const currentRoute = matches[matches.length - 1];
  const routeName = currentRoute.handle?.name || '';
  const routerShowBack = currentRoute.handle?.show;
  const routerPath = currentRoute.handle?.path;

  return (
    <Layout
      headerSlot={<Header title={routeName} path={routerPath} showBackButton={routerShowBack} />}
    />
  );
};

export default HeaderLayout;
