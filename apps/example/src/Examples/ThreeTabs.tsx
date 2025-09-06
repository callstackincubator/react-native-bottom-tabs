import TabView, { SceneMap } from 'react-native-bottom-tabs';
import { useState } from 'react';
import { Article } from '../Screens/Article';
import { Albums } from '../Screens/Albums';
import { Contacts } from '../Screens/Contacts';
import { Button, View } from 'react-native';

export default function ThreeTabs() {
  const [index, setIndex] = useState(0);
  const [extraTab, setExtraTab] = useState(false);
  const [routes, setRoutes] = useState([
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
    },
    {
      key: 'contacts',
      focusedIcon: require('../../assets/icons/person_dark.png'),
      title: 'Contacts',
    },
  ]);

  const renderScene = SceneMap({
    article: Article,
    albums: Albums,
    contacts: Contacts,
    extra: Article,
  });

  const handleToggleTab = () => {
    if (extraTab) {
      setRoutes((prev) => prev.filter((r) => r.key !== 'extra'));
      setIndex(index === routes.length - 1 ? routes.length - 2 : index);
    } else {
      setRoutes((prev) => [
        ...prev,
        {
          key: 'extra',
          title: 'Extra',
          focusedIcon: require('../../assets/icons/article_dark.png'),
        },
      ]);
      setIndex(index);
    }
    setExtraTab(!extraTab);
  };

  return (
    <View style={{ flex: 1 }}>
      <View
        style={{ position: 'absolute', top: 0, left: 0, right: 0, zIndex: 1 }}
      >
        <Button
          title={extraTab ? 'Remove Extra Tab' : 'Add Extra Tab'}
          onPress={handleToggleTab}
        />
      </View>
      <TabView
        navigationState={{ index, routes }}
        onIndexChange={setIndex}
        renderScene={renderScene}
      />
    </View>
  );
}
