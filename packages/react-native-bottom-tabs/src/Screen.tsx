import { View } from 'react-native';
import type { ViewProps } from 'react-native';
import DelayedFreeze from './DelayedFreeze';

interface ScreenProps extends ViewProps {
  children: React.ReactNode;
  freeze: boolean;
  focused?: boolean;
}

function Screen({ children, freeze, focused, ...props }: ScreenProps) {
  return (
    <View
      collapsable={false}
      pointerEvents={focused ? 'auto' : 'none'}
      accessibilityElementsHidden={!focused}
      importantForAccessibility={focused ? 'auto' : 'no-hide-descendants'}
      {...props}
    >
      <DelayedFreeze freeze={freeze}>{children}</DelayedFreeze>
    </View>
  );
}

export default Screen;
