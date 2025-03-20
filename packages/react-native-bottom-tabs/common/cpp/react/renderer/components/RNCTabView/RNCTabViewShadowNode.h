#ifdef __cplusplus

#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/RNCTabView/RNCTabViewState.h>
#include <react/renderer/components/RNCTabView/Props.h>
#include <react/renderer/components/RNCTabView/EventEmitters.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>
#include <react/renderer/core/LayoutContext.h>
#include <react/renderer/core/LayoutConstraints.h>

namespace facebook::react {

JSI_EXPORT extern const char RNCTabViewComponentName[];

/*
* `ShadowNode` for <RNCTabView> component.
*/
class JSI_EXPORT RNCTabViewShadowNode final
: public ConcreteViewShadowNode<
RNCTabViewComponentName,
RNCTabViewProps,
RNCTabViewEventEmitter,
RNCTabViewState>
{
public:
  using ConcreteViewShadowNode::ConcreteViewShadowNode;
  
  void updateStateIfNeeded();
  
#pragma mark - ShadowNode overrides
  
  void layout(LayoutContext layoutContext) override;
};
}

#endif
