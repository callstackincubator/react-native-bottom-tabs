// theme/index.tsx
import {
  Button,
  HomeBanner,
  HomeFeature,
  HomeFooter,
  HomeHero,
  LinkCard,
  OutlineCTA,
  PrevNextPage,
  VersionBadge,
} from '@callstack/rspress-theme';
import {
  HomeLayout as RspressHomeLayout,
  Layout as RspressLayout,
} from 'rspress/theme';

// You can customize the default Layout and HomeLayout like this:
const Layout = () => (
  <RspressLayout afterOutline={<OutlineCTA href="https://callstack.com" />} />
);

const HomeLayout = () => (
  <RspressHomeLayout
    afterFeatures={
      <>
        <HomeBanner href="https://callstack.com" />
        <HomeFooter />
      </>
    }
  />
);

// Export your custom layouts and components
export {
  Layout,
  HomeLayout,
  Button,
  PrevNextPage,
  HomeFeature,
  HomeHero,
  LinkCard,
  VersionBadge,
};

// Export the default theme components
export * from 'rspress/theme';
