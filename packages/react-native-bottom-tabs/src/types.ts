import type { ImageSourcePropType } from 'react-native';
import type { SFSymbol } from 'sf-symbols-typescript';

export type IconSource = string | ImageSourcePropType;

export type AppleIcon = { sfSymbol: SFSymbol };

export type TabRole = 'search';

export type BaseRoute = {
  key: string;
  title?: string;
  badge?: string;
  lazy?: boolean;
  focusedIcon?: ImageSourcePropType | AppleIcon;
  unfocusedIcon?: ImageSourcePropType | AppleIcon;
  activeTintColor?: string;
  hidden?: boolean;
  testID?: string;
  role?: TabRole;
  freezeOnBlur?: boolean;
};

export type NavigationState<Route extends BaseRoute> = {
  index: number;
  routes: Route[];
};
