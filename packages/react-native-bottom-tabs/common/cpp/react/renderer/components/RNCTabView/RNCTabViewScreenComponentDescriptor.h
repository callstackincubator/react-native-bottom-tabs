#ifdef __cplusplus

#pragma once

#include <react/renderer/components/RNCTabView/RNCTabViewScreenShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook::react {

class RNCTabViewScreenComponentDescriptor final : public ConcreteComponentDescriptor<RNCTabViewScreenShadowNode>
{
public:
  RNCTabViewScreenComponentDescriptor(const ComponentDescriptorParameters &parameters)
  : ConcreteComponentDescriptor(parameters) {}

  
  /**
   Retrieve shadow node's state and update it's layout size accordingly.
   This is needed because we need to accomodate for bottom bar / sidebar size.
   */
  void adopt(ShadowNode &shadowNode) const override {
    auto& layoutableShadowNode = static_cast<RNCTabViewScreenShadowNode&>(shadowNode);
    auto& stateData = layoutableShadowNode.getStateData();
    
    layoutableShadowNode.setSize(stateData.frameSize);
    
    ConcreteComponentDescriptor::adopt(shadowNode);
  }
};

}

#endif
