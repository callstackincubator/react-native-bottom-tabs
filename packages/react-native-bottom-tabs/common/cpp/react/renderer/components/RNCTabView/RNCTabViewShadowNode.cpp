#include <react/renderer/components/RNCTabView/RNCTabViewShadowNode.h>

namespace facebook::react {

extern const char RNCTabViewComponentName[] = "RNCTabView";

void RNCTabViewShadowNode::setImageLoader(
    std::weak_ptr<void> imageLoader) {
  getStateDataMutable().setImageLoader(imageLoader);
}

RNCTabViewShadowNode::StateData &
RNCTabViewShadowNode::getStateDataMutable() {
  ensureUnsealed();
  return const_cast<RNCTabViewShadowNode::StateData &>(getStateData());
}


} // namespace facebook::react
