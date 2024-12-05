# Standalone usage

If you don't use React Navigation, you can use the `TabView` component directly.

## Basic Example

```tsx
import * as React from 'react';
import { View, Text } from 'react-native';
import TabView, { SceneMap } from 'react-native-bottom-tabs';

const HomeScreen = () => (
  <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
    <Text>Home Screen</Text>
  </View>
);

const SettingsScreen = () => (
  <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
    <Text>Settings Screen</Text>
  </View>
);

const renderScene = SceneMap({
  home: HomeScreen,
  settings: SettingsScreen,
});

export default function TabViewExample() {
  const [index, setIndex] = React.useState(0);
  const [routes] = React.useState([
    {
      key: 'home',
      title: 'Home',
      focusedIcon: { sfSymbol: 'house' }
    },
    {
      key: 'settings',
      title: 'Settings',
      focusedIcon: { sfSymbol: 'gear' }
    },
  ]);

  return (
    <TabView
      navigationState={{ index, routes }}
      renderScene={renderScene}
      onIndexChange={setIndex}
      labeled
    />
  );
}
```

## Scene Components

Each scene in the tab view is a separate component that represents a screen. You can define these components independently:

```tsx
// HomeScreen.tsx
export const HomeScreen = () => (
  <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
    <Text>Home Screen</Text>
  </View>
);

// SettingsScreen.tsx
export const SettingsScreen = () => (
  <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
    <Text>Settings Screen</Text>
  </View>
);
```

Then use `SceneMap` to map route keys to scene components:

```tsx
import { SceneMap } from 'react-native-bottom-tabs';

const renderScene = SceneMap({
  home: HomeScreen,
  settings: SettingsScreen,
});
```

## Props

### Required Props

#### `navigationState`

State for the tab view. The state should contain:
- `routes`: Array of route objects containing `key` and `title` props
- `index`: Current selected tab index

#### `renderScene`

Function that returns a React element to render for the screen. Can be created using `SceneMap` or custom render function.

#### `onIndexChange`

Callback that is called when the tab index changes.

### Optional Props

#### `labeled`

Whether to show labels in tabs. When `false`, only icons will be displayed.
- Type: `boolean`
- Default <Badge text="iOS" type="info" />: `true`
- Default <Badge text="Android" type="info" />: `false`

#### `sidebarAdaptable` <Badge text="iOS" type="info" />

A tab bar style that adapts to each platform:
- iPadOS: Top tab bar that can adapt into a sidebar
- iOS: Bottom tab bar
- macOS/tvOS: Sidebar
- visionOS: Ornament with sidebar for secondary tabs

#### `ignoresTopSafeArea` <Badge text="iOS" type="info" />

Whether to ignore the top safe area.
- Type: `boolean`

#### `disablePageAnimations` <Badge text="iOS" type="info" />

Whether to disable animations between tabs.
- Type: `boolean`

#### `hapticFeedbackEnabled`

Whether to enable haptic feedback on tab press.
- Type: `boolean`
- Default: `false`


#### `tabLabelStyle`

Object containing styles for the tab label.
Supported properties:
- `fontFamily`
- `fontSize`
- `fontWeight`

#### `scrollEdgeAppearance` <Badge text="iOS" type="info" />

Appearance attributes for the tab bar when a scroll view is at the bottom.
- Type: `'default' | 'opaque' | 'transparent'`

#### `tabBarActiveTintColor`

Color for the active tab.
- Type: `ColorValue`

#### `tabBarInactiveTintColor`

Color for inactive tabs.
- Type: `ColorValue`

#### `barTintColor`

Background color of the tab bar.
- Type: `ColorValue`

#### `translucent` <Badge text="iOS" type="info" />

Whether the tab bar is translucent.
- Type: `boolean`

#### `activeIndicatorColor` <Badge text="Android" type="info" />

Color of tab indicator.
- Type: `ColorValue`

### Route Configuration

Each route in the `routes` array can have the following properties:

- `key`: Unique identifier for the route
- `title`: Display title for the tab
- `focusedIcon`: Icon to show when tab is active
- `unfocusedIcon`: Icon to show when tab is inactive (optional)
- `badge`: Badge text to display on the tab
- `activeTintColor`: Custom active tint color for this specific tab
- `lazy`: Whether to lazy load this tab's content

### Helper Props

#### `getLazy`

Function to determine if a screen should be lazy loaded.
- Default: Uses `route.lazy`

#### `getLabelText`

Function to get the label text for a tab.
- Default: Uses `route.title`

#### `getBadge`

Function to get the badge text for a tab.
- Default: Uses `route.badge`

#### `getActiveTintColor`

Function to get the active tint color for a tab.
- Default: Uses `route.activeTintColor`

#### `getIcon`

Function to get the icon for a tab.

- Default: Uses `route.focusedIcon` and `route.unfocusedIcon`


#### `getHidden`

Function to determine if a tab should be hidden.

- Default: Uses `route.hidden`
