import type { ViewProps } from 'react-native';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

export interface TabViewScreenProps extends ViewProps {}

export default codegenNativeComponent<TabViewScreenProps>('RNCTabViewScreen', {
  interfaceOnly: true,
});
