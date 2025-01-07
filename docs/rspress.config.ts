import * as path from 'path';
import { defineConfig } from 'rspress/config';
import { pluginOpenGraph } from 'rsbuild-plugin-open-graph';
import { pluginCallstackTheme } from '@callstack/rspress-theme/plugin';

export default defineConfig({
  root: path.join(__dirname, 'docs'),
  base: '/react-native-bottom-tabs/',
  title: 'React Native Bottom Tabs',
  description: 'React Native Bottom Tabs Documentation',
  logoText: 'React Native Bottom Tabs',
  icon: '/img/phone.png',
  logo: '/img/phone.png',
  themeConfig: {
    enableContentAnimation: true,
    enableAppearanceAnimation: false,
    socialLinks: [
      {
        icon: 'github',
        mode: 'link',
        content:
          'https://github.com/callstackincubator/react-native-bottom-tabs',
      },
    ],
  },
  plugins: [
    // @ts-ignore
    pluginCallstackTheme(),
  ],
  builderConfig: {
    plugins: [
      pluginOpenGraph({
        title: 'React Native Bottom Tabs',
        type: 'website',
        url: 'https://callstackincubator.github.io/react-native-bottom-tabs/',
        description: 'Native Bottom Tabs for React Native',
      }),
    ],
  },
});
