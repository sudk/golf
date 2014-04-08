//
//  GolfMainViewController.h
//  iaptest
//
//  Created by mahh on 13-7-18.
//  Copyright (c) 2013年 mahh. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GolfTabBarController.h"
#import "SearchCourtHomeViewController.h"
#import "SpecialHomeViewController.h"
#import "RecommendHomeViewController.h"
#import "MyAccountHomeViewController.h"

#define kSCNavBarImageTag 10000001


@interface GolfMainViewController : UIViewController
{
    GolfTabBarController *_golfTabBarController;
    SearchCourtHomeViewController *_searchCourtHomeViewController;
    SpecialHomeViewController *_specialHomeViewController;
    RecommendHomeViewController *_recommendHomeViewController;
    MyAccountHomeViewController *_myAccountHomeViewController;
    
}
@property(nonatomic,retain)GolfTabBarController *golfTabBarController;
@property(nonatomic,retain)SearchCourtHomeViewController *searchCourtHomeViewController;
@property(nonatomic,retain)SpecialHomeViewController *specialHomeViewController;
@property(nonatomic,retain)RecommendHomeViewController *recommendHomeViewController;
@property(nonatomic,retain)MyAccountHomeViewController *myAccountHomeViewController;

@end
