#ifdef __cplusplus

#pragma once
#include <react/renderer/graphics/Size.h>
#ifdef ANDROID
#include <folly/dynamic.h>
#endif

namespace facebook::react {

class RNCTabViewScreenState
{
public:
  RNCTabViewScreenState() = default;
  RNCTabViewScreenState(Size frameSize): frameSize(frameSize) {};

  Size frameSize;

#ifdef ANDROID
  RNCTabViewScreenState(RNCTabViewScreenState const &previousState, folly::dynamic data)
    : frameSize((Float)data["width"].getDouble(), (Float)data["height"].getDouble()) {};
  folly::dynamic getDynamic() const {
    return {};
  };
#endif
};

}

#endif
