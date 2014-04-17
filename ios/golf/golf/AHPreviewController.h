//
//  AHPreviewViewController.h
//  ZYQAssetPickerControllerDemo
//
//  Created by mahh on 14-3-28.
//  Copyright (c) 2014年 heroims. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"

@interface AHPreviewController : BaseViewController<UIGestureRecognizerDelegate,UIScrollViewDelegate>
{
    NSArray *_previewImgArray;
    NSMutableArray *_previewImgMutableArray;
    UIButton *_selectBtn;
    UIToolbar *_topBar;
    UIButton *_numBtn;
    UIToolbar *_bottomBar;
    UIScrollView *_previewScroll;
    float _selectBtnFrameY;
    int currentPage;
    
    
}
@property(strong,nonatomic)NSArray *previewImgArray;
@property(strong,nonatomic)NSMutableArray *previewImgMutableArray;
@property(strong,nonatomic)UIButton *selectBtn;
@property(strong,nonatomic)UIToolbar *topBar;
@property(strong,nonatomic)UIButton *numBtn;
@property(strong,nonatomic)UIToolbar *bottomBar;
@property(strong,nonatomic)NSMutableDictionary *statusDic;
@property(strong,nonatomic)UIScrollView *zoomScrollView;
@property(strong,nonatomic)UIImageView *zoomImgv;
@property(strong,nonatomic)UIPageControl *pageControl;
@property(strong,nonatomic)NSString *previewTitle;
@end
