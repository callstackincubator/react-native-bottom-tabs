import TabView, { SceneMap } from 'react-native-bottom-tabs';
import { useState } from 'react';
import { Article } from '../Screens/Article';
import { Albums } from '../Screens/Albums';
import { Contacts } from '../Screens/Contacts';
import { Chat } from '../Screens/Chat';

export default function FourTabs() {
  const [index, setIndex] = useState(0);
  const [routes] = useState([
    {
      key: 'article',
      title: 'Article',
      focusedIcon: require('../../assets/icons/article.png'),
      unfocusedIcon: require('../../assets/icons/chat.png'),
      badge: '!',
    },
    {
      key: 'albums',
      title: 'Albums',
      focusedIcon: require('../../assets/icons/grid.png'),
      badge: '5',
    },
    {
      key: 'contacts',
      focusedIcon: require('../../assets/icons/person.png'),
      title: 'Contacts',
    },
    {
      key: 'chat',
      focusedIcon: require('../../assets/icons/chat.png'),
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
      navigationState={{ index, routes }}
      onIndexChange={setIndex}
      renderScene={renderScene}
    />
  );
}
