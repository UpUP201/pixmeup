import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const PUBLIC_PATHS = ['/landing', '/login', '/signup'];

export const useAuth = () => {
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');
  const isAuthenticated = !!accessToken;

  useEffect(() => {
    const currentPath = window.location.pathname;
    
    if (!isAuthenticated && !PUBLIC_PATHS.includes(currentPath)) {
      navigate('/landing');
    } else if (isAuthenticated && PUBLIC_PATHS.includes(currentPath)) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  return {
    isAuthenticated,
  };
}; 