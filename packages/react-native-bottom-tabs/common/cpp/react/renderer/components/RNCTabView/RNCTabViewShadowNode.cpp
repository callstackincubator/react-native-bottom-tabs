#include "RNCTabViewShadowNode.h"

namespace facebook::react {

extern const char RNCTabViewComponentName[] = "RNCTabView";

void RNCTabViewShadowNode::updateStateIfNeeded() {
//  ensureUnsealed();
//  const auto &stateData = getStateData();
//  auto frameSize = stateData.frameSize;
//  
//  for (auto &child : getLayoutableChildNodes()) {
//    auto yogaChild = dynamic_cast<YogaLayoutableShadowNode*>(child);
//    yogaChild->getState();
//    if (!yogaChild->getSealed() && yogaChild->getLayoutMetrics().frame.size != frameSize) {
//      yogaChild->setSize(frameSize);
//    }
//  }
  
};

void RNCTabViewShadowNode::layout(facebook::react::LayoutContext layoutContext) {
  YogaLayoutableShadowNode::layout(layoutContext);
  
  // Now adjust children as needed
  const auto &stateData = getStateData();
  auto frameSize = stateData.frameSize;
  
  for (auto childNode : getLayoutableChildNodes()) {
    childNode->ensureUnsealed();
    auto yogaChild = dynamic_cast<YogaLayoutableShadowNode*>(childNode);
    yogaChild->setSize(frameSize);
    
  }
}

}
