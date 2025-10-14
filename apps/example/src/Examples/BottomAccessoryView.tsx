import TabView, { SceneMap } from 'react-native-bottom-tabs';
import { useState } from 'react';
import { Article } from '../Screens/Article';
import { Albums } from '../Screens/Albums';
import { Contacts } from '../Screens/Contacts';
import { Text, View, type TextStyle, type ViewStyle } from 'react-native';

const bottomAccessoryViewStyle: ViewStyle = {
  width: '100%',
  height: '100%',
  justifyContent: 'center',
  alignItems: 'center',
};

const textStyle: TextStyle = { textAlign: 'center' };

const renderScene = SceneMap({
  article: Article,
  albums: Albums,
  contacts: Contacts,
});

const isAndroid = false;
export default function BottomAccessoryView() {
  const [index, setIndex] = useState(0);
  const [routes] = useState([
    {
      key: 'article',
      title: 'Article',
      focusedIcon: isAndroid
        ? require('../../assets/icons/article_dark.png')
        : { sfSymbol: 'document.fill' },
      unfocusedIcon: isAndroid
        ? require('../../assets/icons/chat_dark.png')
        : { sfSymbol: 'document' },
      badge: '!',
    },
    {
      key: 'albums',
      title: 'Albums',
      focusedIcon: isAndroid
        ? require('../../assets/icons/grid_dark.png')
        : { sfSymbol: 'square.grid.3x2.fill' },
      badge: '5',
    },
    {
      key: 'contacts',
      focusedIcon: isAndroid
        ? require('../../assets/icons/person_dark.png')
        : { sfSymbol: 'person.fill' },
      title: 'Contacts',
      role: 'search',
    },
  ]);

  const [bottomAccessoryDimensions, setBottomAccessoryDimensions] = useState({
    width: 0,
    height: 0,
  });

  return (
    <TabView
      sidebarAdaptable
      minimizeBehavior="onScrollDown"
      navigationState={{ index, routes }}
      onIndexChange={setIndex}
      renderScene={renderScene}
      renderBottomAccessoryView={({ placement }) => (
        <View
          style={bottomAccessoryViewStyle}
          onLayout={(e) => setBottomAccessoryDimensions(e.nativeEvent.layout)}
        >
          <Text style={textStyle}>
            Placement: {placement}. Dimensions:{' '}
            {bottomAccessoryDimensions.width}x{bottomAccessoryDimensions.height}
          </Text>
        </View>
      )}
    />
  );
}
