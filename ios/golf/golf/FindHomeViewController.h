//
//  FindHomeViewController.h
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "StyledPageControl.h"

@interface FindHomeViewController : BaseViewController<UIScrollViewDelegate>
@property(nonatomic,strong)NSArray *adImgArray;
@property(nonatomic,strong)UIScrollView *adScrollView;
@property(nonatomic,strong)StyledPageControl *adPageControl;
@property(strong,nonatomic)NSMutableArray *adImgvArray;
@property(nonatomic,assign)int timeCount;
@end
