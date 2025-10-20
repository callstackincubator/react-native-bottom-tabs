# Web Platform Support

React Native Bottom Tabs uses native platform primitives (SwiftUI on iOS and Material Design on Android) which are not available on web. For web applications, you'll need to use JavaScript-based tab implementations as a fallback.

## How It Works

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

## Icon Support

On web, you cannot use SF Symbols. Instead, use cross-platform icon libraries:

**Expo Vector Icons** (recommended):

```tsx
import { Ionicons } from '@expo/vector-icons';

options={{
  tabBarIcon: ({ color, size }) => (
    <Ionicons name="home" size={size} color={color} />
  ),
}}
```

**React Icons:**

```tsx
import { FiHome } from 'react-icons/fi';

options={{
  tabBarIcon: ({ color }) => <FiHome color={color} />,
}}
```

## Additional Resources

- [React Navigation Bottom Tabs](https://reactnavigation.org/docs/bottom-tab-navigator/)
- [React Native Web Documentation](https://necolas.github.io/react-native-web/)
- [Metro Bundler Platform-Specific Extensions](https://reactnative.dev/docs/platform-specific-code#platform-specific-extensions)
