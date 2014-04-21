//
//  LoadIngView.h
//  aiguang
//
//  Created by zhen cheng on 11-11-30.
//  Copyright (c) 2011年 aibang.com. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CommonCrypto/CommonDigest.h>

@protocol NewLoadingViewDelegate;;
typedef enum {
    MBProgressHUDModeIndeterminate,
	MBProgressHUDModeDeterminate,
	MBProgressHUDModeCustomView
} MBProgressHUDMode;

typedef enum {
    MBProgressHUDAnimationFade,
    MBProgressHUDAnimationZoom
} MBProgressHUDAnimation;
@interface NewLoadingView : UIView {
	
	MBProgressHUDMode mode;
    MBProgressHUDAnimation animationType;
	
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
	
	id<NewLoadingViewDelegate> delegate;
	NSString *labelText;
	NSString *detailsLabelText;
	float opacity;
	UIFont *labelFont;
	UIFont *detailsLabelFont;
	
    BOOL isFinished;
	BOOL removeFromSuperViewOnHide;
	
	UIView *customView;
	
	CGAffineTransform rotationTransform;
}


+ (NewLoadingView *)showHUDAddedTo:(UIView *)view animated:(BOOL)animated;
+ (BOOL)hideHUDForView:(UIView *)view animated:(BOOL)animated;
+(NSString *)MD5With:(NSString *)mdString;
- (id)initWithWindow:(UIWindow *)window;
- (id)initWithView:(UIView *)view;
@property (retain) UIView *customView;
@property (assign) MBProgressHUDMode mode;
@property (assign) MBProgressHUDAnimation animationType;
@property (assign) id<NewLoadingViewDelegate> delegate;
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
@property (retain) UIFont* labelFont;
@property (retain) UIFont* detailsLabelFont;
@property (assign) float progress;
- (void)show:(BOOL)animated;
- (void)hide:(BOOL)animated;
- (void)hide:(BOOL)animated afterDelay:(NSTimeInterval)delay;
- (void)showWhileExecuting:(SEL)method onTarget:(id)target withObject:(id)object animated:(BOOL)animated;

@end
@protocol NewLoadingViewDelegate <NSObject>

@optional

- (void)hudWasHidden:(NewLoadingView *)hud;

- (void)hudWasHidden __attribute__ ((deprecated)); 

@end

@interface MBRoundProgressView : UIView {
@private
    float _progress;
}


@property (nonatomic, assign) float progress;

@end
