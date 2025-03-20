#ifdef __cplusplus

#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/RNCTabView/RNCTabViewScreenState.h>
#include <react/renderer/components/RNCTabView/Props.h>
#include <react/renderer/components/RNCTabView/EventEmitters.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>
#include <react/renderer/core/LayoutContext.h>
#include <react/renderer/core/LayoutConstraints.h>

namespace facebook::react {

JSI_EXPORT extern const char RNCTabViewScreenComponentName[];

/*
* `ShadowNode` for <RNCTabViewScreen> component.
*/
class JSI_EXPORT RNCTabViewScreenShadowNode final
: public ConcreteViewShadowNode<
RNCTabViewScreenComponentName,
RNCTabViewScreenProps,
RNCTabViewScreenEventEmitter,
RNCTabViewScreenState>
{
public:
  using ConcreteViewShadowNode::ConcreteViewShadowNode;
};
}

#endif
