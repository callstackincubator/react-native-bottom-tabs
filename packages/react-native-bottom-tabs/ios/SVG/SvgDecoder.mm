#import "SvgDecoder.h"
#import "CoreSVG.h"

@implementation SvgDecoder

RCT_EXPORT_MODULE()

- (BOOL)canDecodeImageData:(NSData *)imageData
{
  return [CoreSVGWrapper isSVGData:imageData];
}

- (RCTImageLoaderCancellationBlock)decodeImageData:(NSData *)imageData
                                              size:(CGSize)size
                                             scale:(CGFloat)scale
                                        resizeMode:(RCTResizeMode)resizeMode
                                 completionHandler:(RCTImageLoaderCompletionBlock)completionHandler
{
  UIImage *image = [CoreSVGWrapper.shared imageFromSVGData:imageData];

  if (image) {
    completionHandler(nil, image);
  } else {
    NSError *error = [NSError errorWithDomain:@"SVGDecoderErrorDomain"
                                         code:2
                                     userInfo:@{NSLocalizedDescriptionKey: @"Failed to render SVG to image"}];
    completionHandler(error, nil);
  }
  return ^{};
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeSVGDecoderSpecJSI>(params);
}

@end
