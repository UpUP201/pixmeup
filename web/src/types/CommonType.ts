interface HandleProps {
  name?: string;
  path?: string;
  show?: boolean;
}

export interface RouteMatch {
  id: string;
  pathname: string;
  params: Record<string, string>;
  data: unknown;
  handle?: HandleProps;
}