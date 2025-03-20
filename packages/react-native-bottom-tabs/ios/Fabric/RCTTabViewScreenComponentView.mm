#ifdef RCT_NEW_ARCH_ENABLED
#import "RCTTabViewScreenComponentView.h"

#import <react/renderer/components/RNCTabView/RNCTabViewScreenComponentDescriptor.h>
#import <react/renderer/components/RNCTabView/EventEmitters.h>
#import <react/renderer/components/RNCTabView/Props.h>
#import <react/renderer/components/RNCTabView/RCTComponentViewHelpers.h>

#import <React/RCTFabricComponentsPlugins.h>

#if TARGET_OS_OSX
typedef NSView PlatformView;
#else
typedef UIView PlatformView;
#endif


using namespace facebook::react;

@interface RCTTabViewScreenComponentView () <RCTRNCTabViewScreenViewProtocol> {
}

@end

@implementation RCTTabViewScreenComponentView {
  RNCTabViewScreenShadowNode::ConcreteState::Shared _state;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<RNCTabViewScreenComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const RNCTabViewScreenProps>();
    _props = defaultProps;
  }

  return self;
}

- (void)updateState:(const facebook::react::State::Shared &)state oldState:(const facebook::react::State::Shared &)oldState {
  _state = std::static_pointer_cast<const RNCTabViewScreenShadowNode::ConcreteState>(state);
}

- (void)updateFrameSize:(CGSize)size
{
  _state->updateState(RNCTabViewScreenState({size.width, size.height}));
}

@end

Class<RCTComponentViewProtocol> RNCTabViewScreenCls(void)
{
  return RCTTabViewScreenComponentView.class;
}

#endif // RCT_NEW_ARCH_ENABLED



