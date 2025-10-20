# Web Platform Support

React Native Bottom Tabs uses native platform primitives (SwiftUI on iOS and Material Design on Android) which are not available on web. For web applications, you'll need to use JavaScript-based tab implementations as a fallback.

## Why Web Requires a Different Approach

Native bottom tabs rely on platform-specific UI components that don't exist in web browsers. React Native Web translates React Native components to web equivalents, but native modules require explicit web implementations.

:::note

Web support requires using platform-specific files or a custom tab bar implementation. This guide shows you both approaches.

:::

## Approach 1: Platform-Specific Files (Recommended)

React Native's Metro bundler automatically resolves platform-specific files. You can create separate implementations for native platforms and web.

### File Structure

```
src/
├── navigation/
│   ├── TabNavigator.native.tsx    # Used on iOS/Android
│   └── TabNavigator.web.tsx       # Used on web
```

### Native Implementation

```tsx title="TabNavigator.native.tsx"
import { createNativeBottomTabNavigator } from '@bottom-tabs/react-navigation';
import { HomeScreen } from '../screens/HomeScreen';
import { SettingsScreen } from '../screens/SettingsScreen';

const Tabs = createNativeBottomTabNavigator();

export function TabNavigator() {
  return (
    <Tabs.Navigator>
      <Tabs.Screen
        name="Home"
        component={HomeScreen}
        options={{
          tabBarIcon: () => ({ sfSymbol: 'house' }),
        }}
      />
      <Tabs.Screen
        name="Settings"
        component={SettingsScreen}
        options={{
          tabBarIcon: () => ({ sfSymbol: 'gear' }),
        }}
      />
    </Tabs.Navigator>
  );
}
```

### Web Implementation

For web, use `@react-navigation/bottom-tabs` which provides a JavaScript-based implementation:

```bash
npm install @react-navigation/bottom-tabs
```

```tsx title="TabNavigator.web.tsx"
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { HomeScreen } from '../screens/HomeScreen';
import { SettingsScreen } from '../screens/SettingsScreen';

const Tab = createBottomTabNavigator();

export function TabNavigator() {
  return (
    <Tab.Navigator>
      <Tab.Screen
        name="Home"
        component={HomeScreen}
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon name="home" size={size} color={color} />
          ),
        }}
      />
      <Tab.Screen
        name="Settings"
        component={SettingsScreen}
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon name="settings" size={size} color={color} />
          ),
        }}
      />
    </Tab.Navigator>
  );
}
```

### Using in Your App

Import the component normally - React Native will automatically use the correct file:

```tsx title="App.tsx"
import { NavigationContainer } from '@react-navigation/native';
import { TabNavigator } from './navigation/TabNavigator';

export default function App() {
  return (
    <NavigationContainer>
      <TabNavigator />
    </NavigationContainer>
  );
}
```

## Approach 2: Custom Tab Bar with Standalone Usage

If you're using the standalone `TabView` component, you can provide a custom `tabBar` prop that renders a web-compatible implementation.

```tsx
import { Platform } from 'react-native';
import TabView from 'react-native-bottom-tabs';

function MyTabs() {
  const [index, setIndex] = React.useState(0);
  const [routes] = React.useState([
    { key: 'home', title: 'Home' },
    { key: 'settings', title: 'Settings' },
  ]);

  const renderScene = SceneMap({
    home: HomeScreen,
    settings: SettingsScreen,
  });

  return (
    <TabView
      navigationState={{ index, routes }}
      renderScene={renderScene}
      onIndexChange={setIndex}
      tabBar={Platform.OS === 'web' ? CustomWebTabBar : undefined}
    />
  );
}
```

### Example Custom Web Tab Bar

```tsx
function CustomWebTabBar() {
  return (
    <View style={styles.tabBar}>
      <TouchableOpacity style={styles.tab}>
        <Text>Home</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.tab}>
        <Text>Settings</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  tabBar: {
    flexDirection: 'row',
    backgroundColor: '#fff',
    borderTopWidth: 1,
    borderTopColor: '#e0e0e0',
    height: 60,
  },
  tab: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
```

## Approach 3: Conditional Platform Rendering

For simpler cases, you can conditionally render different navigators based on platform:

```tsx
import { Platform } from 'react-native';
import { createNativeBottomTabNavigator } from '@bottom-tabs/react-navigation';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';

const NativeTabs = createNativeBottomTabNavigator();
const JSTabs = createBottomTabNavigator();

const Tabs = Platform.OS === 'web' ? JSTabs : NativeTabs;

export function TabNavigator() {
  return (
    <Tabs.Navigator>
      <Tabs.Screen name="Home" component={HomeScreen} />
      <Tabs.Screen name="Settings" component={SettingsScreen} />
    </Tabs.Navigator>
  );
}
```

:::warning

This approach requires installing both `@bottom-tabs/react-navigation` and `@react-navigation/bottom-tabs`, which increases bundle size for all platforms.

:::

## Styling Considerations

When implementing web tabs, consider these styling differences:

### Native Platforms
- Use `sfSymbol` for iOS icons
- Platform-specific appearance attributes
- Native gestures and animations

### Web Platform
- Use web-compatible icon libraries (e.g., `react-icons`, `@expo/vector-icons`)
- CSS-based styling and animations
- Standard web accessibility practices

## Icon Libraries for Web

For web compatibility, use icon libraries that work across all platforms:

### Expo Vector Icons

```tsx
import { Ionicons } from '@expo/vector-icons';

options={{
  tabBarIcon: ({ color, size }) => (
    <Ionicons name="home" size={size} color={color} />
  ),
}}
```

### React Icons

```tsx
import { FiHome } from 'react-icons/fi';

options={{
  tabBarIcon: ({ color }) => <FiHome color={color} />,
}}
```

## Summary

| Approach | Pros | Cons |
|----------|------|------|
| **Platform-Specific Files** | Clean separation, optimal bundle size | Requires maintaining two implementations |
| **Custom Tab Bar** | Full control, single codebase | More code to maintain |
| **Conditional Rendering** | Simple to understand | Both libraries in bundle |

For most projects, **platform-specific files** provide the best balance of code organization, bundle size, and developer experience.

## Additional Resources

- [React Navigation Bottom Tabs](https://reactnavigation.org/docs/bottom-tab-navigator/)
- [React Native Web Documentation](https://necolas.github.io/react-native-web/)
- [Metro Bundler Platform-Specific Extensions](https://reactnative.dev/docs/platform-specific-code#platform-specific-extensions)
