#import <UIKit/UIKit.h>

@interface CoreSVGWrapper : NSObject

+ (instancetype)sharedWrapper;

- (UIImage *)imageFromSVGData:(NSData *)data;
- (UIImage *)imageFromSVGData:(NSData *)data targetSize:(CGSize)targetSize;
- (UIImage *)imageFromSVGData:(NSData *)data targetSize:(CGSize)targetSize preserveAspectRatio:(BOOL)preserveAspectRatio;

- (BOOL)isSVGData:(NSData *)data;
+ (BOOL)supportsVectorSVG;

@end
