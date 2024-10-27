import TabView, { SceneMap } from 'react-native-bottom-tabs';
import { useState } from 'react';
import { Article } from '../Screens/Article';
import { Albums } from '../Screens/Albums';
import { Contacts } from '../Screens/Contacts';
import { Chat } from '../Screens/Chat';

export default function TintColorsExample() {
  const [index, setIndex] = useState(0);
  const [routes] = useState([
    {
      key: 'article',
      title: 'Article',
      focusedIcon: require('../../assets/icons/article_dark.png'),
      unfocusedIcon: require('../../assets/icons/chat_dark.png'),
      badge: '!',
    },
    {
      key: 'albums',
      title: 'Albums',
      focusedIcon: require('../../assets/icons/grid_dark.png'),
      badge: '5',
      activeTintColor: 'green',
    },
    {
      key: 'contacts',
      focusedIcon: require('../../assets/icons/person_dark.png'),
      title: 'Contacts',
      activeTintColor: 'yellow',
    },
    {
      key: 'chat',
      focusedIcon: require('../../assets/icons/chat_dark.png'),
      title: 'Chat',
    },
  ]);

  const renderScene = SceneMap({
    article: Article,
    albums: Albums,
    contacts: Contacts,
    chat: Chat,
  });

  return (
    <TabView
      sidebarAdaptable
      navigationState={{ index, routes }}
      onIndexChange={setIndex}
      renderScene={renderScene}
      tabBarActiveTintColor="red"
      tabBarInactiveTintColor="orange"
      scrollEdgeAppearance="default"
    />
  );
}
