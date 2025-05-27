import { useMatches } from 'react-router';
import Layout from './Layout';
import { Header } from '@/components/common/navigation/Header';
import { Footer } from '@/components/common/navigation/Footer';
import { RouteMatch } from '@/types/CommonType';

const HeaderNavLayout = () => {
  const matches = useMatches() as RouteMatch[];

  const currentRoute = matches[matches.length - 1];
  const routeName = currentRoute.handle?.name || '';
  const routerShowBack = currentRoute.handle?.show;
  const routerPath = currentRoute.handle?.path;

  return (
    <Layout
      headerSlot={<Header title={routeName} path={routerPath} showBackButton={routerShowBack} />}
      navbarSlot={<Footer />}
    />
  );
};

export default HeaderNavLayout;
