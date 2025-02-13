import React from 'react';
import { Freeze } from 'react-freeze';

interface FreezeWrapperProps {
  freeze: boolean;
  children: React.ReactNode;
}

// Animation delay for freeze effect.
const ANIMATION_DELAY = 200;

// This component delays the freeze effect by animation delay.
// So that the screen is not frozen immediately causing background flash.
function DelayedFreeze({ freeze, children }: FreezeWrapperProps) {
  // flag used for determining whether freeze should be enabled
  const [freezeState, setFreezeState] = React.useState(false);

  React.useEffect(() => {
    const id = setTimeout(() => {
      setFreezeState(freeze);
    }, ANIMATION_DELAY);

    return () => {
      clearTimeout(id);
    };
  }, [freeze]);

  return <Freeze freeze={freeze ? freezeState : false}>{children}</Freeze>;
}

export default DelayedFreeze;
