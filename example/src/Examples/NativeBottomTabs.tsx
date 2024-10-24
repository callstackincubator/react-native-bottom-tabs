import { Article } from '../Screens/Article';
import { Albums } from '../Screens/Albums';
import { Contacts } from '../Screens/Contacts';
import { Chat } from '../Screens/Chat';
// This import works properly when library is published
import createNativeBottomTabNavigator from '../../../src/react-navigation/navigators/createNativeBottomTabNavigator';
import { Platform } from 'react-native';

const Tab = createNativeBottomTabNavigator();

function NativeBottomTabs() {
  return (
    <Tab.Navigator
      tabBarInactiveTintColor="red"
      tabBarActiveTintColor="orange"
      screenListeners={{
        tabLongPress: (data) => {
          console.log(
            `${Platform.OS}: Long press detected on tab with key ${data.target} at the navigator level.`
          );
        },
      }}
    >
      <Tab.Screen
        name="Article"
        component={Article}
        listeners={{
          tabLongPress: (data) => {
            console.log(
              `${Platform.OS}: Long press detected on tab with key ${data.target} at the screen level.`
            );
          },
        }}
        options={{
          tabBarBadge: '10',
          tabBarIcon: ({ focused }) =>
            focused
              ? require('../../assets/icons/person_dark.png')
              : require('../../assets/icons/article_dark.png'),
        }}
      />
      <Tab.Screen
        name="Albums"
        component={Albums}
        options={{
          tabBarIcon: () => require('../../assets/icons/grid_dark.png'),
        }}
      />
      <Tab.Screen
        name="Contacts"
        component={Contacts}
        options={{
          tabBarIcon: () => require('../../assets/icons/person_dark.png'),
          tabBarActiveTintColor: 'yellow',
        }}
      />
      <Tab.Screen
        name="Chat"
        component={Chat}
        options={{
          tabBarIcon: () => require('../../assets/icons/chat_dark.png'),
          tabBarActiveTintColor: 'purple',
        }}
      />
    </Tab.Navigator>
  );
}

export default NativeBottomTabs;
