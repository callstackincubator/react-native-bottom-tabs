#ifdef __cplusplus

#pragma once

#include <react/renderer/components/RNCTabView/RNCTabViewShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>
#include <react/renderer/componentregistry/ComponentDescriptorProviderRegistry.h>

namespace facebook::react {

class RNCTabViewComponentDescriptor final
    : public ConcreteComponentDescriptor<RNCTabViewShadowNode> {
 public:
    using ConcreteComponentDescriptor::ConcreteComponentDescriptor;

  void adopt(ShadowNode& shadowNode) const override {
    ConcreteComponentDescriptor::adopt(shadowNode);

#if !defined(ANDROID)
    auto &tabViewShadowNode =
        static_cast<RNCTabViewShadowNode&>(shadowNode);

    std::weak_ptr<void> imageLoader =
        contextContainer_->at<std::shared_ptr<void>>("RCTImageLoader");
    tabViewShadowNode.setImageLoader(imageLoader);
#endif
  }
};


} // namespace facebook::react

#endif
