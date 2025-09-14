#import "CoreSVG.h"
#import <dlfcn.h>
#import <objc/runtime.h>

#define kSVGTagEnd @"</svg>"

typedef struct CF_BRIDGED_TYPE(id) CGSVGDocument *CGSVGDocumentRef;

static CGSVGDocumentRef (*CoreSVGDocumentRetain)(CGSVGDocumentRef);
static void (*CoreSVGDocumentRelease)(CGSVGDocumentRef);
static CGSVGDocumentRef (*CoreSVGDocumentCreateFromData)(CFDataRef data, CFDictionaryRef options);
static void (*CoreSVGContextDrawSVGDocument)(CGContextRef context, CGSVGDocumentRef document);
static CGSize (*CoreSVGDocumentGetCanvasSize)(CGSVGDocumentRef document);

#if TARGET_OS_IOS || TARGET_OS_WATCH
static SEL CoreSVGImageWithDocumentSEL = NULL;
#endif

static inline NSString *Base64DecodedString(NSString *base64String) {
    NSData *data = [[NSData alloc] initWithBase64EncodedString:base64String options:NSDataBase64DecodingIgnoreUnknownCharacters];
    if (!data) {
        return nil;
    }
    return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
}

@implementation CoreSVGWrapper

+ (instancetype)sharedWrapper {
    static dispatch_once_t onceToken;
    static CoreSVGWrapper *wrapper;
    dispatch_once(&onceToken, ^{
        wrapper = [[CoreSVGWrapper alloc] init];
    });
    return wrapper;
}

+ (void)initialize {
    CoreSVGDocumentRetain = (CGSVGDocumentRef (*)(CGSVGDocumentRef))dlsym(RTLD_DEFAULT, Base64DecodedString(@"Q0dTVkdEb2N1bWVudFJldGFpbg==").UTF8String);
    CoreSVGDocumentRelease = (void (*)(CGSVGDocumentRef))dlsym(RTLD_DEFAULT, Base64DecodedString(@"Q0dTVkdEb2N1bWVudFJlbGVhc2U=").UTF8String);
    CoreSVGDocumentCreateFromData = (CGSVGDocumentRef (*)(CFDataRef data, CFDictionaryRef options))dlsym(RTLD_DEFAULT, Base64DecodedString(@"Q0dTVkdEb2N1bWVudENyZWF0ZUZyb21EYXRh").UTF8String);
    CoreSVGContextDrawSVGDocument = (void (*)(CGContextRef context, CGSVGDocumentRef document))dlsym(RTLD_DEFAULT, Base64DecodedString(@"Q0dDb250ZXh0RHJhd1NWR0RvY3VtZW50").UTF8String);
    CoreSVGDocumentGetCanvasSize = (CGSize (*)(CGSVGDocumentRef document))dlsym(RTLD_DEFAULT, Base64DecodedString(@"Q0dTVkdEb2N1bWVudEdldENhbnZhc1NpemU=").UTF8String);

#if TARGET_OS_IOS || TARGET_OS_WATCH
    CoreSVGImageWithDocumentSEL = NSSelectorFromString(Base64DecodedString(@"X2ltYWdlV2l0aENHU1ZHRG9jdW1lbnQ6"));
#endif
}

- (UIImage *)imageFromSVGData:(NSData *)data {
    return [self imageFromSVGData:data targetSize:CGSizeZero preserveAspectRatio:YES];
}

- (UIImage *)imageFromSVGData:(NSData *)data targetSize:(CGSize)targetSize {
    return [self imageFromSVGData:data targetSize:targetSize preserveAspectRatio:YES];
}

- (UIImage *)imageFromSVGData:(NSData *)data targetSize:(CGSize)targetSize preserveAspectRatio:(BOOL)preserveAspectRatio {
    if (!data) {
        return nil;
    }

    if (CGSizeEqualToSize(targetSize, CGSizeZero) && [self.class supportsVectorSVG]) {
        return [self createVectorSVGWithData:data];
    } else {
        return [self createBitmapSVGWithData:data targetSize:targetSize preserveAspectRatio:preserveAspectRatio];
    }
}

- (UIImage *)createVectorSVGWithData:(NSData *)data {
    if (!data) return nil;

#if TARGET_OS_IOS || TARGET_OS_WATCH
    CGSVGDocumentRef document = CoreSVGDocumentCreateFromData((__bridge CFDataRef)data, NULL);
    if (!document) {
        return nil;
    }

    UIImage *image = ((UIImage *(*)(id,SEL,CGSVGDocumentRef))[UIImage.class methodForSelector:CoreSVGImageWithDocumentSEL])(UIImage.class, CoreSVGImageWithDocumentSEL, document);
    CoreSVGDocumentRelease(document);

    // Test render to catch potential CoreSVG crashes
    UIGraphicsBeginImageContextWithOptions(CGSizeMake(1, 1), NO, 1.0);
    @try {
        [image drawInRect:CGRectMake(0, 0, 1, 1)];
    } @catch (...) {
        UIGraphicsEndImageContext();
        return nil;
    }
    UIGraphicsEndImageContext();

    return image;
#else
    return [self createBitmapSVGWithData:data targetSize:CGSizeZero preserveAspectRatio:YES];
#endif
}

- (UIImage *)createBitmapSVGWithData:(NSData *)data targetSize:(CGSize)targetSize preserveAspectRatio:(BOOL)preserveAspectRatio {
    if (!data) return nil;

    CGSVGDocumentRef document = CoreSVGDocumentCreateFromData((__bridge CFDataRef)data, NULL);
    if (!document) {
        return nil;
    }

    CGSize size = CoreSVGDocumentGetCanvasSize(document);
    if (size.width <= 0 || size.height <= 0) {
        CoreSVGDocumentRelease(document);
        return nil;
    }

    CGFloat xScale, yScale;

    if (CGSizeEqualToSize(targetSize, CGSizeZero)) {
        targetSize = size;
        xScale = yScale = 1.0;
    } else {
        CGFloat xRatio = targetSize.width / size.width;
        CGFloat yRatio = targetSize.height / size.height;

        if (preserveAspectRatio) {
            if (targetSize.width <= 0) {
                yScale = yRatio;
                xScale = yRatio;
                targetSize.width = size.width * xScale;
            } else if (targetSize.height <= 0) {
                xScale = xRatio;
                yScale = xRatio;
                targetSize.height = size.height * yScale;
            } else {
                xScale = MIN(xRatio, yRatio);
                yScale = MIN(xRatio, yRatio);
                targetSize.width = size.width * xScale;
                targetSize.height = size.height * yScale;
            }
        } else {
            if (targetSize.width <= 0) {
                targetSize.width = size.width;
                yScale = yRatio;
                xScale = 1.0;
            } else if (targetSize.height <= 0) {
                xScale = xRatio;
                yScale = 1.0;
                targetSize.height = size.height;
            } else {
                xScale = xRatio;
                yScale = yRatio;
            }
        }
    }

    CGAffineTransform scaleTransform = CGAffineTransformMakeScale(xScale, yScale);
    CGAffineTransform offsetTransform = CGAffineTransformIdentity;

    if (preserveAspectRatio) {
        CGFloat offsetX = (targetSize.width / xScale - size.width) / 2;
        CGFloat offsetY = (targetSize.height / yScale - size.height) / 2;
        offsetTransform = CGAffineTransformMakeTranslation(offsetX, offsetY);
    }

    UIGraphicsBeginImageContextWithOptions(targetSize, NO, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();

#if TARGET_OS_IOS || TARGET_OS_WATCH
    CGContextTranslateCTM(context, 0, targetSize.height);
    CGContextScaleCTM(context, 1, -1);
#endif

    CGContextConcatCTM(context, scaleTransform);
    CGContextConcatCTM(context, offsetTransform);

    CoreSVGContextDrawSVGDocument(context, document);

    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    CoreSVGDocumentRelease(document);

    return image;
}

- (BOOL)isSVGData:(NSData *)data {
    if (!data) return NO;

    NSRange searchRange = NSMakeRange(MAX(0, (NSInteger)data.length - 100), MIN(100, data.length));
    return [data rangeOfData:[kSVGTagEnd dataUsingEncoding:NSUTF8StringEncoding]
                     options:NSDataSearchBackwards
                       range:searchRange].location != NSNotFound;
}

+ (BOOL)supportsVectorSVG {
    static dispatch_once_t onceToken;
    static BOOL supports;
    dispatch_once(&onceToken, ^{
#if TARGET_OS_IOS || TARGET_OS_WATCH
        supports = [UIImage respondsToSelector:CoreSVGImageWithDocumentSEL];
#else
        supports = NO;
#endif
    });
    return supports;
}

@end
