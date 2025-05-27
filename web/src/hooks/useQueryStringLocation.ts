import { useMemo } from "react";
import { useLocation } from "react-router";

const useQueryStringLocation = (to:string) => {

  const location = useLocation();

  const newPath = useMemo(() => {

    const queryString = location.search;

    const destinationPath = queryString ? `${to}${queryString}` : to;

    return destinationPath;

  },[to, location.search])


  return newPath;
}

export default useQueryStringLocation;