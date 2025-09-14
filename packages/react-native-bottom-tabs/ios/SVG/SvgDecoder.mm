#import "SvgDecoder.h"
#import "CoreSVG.h"

@implementation SvgDecoder

RCT_EXPORT_MODULE()

- (BOOL)canDecodeImageData:(NSData *)imageData
{
#if TARGET_OS_OSX
  return NO;
#endif
  
  if (!imageData || imageData.length == 0) {
    return NO;
  }

  NSString *dataString = [[NSString alloc] initWithData:imageData encoding:NSUTF8StringEncoding];

  if (!dataString) {
    return NO;
  }

  NSString *lowercaseString = [dataString lowercaseString];
  BOOL containsSVGTag = [lowercaseString containsString:@"<svg"];
  BOOL containsXMLDeclaration = [lowercaseString containsString:@"<?xml"];
  BOOL containsSVGNamespace = [lowercaseString containsString:@"http://www.w3.org/2000/svg"];

  return containsSVGTag || (containsXMLDeclaration && containsSVGNamespace);
}


- (RCTImageLoaderCancellationBlock)decodeImageData:(NSData *)imageData
                                              size:(CGSize)size
                                             scale:(CGFloat)scale
                                        resizeMode:(RCTResizeMode)resizeMode
                                 completionHandler:(RCTImageLoaderCompletionBlock)completionHandler
{
#if !TARGET_OS_OSX
  UIImage *image = [[CoreSVGWrapper alloc] imageFromSVGData:imageData targetSize:size];

  if (image) {
    completionHandler(nil, image);
  } else {
    NSError *error = [NSError errorWithDomain:@"SVGDecoderErrorDomain"
                                         code:2
                                     userInfo:@{NSLocalizedDescriptionKey: @"Failed to render SVG to image"}];
    completionHandler(error, nil);
  }
  return ^{};
#else
  return ^{};
#endif
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeSVGDecoderSpecJSI>(params);
}

@end
