#import "BDKNotifyHUD.h"
#import <QuartzCore/QuartzCore.h>

#define kBDKNotifyHUDDefaultRoundness    10.0f
#define kBDKNotifyHUDDefaultOpacity      0.75f
#define kBDKNotifyHUDDefaultPadding      10.0f
#define kBDKNotifyHUDDefaultInnerPadding 6.0f

@implementation NSObject (PerformBlockAfterDelay)

- (void)performBlock:(void (^)(void))block afterDelay:(NSTimeInterval)delay {
    block = [block copy];
    [self performSelector:@selector(fireBlockAfterDelay:) withObject:block afterDelay:delay];
}

- (void)fireBlockAfterDelay:(void (^)(void))block {
    block();
}

@end

@interface BDKNotifyHUD ()

@property (strong, nonatomic) UIView *backgroundView;
@property (strong, nonatomic) UIImageView *imageView;
@property (strong, nonatomic) UILabel *textLabel;

- (void)recalculateHeight;
- (void)adjustTextLabel:(UILabel *)label;
- (void)fadeAfter:(CGFloat)duration speed:(CGFloat)speed completion:(void (^)(void))completion;

@end

@implementation BDKNotifyHUD
//add
@synthesize labelText;
@synthesize detailsLabelText;
@synthesize opacity;
@synthesize xOffset;
@synthesize yOffset;
@synthesize margin;
@synthesize dimBackground;
@synthesize graceTime;
@synthesize minShowTime;
@synthesize taskInProgress;
@synthesize removeFromSuperViewOnHide;
@synthesize width;
@synthesize height;
//add


#pragma mark - Lifecycle

+ (id)notifyHUDWithImage:(UIImage *)image text:(NSString *)text {
    return [[[self alloc] initWithImage:image text:text] autorelease];
}

+ (CGRect)defaultFrame {
    return CGRectMake(0, 0, kBDKNotifyHUDDefaultWidth, kBDKNotifyHUDDefaultHeight);
}

- (id)initWithImage:(UIImage *)image text:(NSString *)text {
    if ((self = [self initWithFrame:[self.class defaultFrame]])) {
        self.image = image;
        self.text = text;
        self.roundness = kBDKNotifyHUDDefaultRoundness;
        self.borderColor = [UIColor clearColor];
        self.destinationOpacity = kBDKNotifyHUDDefaultOpacity;
        self.currentOpacity = 0.0f;
        [self addSubview:self.backgroundView];
        [self addSubview:self.imageView];
        [self addSubview:self.textLabel];
        [self recalculateHeight];
    }
    return self;
}

- (void)presentWithDuration:(CGFloat)duration speed:(CGFloat)speed inView:(UIView *)view completion:(void (^)(void))completion {
    self.isAnimating = YES;
    [UIView animateWithDuration:speed animations:^{
        [self setCurrentOpacity:self.destinationOpacity];
    } completion:^(BOOL finished) {
        if (finished) [self fadeAfter:duration speed:speed completion:completion];
    }];
}

- (void)fadeAfter:(CGFloat)duration speed:(CGFloat)speed completion:(void (^)(void))completion {
    [self performBlock:^{
        [UIView animateWithDuration:speed animations:^{
            [self setCurrentOpacity:0.0];
        } completion:^(BOOL finished) {
            if (finished) {
                self.isAnimating = NO;
                if (completion != nil) completion();
            }
        }];
    } afterDelay:duration];
}

#pragma mark - Setters

- (void)setRoundness:(CGFloat)roundness {
    if (_backgroundView != nil) self.backgroundView.layer.cornerRadius = roundness;
    _roundness = roundness;
}

- (void)setBorderColor:(UIColor *)borderColor {
    if (_backgroundView != nil) self.backgroundView.layer.borderColor = [borderColor CGColor];
    _borderColor = borderColor;
}

- (void)setText:(NSString *)text {
    if (_textLabel != nil) {
        self.textLabel.text = text;
        [self adjustTextLabel:self.textLabel];
    }
    _text = text;
}

- (void)setImage:(UIImage *)image {
    if (_imageView != nil) self.imageView.image = image;
    _image = image;
}

- (void)setCurrentOpacity:(CGFloat)currentOpacity {
    self.imageView.alpha = currentOpacity > 0 ? 1.0f : 0.0f;
    self.textLabel.alpha = currentOpacity > 0 ? 1.0f : 0.0f;
    self.backgroundView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:currentOpacity];
    _currentOpacity = currentOpacity;
}

#pragma mark - Getters

- (UIView *)backgroundView {
    if (_backgroundView != nil) return _backgroundView;
    
//    _backgroundView = [[UIView alloc] initWithFrame:self.bounds];
    _backgroundView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width-20, 60)];
//    _backgroundView.layer.cornerRadius = self.roundness;
//    _backgroundView.layer.borderWidth = 1.0f;
//    _backgroundView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.0f];
//    _backgroundView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:1.0f];
    _backgroundView.backgroundColor = [UIColor blackColor];
//    _backgroundView.layer.borderColor = [self.borderColor CGColor];
    
    return _backgroundView;
}

- (UIImageView *)imageView {
    if (_imageView != nil) return _imageView;
    
    _imageView = [[UIImageView alloc] initWithFrame:CGRectZero];
    _imageView.backgroundColor = [UIColor clearColor];
    _imageView.contentMode = UIViewContentModeCenter;
    if (self.image != nil) { 
        _imageView.image = self.image;
        CGRect frame = _imageView.frame;
        frame.size = self.image.size;
        frame.origin = CGPointMake((self.backgroundView.frame.size.width - frame.size.width) / 2, kBDKNotifyHUDDefaultPadding);
        _imageView.frame = frame;
        _imageView.alpha = 0.0f;
    }
    
    return _imageView;
}

- (UILabel *)textLabel {
    if (_textLabel != nil) return _textLabel;
    
    CGRect frame = CGRectMake(0, floorf(CGRectGetMaxY(self.imageView.frame) + kBDKNotifyHUDDefaultInnerPadding),
                              floorf(self.backgroundView.frame.size.width),
                              floorf(self.backgroundView.frame.size.height / 2.0f));
    _textLabel = [[UILabel alloc] initWithFrame:frame];
    _textLabel.font = [UIFont boldSystemFontOfSize:18];
    _textLabel.textColor = [UIColor whiteColor];
    _textLabel.alpha = 0.0f;
    _textLabel.backgroundColor = [UIColor clearColor];
    _textLabel.textAlignment = NSTextAlignmentCenter;
    _textLabel.numberOfLines = 0;
    if (self.text != nil) _textLabel.text = self.text;
    [self adjustTextLabel:_textLabel];
    [self recalculateHeight];
    
    return _textLabel;
}

#pragma mark - UIView
/*
- (void)layoutSubviews {
    [self recalculateHeight];
}
*/

- (void)adjustTextLabel:(UILabel *)label {
    CGRect frame = _textLabel.frame;
    frame.size.width = self.backgroundView.frame.size.width;
    _textLabel.frame = frame;
    [label sizeToFit];
    frame = _textLabel.frame;
    frame.origin.x = floorf((self.backgroundView.frame.size.width - _textLabel.frame.size.width) / 2);
    _textLabel.frame = frame;
}

- (void)recalculateHeight {
    CGRect frame = self.backgroundView.frame;
    frame.size.height = CGRectGetMaxY(self.textLabel.frame) + kBDKNotifyHUDDefaultPadding;
    self.backgroundView.frame = frame;
}

/*
//add

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
	if (self) {
        // Set default values for properties
//        self.animationType = MBProgressHUDAnimationFade;
//        self.mode = MBProgressHUDModeIndeterminate;
        self.labelText = nil;
        self.detailsLabelText = nil;
        self.opacity = 0.8f;
//        self.labelFont = [UIFont boldSystemFontOfSize:LABELFONTSIZE];
//        self.detailsLabelFont = [UIFont boldSystemFontOfSize:LABELDETAILSFONTSIZE];
        self.xOffset = 0.0f;
        self.yOffset = 0.0f;
		self.dimBackground = NO;
		self.margin = 20.0f;
		self.graceTime = 0.0f;
		self.minShowTime = 0.0f;
		self.removeFromSuperViewOnHide = NO;
		
		self.autoresizingMask = UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
		
        // Transparent background
        self.opaque = NO;
        self.backgroundColor = [UIColor clearColor];
		
        // Make invisible for now
        self.alpha = 0.0f;
		
        // Add label
        label = [[UILabel alloc] initWithFrame:self.bounds];
		
        // Add details label
        detailsLabel = [[UILabel alloc] initWithFrame:self.bounds];
		
		taskInProgress = NO;
//		rotationTransform = CGAffineTransformIdentity;
    }
    return self;
}

#pragma mark BG Drawing

- (void)drawRect:(CGRect)rect {
	
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    if (dimBackground) {
        //Gradient colours
        size_t gradLocationsNum = 2;
        CGFloat gradLocations[2] = {0.0f, 1.0f};
        CGFloat gradColors[8] = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.75f};
        CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
        CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, gradColors, gradLocations, gradLocationsNum);
		CGColorSpaceRelease(colorSpace);
        
        //Gradient center
        CGPoint gradCenter= CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
        //Gradient radius
        float gradRadius = MIN(self.bounds.size.width , self.bounds.size.height) ;
        //Gradient draw
        CGContextDrawRadialGradient (context, gradient, gradCenter,
                                     0, gradCenter, gradRadius,
                                     kCGGradientDrawsAfterEndLocation);
		CGGradientRelease(gradient);
    }
    
    // Center HUD
    CGRect allRect = self.bounds;
    // Draw rounded HUD bacgroud rect
    CGRect boxRect = CGRectMake(roundf((allRect.size.width - self.width) / 2) + self.xOffset,
                                roundf((allRect.size.height - self.height) / 2) + self.yOffset, self.width, self.height);
	// Corner radius
	float radius = 10.0f;
	
    CGContextBeginPath(context);
    CGContextSetGrayFillColor(context, 0.0f, self.opacity);
    CGContextMoveToPoint(context, CGRectGetMinX(boxRect) + radius, CGRectGetMinY(boxRect));
    CGContextAddArc(context, CGRectGetMaxX(boxRect) - radius, CGRectGetMinY(boxRect) + radius, radius, 3 * (float)M_PI / 2, 0, 0);
    CGContextAddArc(context, CGRectGetMaxX(boxRect) - radius, CGRectGetMaxY(boxRect) - radius, radius, 0, (float)M_PI / 2, 0);
    CGContextAddArc(context, CGRectGetMinX(boxRect) + radius, CGRectGetMaxY(boxRect) - radius, radius, (float)M_PI / 2, (float)M_PI, 0);
    CGContextAddArc(context, CGRectGetMinX(boxRect) + radius, CGRectGetMinY(boxRect) + radius, radius, (float)M_PI, 3 * (float)M_PI / 2, 0);
    CGContextClosePath(context);
    CGContextFillPath(context);
}
- (id)initWithView:(UIView *)view {
	// Let's check if the view is nil (this is a common error when using the windw initializer above)
	if (!view) {
		[NSException raise:@"MBProgressHUDViewIsNillException"
					format:@"The view used in the NewLoadingView  initializer is nil."];
	}
	id me = [self initWithFrame:view.bounds];
	// We need to take care of rotation ourselfs if we're adding the HUD to a window
	if ([view isKindOfClass:[UIWindow class]]) {
		[self setTransformForCurrentOrientation:NO];
	}
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(deviceOrientationDidChange:)
												 name:UIDeviceOrientationDidChangeNotification object:nil];
	
	return me;
}
*/
//add
 - (void)layoutSubviews {
 CGRect frame = self.bounds;
 
 // Compute HUD dimensions based on indicator size (add margin to HUD border)
     self.margin = 20.0f;
     self.xOffset = 0.0f;
     self.yOffset = 0.0f;
 CGRect indFrame = indicator.bounds;
 self.width = indFrame.size.width + 10 * margin;
 self.height = indFrame.size.height + 3 * margin;
 
 // Position the indicator
 indFrame.origin.x = floorf((frame.size.width - indFrame.size.width) / 2) + self.xOffset;
 indFrame.origin.y = floorf((frame.size.height - indFrame.size.height) / 2) + self.yOffset;
 indicator.frame = indFrame;
 /*
 // Add label if label text was set
 if (nil != self.labelText) {
 // Get size of label text
 
 CGSize dims = [self.labelText sizeWithFont:self.labelFont];
 //        CGSize dims = [self.labelText sizeWithFont:[UIFont fontWithName:@"Arial" size:12]];
 
 // Compute label dimensions based on font metrics if size is larger than max then clip the label width
 float lHeight = dims.height;
 float lWidth;
 if (dims.width <= (frame.size.width - 2 * margin)) {
 lWidth = dims.width;
 }
 else {
 lWidth = frame.size.width - 4 * margin;
 }
 
 // Set label properties
 label.font = self.labelFont;
 label.adjustsFontSizeToFitWidth = NO;
 label.textAlignment = UITextAlignmentCenter;
 label.opaque = NO;
 label.backgroundColor = [UIColor clearColor];
 label.textColor = [UIColor whiteColor];
 label.text = self.labelText;
 
 // Update HUD size
 if (self.width < (lWidth + 2 * margin)) {
 self.width = lWidth + 2 * margin;
 }
 self.height = self.height + lHeight + PADDING;
 
 // Move indicator to make room for the label
 indFrame.origin.y -= (floorf(lHeight / 2 + PADDING / 2));
 indicator.frame = indFrame;
 
 // Set the label position and dimensions
 CGRect lFrame = CGRectMake(floorf((frame.size.width - lWidth) / 2) + xOffset,
 floorf(indFrame.origin.y + indFrame.size.height + PADDING),
 lWidth, lHeight);
 label.frame = lFrame;
 
 [self addSubview:label];
 
 // Add details label delatils text was set
 if (nil != self.detailsLabelText) {
 // Get size of label text
 dims = [self.detailsLabelText sizeWithFont:self.detailsLabelFont];
 
 // Compute label dimensions based on font metrics if size is larger than max then clip the label width
 lHeight = dims.height;
 if (dims.width <= (frame.size.width - 2 * margin)) {
 lWidth = dims.width;
 }
 else {
 lWidth = frame.size.width - 4 * margin;
 }
 
 // Set label properties
 detailsLabel.font = self.detailsLabelFont;
 detailsLabel.adjustsFontSizeToFitWidth = NO;
 detailsLabel.textAlignment = UITextAlignmentCenter;
 detailsLabel.opaque = NO;
 detailsLabel.backgroundColor = [UIColor clearColor];
 detailsLabel.textColor = [UIColor whiteColor];
 detailsLabel.text = self.detailsLabelText;
 
 // Update HUD size
 if (self.width < lWidth) {
 self.width = lWidth + 2 * margin;
 }
 self.height = self.height + lHeight + PADDING;
 
 // Move indicator to make room for the new label
 indFrame.origin.y -= (floorf(lHeight / 2 + PADDING / 2));
 indicator.frame = indFrame;
 
 // Move first label to make room for the new label
 lFrame.origin.y -= (floorf(lHeight / 2 + PADDING / 2));
 label.frame = lFrame;
 
 // Set label position and dimensions
 CGRect lFrameD = CGRectMake(floorf((frame.size.width - lWidth) / 2) + xOffset,
 lFrame.origin.y + lFrame.size.height + PADDING, lWidth, lHeight);
 detailsLabel.frame = lFrameD;
 
 [self addSubview:detailsLabel];
 }
 }*/
}
@end
