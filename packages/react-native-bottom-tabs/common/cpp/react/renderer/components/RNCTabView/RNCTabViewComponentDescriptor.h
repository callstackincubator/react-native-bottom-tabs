#ifdef __cplusplus

#pragma once

#include <react/renderer/components/RNCTabView/RNCTabViewShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook::react {

class RNCTabViewComponentDescriptor final : public ConcreteComponentDescriptor<RNCTabViewShadowNode>
{
public:
  RNCTabViewComponentDescriptor(const ComponentDescriptorParameters &parameters)
  : ConcreteComponentDescriptor(parameters) {}
  
  void adopt(ShadowNode &shadowNode) const override {
    react_native_assert(dynamic_cast<RNCTabViewShadowNode *>(&shadowNode));
    
    const auto tabViewShadowNode = dynamic_cast<RNCTabViewShadowNode *>(&shadowNode);
    tabViewShadowNode->updateStateIfNeeded();
    ConcreteComponentDescriptor::adopt(shadowNode);
  }

};

}

#endif
