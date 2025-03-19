#include "RNCTabViewShadowNode.h"

namespace facebook::react {

extern const char RNCTabViewComponentName[] = "RNCTabView";

void RNCTabViewShadowNode::updateStateIfNeeded() {
  ensureUnsealed();
  const auto &stateData = getStateData();
  auto frameSize = stateData.frameSize;
  
  for (auto &child : getLayoutableChildNodes()) {
    auto yogaChild = dynamic_cast<YogaLayoutableShadowNode*>(child);
    if (!yogaChild->getSealed() && yogaChild->getLayoutMetrics().frame.size != frameSize) {
      yogaChild->setSize(frameSize);
    }
  }
};

}
