import React from 'react';
import type { DimensionValue, ViewStyle } from 'react-native';
import BottomAccessoryViewNativeComponent, {
  type OnNativeLayout,
  type OnPlacementChanged,
} from './BottomAccessoryViewNativeComponent';

const defaultStyle: ViewStyle = {
  position: 'absolute',
  top: 0,
  left: 0,
};

export interface BottomAccessoryViewProps {
  renderBottomAccessoryView: (props: {
    placement: 'inline' | 'expanded' | 'none';
  }) => React.ReactNode;
}

export const BottomAccessoryView = (props: BottomAccessoryViewProps) => {
  const { renderBottomAccessoryView } = props;
  const [bottomAccessoryDimensions, setBottomAccessoryDimensions] =
    React.useState<
      { width: DimensionValue; height: DimensionValue } | undefined
    >({ width: '100%', height: '100%' });
  const [placement, setPlacement] = React.useState<
    'inline' | 'expanded' | 'none'
  >('none');

  const handleNativeLayout = React.useCallback(
    ({ nativeEvent: { width, height } }: { nativeEvent: OnNativeLayout }) => {
      setBottomAccessoryDimensions({ width, height });
    },
    [setBottomAccessoryDimensions]
  );

  const handlePlacementChanged = React.useCallback(
    ({ nativeEvent }: { nativeEvent: OnPlacementChanged }) => {
      if (
        nativeEvent.placement === 'inline' ||
        nativeEvent.placement === 'expanded' ||
        nativeEvent.placement === 'none'
      ) {
        setPlacement(nativeEvent.placement);
      }
    },
    [setPlacement]
  );

  return (
    <BottomAccessoryViewNativeComponent
      style={[defaultStyle, bottomAccessoryDimensions]}
      onNativeLayout={handleNativeLayout}
      onPlacementChanged={handlePlacementChanged}
    >
      {renderBottomAccessoryView({ placement })}
    </BottomAccessoryViewNativeComponent>
  );
};
