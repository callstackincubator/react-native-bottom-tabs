#if TARGET_OS_OSX
#import <AppKit/AppKit.h>
typedef NSImage PlatformImage;
#else
#import <UIKit/UIKit.h>
typedef UIImage PlatformImage;
#endif


@interface CoreSVGWrapper : NSObject

+ (instancetype)shared;

- (PlatformImage *)imageFromSVGData:(NSData *)data;

+ (BOOL)isSVGData:(NSData *)data;
+ (BOOL)supportsVectorSVGImage;

@end
