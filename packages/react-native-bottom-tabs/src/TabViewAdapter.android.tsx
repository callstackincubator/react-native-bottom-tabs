import NativeTabView from './TabViewNativeComponent';
import type { TabViewProps } from './TabViewNativeComponent';
import { StyleSheet, View } from 'react-native';

const TabViewAdapter = ({ children, style: _, ...props }: TabViewProps) => {
  return (
    <NativeTabView {...props} style={styles.full}>
      <View style={styles.full}>{children}</View>
    </NativeTabView>
  );
};

const styles = StyleSheet.create({
  full: {
    width: '100%',
    height: '100%',
  },
});

export default TabViewAdapter;
