#ifdef __cplusplus

#pragma once

#include <react/renderer/components/RNCTabView/EventEmitters.h>
#include <react/renderer/components/RNCTabView/Props.h>
#include <react/renderer/components/RNCTabView/RNCTabViewState.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>
#include <jsi/jsi.h>

namespace facebook::react {

JSI_EXPORT extern const char RNCTabViewComponentName[];

/*
 * `ShadowNode` for <RNCTabView> component.
 */
class JSI_EXPORT RNCTabViewShadowNode final: public ConcreteViewShadowNode<
RNCTabViewComponentName,
RNCTabViewProps,
RNCTabViewEventEmitter,
RNCTabViewState> {
  
public:
  using ConcreteViewShadowNode::ConcreteViewShadowNode;
  using StateData = ConcreteViewShadowNode::ConcreteStateData;
  
  void setImageLoader(std::weak_ptr<void> imageLoader);
  
  StateData &getStateDataMutable();
};

}

#endif
