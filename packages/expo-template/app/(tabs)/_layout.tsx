import React from 'react';
import { withLayoutContext } from 'expo-router';
import { createNativeBottomTabNavigator } from '@bottom-tabs/react-navigation';

const Tabs = withLayoutContext(createNativeBottomTabNavigator().Navigator);

export default function TabLayout() {
  return (
    <Tabs>
      <Tabs.Screen
        name="index"
        options={{
          title: 'Home',
          tabBarIcon: () => ({ sfSymbol: 'house.fill' }),
        }}
      />
      <Tabs.Screen
        name="explore"
        options={{
          title: 'Explore',
          tabBarIcon: () => ({ sfSymbol: 'paperplane.fill' }),
        }}
      />
    </Tabs>
  );
}
