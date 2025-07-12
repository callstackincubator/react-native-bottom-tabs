import React, { useLayoutEffect, useRef } from 'react';
import type {
  OnNativeLayout,
  OnPageSelectedEventData,
  OnTabBarMeasured,
  TabViewItems,
} from './TabViewNativeComponent';
import {
  type ColorValue,
  type DimensionValue,
  Image,
  Platform,
  StyleSheet,
  View,
  processColor,
} from 'react-native';
import { BottomTabBarHeightContext } from './utils/BottomTabBarHeightContext';

//@ts-ignore
import type { ImageSource } from 'react-native/Libraries/Image/ImageSource';
import NativeTabView from './TabViewNativeComponent';
import useLatestCallback from 'use-latest-callback';
import type { BaseRoute, NavigationState, TabRole } from './types';
import DelayedFreeze from './DelayedFreeze';

const isAppleSymbol = (icon: any): icon is { sfSymbol: string } =>
  icon?.sfSymbol;

interface Props<Route extends BaseRoute> {
  /*
   * Whether to show labels in tabs. When false, only icons will be displayed.
   */
  labeled?: boolean;
  /**
   * A tab bar style that adapts to each platform.
   *
   * that varies depending on the platform:
   * Tab views using the sidebar adaptable style have an appearance
   * - iPadOS displays a top tab bar that can adapt into a sidebar.
   * - iOS displays a bottom tab bar.
   * - macOS and tvOS always show a sidebar.
   * - visionOS shows an ornament and also shows a sidebar for secondary tabs within a `TabSection`.
   */
  sidebarAdaptable?: boolean;
  /**
   * Whether to disable page animations between tabs. (iOS only) Defaults to `false`.
   */
  disablePageAnimations?: boolean;
  /**
   * Whether to enable haptic feedback. Defaults to `true`.
   */
  hapticFeedbackEnabled?: boolean;
  /**
   * Describes the appearance attributes for the tabBar to use when an observable scroll view is scrolled to the bottom. (iOS only)
   */
  scrollEdgeAppearance?: 'default' | 'opaque' | 'transparent';

  /**
   * Behavior for minimizing the tab bar. (iOS 26+)
   */
  minimizeBehavior?: 'automatic' | 'onScrollDown' | 'onScrollUp' | 'never';
  /**
   * Active tab color.
   */
  tabBarActiveTintColor?: ColorValue;
  /**
   * Inactive tab color.
   */
  tabBarInactiveTintColor?: ColorValue;
  /**
   * State for the tab view.
   *
   * The state should contain a `routes` prop which is an array of objects containing `key` and `title` props, such as `{ key: 'music', title: 'Music' }`.
   *
   */
  navigationState: NavigationState<Route>;
  /**
   * Function which takes an object with the route and returns a React element.
   */
  renderScene: (props: {
    route: Route;
    jumpTo: (key: string) => void;
  }) => React.ReactNode | null;
  /**
   * Callback which is called on tab change, receives the index of the new tab as argument.
   */
  onIndexChange: (index: number) => void;
  /**
   * Callback which is called on long press on tab, receives the index of the tab as argument.
   */
  onTabLongPress?: (index: number) => void;
  /**
   * Get lazy for the current screen. Uses true by default.
   */
  getLazy?: (props: { route: Route }) => boolean | undefined;
  /**
   * Get label text for the tab, uses `route.title` by default.
   */
  getLabelText?: (props: { route: Route }) => string | undefined;
  /**
   * Get badge for the tab, uses `route.badge` by default.
   */
  getBadge?: (props: { route: Route }) => string | undefined;
  /**
   * Get active tint color for the tab, uses `route.activeTintColor` by default.
   */
  getActiveTintColor?: (props: { route: Route }) => ColorValue | undefined;
  /**
   * Get icon for the tab, uses `route.focusedIcon` by default.
   */
  getIcon?: (props: {
    route: Route;
    focused: boolean;
  }) => ImageSource | undefined;

  /**
   * Get hidden for the tab, uses `route.hidden` by default.
   * If `true`, the tab will be hidden.
   */
  getHidden?: (props: { route: Route }) => boolean | undefined;

  /**
   * Get testID for the tab, uses `route.testID` by default.
   */
  getTestID?: (props: { route: Route }) => string | undefined;

  /**
   * Get role for the tab, uses `route.role` by default. (iOS only)
   */
  getRole?: (props: { route: Route }) => TabRole | undefined;

  /**
   * Custom tab bar to render. Set to `null` to hide the tab bar completely.
   */
  tabBar?: () => React.ReactNode;

  /**
   * Get freezeOnBlur for the current screen. Uses false by default.
   */
  getFreezeOnBlur?: (props: { route: Route }) => boolean | undefined;

  tabBarStyle?: {
    /**
     * Background color of the tab bar.
     */
    backgroundColor?: ColorValue;
  };

  /**
   * A Boolean value that indicates whether the tab bar is translucent. (iOS only)
   */
  translucent?: boolean;
  rippleColor?: ColorValue;
  /**
   * Color of tab indicator. (Android only)
   */
  activeIndicatorColor?: ColorValue;
  tabLabelStyle?: {
    /**
     * Font family for the tab labels.
     */
    fontFamily?: string;

    /**
     * Font weight for the tab labels.
     */
    fontWeight?: string;

    /**
     * Font size for the tab labels.
     */
    fontSize?: number;
  };
}

const ANDROID_MAX_TABS = 100;

const TabView = <Route extends BaseRoute>({
  navigationState,
  renderScene,
  onIndexChange,
  onTabLongPress,
  rippleColor,
  tabBarActiveTintColor: activeTintColor,
  tabBarInactiveTintColor: inactiveTintColor,
  getBadge = ({ route }: { route: Route }) => route.badge,
  getLazy = ({ route }: { route: Route }) => route.lazy,
  getLabelText = ({ route }: { route: Route }) => route.title,
  getIcon = ({ route, focused }: { route: Route; focused: boolean }) =>
    route.unfocusedIcon
      ? focused
        ? route.focusedIcon
        : route.unfocusedIcon
      : route.focusedIcon,
  getHidden = ({ route }: { route: Route }) => route.hidden,
  getActiveTintColor = ({ route }: { route: Route }) => route.activeTintColor,
  getTestID = ({ route }: { route: Route }) => route.testID,
  getRole = ({ route }: { route: Route }) => route.role,
  hapticFeedbackEnabled = false,
  // Android's native behavior is to show labels when there are less than 4 tabs. We leave it as undefined to use the platform default behavior.
  labeled = Platform.OS !== 'android' ? true : undefined,
  getFreezeOnBlur = ({ route }: { route: Route }) => route.freezeOnBlur,
  tabBar: renderCustomTabBar,
  tabBarStyle,
  tabLabelStyle,
  ...props
}: Props<Route>) => {
  // @ts-ignore
  const focusedKey = navigationState.routes[navigationState.index].key;
  const customTabBarWrapperRef = useRef<View>(null);
  const [tabBarHeight, setTabBarHeight] = React.useState<number | undefined>(0);
  const [measuredDimensions, setMeasuredDimensions] = React.useState<
    { width: DimensionValue; height: DimensionValue } | undefined
  >({ width: '100%', height: '100%' });

  const trimmedRoutes = React.useMemo(() => {
    if (
      Platform.OS === 'android' &&
      navigationState.routes.length > ANDROID_MAX_TABS
    ) {
      console.warn(
        `TabView only supports up to ${ANDROID_MAX_TABS} tabs on Android`
      );
      return navigationState.routes.slice(0, ANDROID_MAX_TABS);
    }
    return navigationState.routes;
  }, [navigationState.routes]);

  /**
   * List of loaded tabs, tabs will be loaded when navigated to.
   */
  const [loaded, setLoaded] = React.useState<string[]>([focusedKey]);

  if (!loaded.includes(focusedKey)) {
    // Set the current tab to be loaded if it was not loaded before
    setLoaded((loaded) => [...loaded, focusedKey]);
  }

  const icons = React.useMemo(
    () =>
      trimmedRoutes.map((route) =>
        getIcon({
          route,
          focused: route.key === focusedKey,
        })
      ),
    [focusedKey, getIcon, trimmedRoutes]
  );

  const items: TabViewItems = React.useMemo(
    () =>
      trimmedRoutes.map((route, index) => {
        const icon = icons[index];
        const isSfSymbol = isAppleSymbol(icon);

        if (Platform.OS === 'android' && isSfSymbol) {
          console.warn(
            'SF Symbols are not supported on Android. Use require() or pass uri to load an image instead.'
          );
        }

        return {
          key: route.key,
          title: getLabelText({ route }) ?? route.key,
          sfSymbol: isSfSymbol ? icon.sfSymbol : undefined,
          badge: getBadge?.({ route }),
          activeTintColor: processColor(getActiveTintColor({ route })),
          hidden: getHidden?.({ route }),
          testID: getTestID?.({ route }),
          role: getRole?.({ route }),
        };
      }),
    [
      trimmedRoutes,
      icons,
      getLabelText,
      getBadge,
      getActiveTintColor,
      getHidden,
      getTestID,
      getRole,
    ]
  );

  const resolvedIconAssets: ImageSource[] = React.useMemo(
    () =>
      // Pass empty object for icons that are not provided to avoid index mismatch on native side.
      icons.map((icon) =>
        icon && !isAppleSymbol(icon)
          ? Image.resolveAssetSource(icon)
          : { uri: '' }
      ),
    [icons]
  );

  const jumpTo = useLatestCallback((key: string) => {
    const index = trimmedRoutes.findIndex((route) => route.key === key);

    onIndexChange(index);
  });

  const handleTabLongPress = React.useCallback(
    ({ nativeEvent: { key } }: { nativeEvent: OnPageSelectedEventData }) => {
      const index = trimmedRoutes.findIndex((route) => route.key === key);
      onTabLongPress?.(index);
    },
    [trimmedRoutes, onTabLongPress]
  );

  const handlePageSelected = React.useCallback(
    ({ nativeEvent: { key } }: { nativeEvent: OnPageSelectedEventData }) => {
      jumpTo(key);
    },
    [jumpTo]
  );

  const handleTabBarMeasured = React.useCallback(
    ({ nativeEvent: { height } }: { nativeEvent: OnTabBarMeasured }) => {
      setTabBarHeight(height);
    },
    [setTabBarHeight]
  );

  const handleNativeLayout = React.useCallback(
    ({ nativeEvent: { width, height } }: { nativeEvent: OnNativeLayout }) => {
      setMeasuredDimensions({ width, height });
    },
    [setMeasuredDimensions]
  );

  useLayoutEffect(() => {
    // If we are rendering a custom tab bar, we need to measure it to set the tab bar height.
    if (renderCustomTabBar && customTabBarWrapperRef.current) {
      customTabBarWrapperRef.current.measure((_x, _y, _width, height) => {
        setTabBarHeight(height);
      });
    }
  }, [renderCustomTabBar]);

  return (
    <BottomTabBarHeightContext.Provider value={tabBarHeight}>
      <NativeTabView
        {...props}
        {...tabLabelStyle}
        style={styles.fullWidth}
        items={items}
        // When rendering a custom tab bar, icons can be React elements, which will not be properly resolved.
        icons={renderCustomTabBar ? undefined : resolvedIconAssets}
        selectedPage={focusedKey}
        tabBarHidden={!!renderCustomTabBar}
        onTabLongPress={handleTabLongPress}
        onPageSelected={handlePageSelected}
        onTabBarMeasured={handleTabBarMeasured}
        onNativeLayout={handleNativeLayout}
        hapticFeedbackEnabled={hapticFeedbackEnabled}
        activeTintColor={activeTintColor}
        inactiveTintColor={inactiveTintColor}
        barTintColor={tabBarStyle?.backgroundColor}
        rippleColor={rippleColor}
        labeled={labeled}
      >
        {trimmedRoutes.map((route) => {
          if (getLazy({ route }) !== false && !loaded.includes(route.key)) {
            // Don't render a screen if we've never navigated to it
            return (
              <View
                key={route.key}
                collapsable={false}
                style={styles.fullWidth}
              />
            );
          }

          const focused = route.key === focusedKey;
          const freeze = !focused ? getFreezeOnBlur({ route }) : false;

          return (
            <View
              key={route.key}
              style={[
                styles.screen,
                renderCustomTabBar ? styles.fullWidth : measuredDimensions,
              ]}
              collapsable={false}
              pointerEvents={focused ? 'auto' : 'none'}
              accessibilityElementsHidden={!focused}
              importantForAccessibility={
                focused ? 'auto' : 'no-hide-descendants'
              }
            >
              <DelayedFreeze freeze={!!freeze}>
                {renderScene({
                  route,
                  jumpTo,
                })}
              </DelayedFreeze>
            </View>
          );
        })}
      </NativeTabView>
      {renderCustomTabBar ? (
        <View ref={customTabBarWrapperRef}>{renderCustomTabBar()}</View>
      ) : null}
    </BottomTabBarHeightContext.Provider>
  );
};

const styles = StyleSheet.create({
  fullWidth: {
    width: '100%',
    height: '100%',
    flex: 1,
  },
  screen: {
    position: 'absolute',
  },
});

export default TabView;
