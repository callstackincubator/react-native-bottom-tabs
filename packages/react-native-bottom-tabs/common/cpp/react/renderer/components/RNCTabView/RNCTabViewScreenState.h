#ifdef __cplusplus

#pragma once
#include <react/renderer/graphics/Size.h>
#ifdef ANDROID
#include <folly/dynamic.h>
#include <react/renderer/mapbuffer/MapBuffer.h>
#include <react/renderer/mapbuffer/MapBufferBuilder.h>
#endif

namespace facebook::react {

class RNCTabViewScreenState
{
public:
  RNCTabViewScreenState() = default;
  RNCTabViewScreenState(Size frameSize): frameSize(frameSize) {};

  Size frameSize;
};

}

#endif
