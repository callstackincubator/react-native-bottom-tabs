#ifdef RCT_NEW_ARCH_ENABLED
#import "RCTBottomAccessoryComponentView.h"

#import <react/renderer/components/RNCTabView/ComponentDescriptors.h>
#import <react/renderer/components/RNCTabView/EventEmitters.h>
#import <react/renderer/components/RNCTabView/Props.h>
#import <react/renderer/components/RNCTabView/RCTComponentViewHelpers.h>

#import <React/RCTFabricComponentsPlugins.h>

#if __has_include("react_native_bottom_tabs/react_native_bottom_tabs-Swift.h")
#import "react_native_bottom_tabs/react_native_bottom_tabs-Swift.h"
#else
#import "react_native_bottom_tabs-Swift.h"
#endif

using namespace facebook::react;

@implementation RCTBottomAccessoryComponentView

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<BottomAccessoryViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const BottomAccessoryViewProps>();
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &oldViewProps = *std::static_pointer_cast<BottomAccessoryViewProps const>(oldProps);
  const auto &newViewProps = *std::static_pointer_cast<BottomAccessoryViewProps const>(props);

  [super updateProps:props oldProps:oldProps];
}


Class<RCTComponentViewProtocol> BottomAccessoryViewCls(void)
{
  return RCTBottomAccessoryComponentView.class;
}

@end

#endif
