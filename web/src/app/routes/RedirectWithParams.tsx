import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router';

const RedirectWithParams = ({ to }: { to: string }) => {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const queryString = location.search;

    const destinationPath = queryString ? `${to}${queryString}` : to;

    navigate(destinationPath, { replace: true });
  }, [to, location.search, navigate]);

  return null;
};

export default RedirectWithParams;
