import { codegenNativeComponent } from 'react-native';
import type { ViewProps } from 'react-native';
import type {
  DirectEventHandler,
  Double,
} from 'react-native/Libraries/Types/CodegenTypes';

export type OnNativeLayout = Readonly<{
  width: Double;
  height: Double;
}>;

export type OnPlacementChanged = Readonly<{
  placement: string;
}>;

export interface BottomAccessoryViewProps extends ViewProps {
  onNativeLayout?: DirectEventHandler<OnNativeLayout>;
  onPlacementChanged?: DirectEventHandler<OnPlacementChanged>;
}

export default codegenNativeComponent<BottomAccessoryViewProps>(
  'BottomAccessoryView',
  {
    excludedPlatforms: ['android'],
  }
);
