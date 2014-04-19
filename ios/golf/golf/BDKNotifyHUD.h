#import <UIKit/UIKit.h>

#define kBDKNotifyHUDDefaultWidth 130.0f
#define kBDKNotifyHUDDefaultHeight 100.0f

@interface BDKNotifyHUD : UIView
{
    //add
//    MBProgressHUDMode mode;
//    MBProgressHUDAnimation animationType;
	
	SEL methodForExecution;
	id targetForExecution;
	id objectForExecution;
	BOOL useAnimation;
	
    float yOffset;
    float xOffset;
	
	float width;
	float height;
	
	float margin;
	
	BOOL dimBackground;
	
	BOOL taskInProgress;
	float graceTime;
	float minShowTime;
	NSTimer *graceTimer;
	NSTimer *minShowTimer;
	NSDate *showStarted;
	
	UIView *indicator;
	UILabel *label;
	UILabel *detailsLabel;
	
	float progress;
	
	NSString *labelText;
	NSString *detailsLabelText;
	float opacity;
	UIFont *labelFont;
	UIFont *detailsLabelFont;
	
    BOOL isFinished;
	BOOL removeFromSuperViewOnHide;
    
    //add
}

@property (nonatomic) CGFloat destinationOpacity;
@property (nonatomic) CGFloat currentOpacity;
@property (nonatomic) UIImage *image;
@property (nonatomic) CGFloat roundness;
@property (nonatomic) BOOL bordered;
@property (nonatomic) BOOL isAnimating;

@property (strong, nonatomic) UIColor *borderColor;
@property (strong, nonatomic) NSString *text;
//add
//@property (assign) MBProgressHUDMode mode;
//@property (assign) MBProgressHUDAnimation animationType;
@property (assign) float width;
@property (assign) float height;
@property (copy) NSString *labelText;
@property (copy) NSString *detailsLabelText;
@property (assign) float opacity;
@property (assign) float xOffset;
@property (assign) float yOffset;
@property (assign) float margin;
@property (assign) BOOL dimBackground;
@property (assign) float graceTime;
@property (assign) float minShowTime;
@property (assign) BOOL taskInProgress;
@property (assign) BOOL removeFromSuperViewOnHide;
//add

+ (id)notifyHUDWithImage:(UIImage *)image text:(NSString *)text;
- (id)initWithImage:(UIImage *)image text:(NSString *)text;

- (void)presentWithDuration:(CGFloat)duration speed:(CGFloat)speed inView:(UIView *)view completion:(void (^)(void))completion;

@end
