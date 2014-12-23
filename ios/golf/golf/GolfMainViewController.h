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
#import "FindHomeViewController.h"
#import "RecommendHomeViewController.h"
#import "MyAccountHomeViewController.h"

#define kSCNavBarImageTag 10000001


@interface GolfMainViewController : UIViewController
{
    GolfTabBarController *_golfTabBarController;
    SearchCourtHomeViewController *_searchCourtHomeViewController;
    FindHomeViewController *_findHomeViewController;
    RecommendHomeViewController *_recommendHomeViewController;
    MyAccountHomeViewController *_myAccountHomeViewController;
    
}
@property(nonatomic,retain)GolfTabBarController *golfTabBarController;
@property(nonatomic,retain)SearchCourtHomeViewController *searchCourtHomeViewController;
@property(nonatomic,retain)FindHomeViewController *findHomeViewController;;
@property(nonatomic,retain)RecommendHomeViewController *recommendHomeViewController;
@property(nonatomic,retain)MyAccountHomeViewController *myAccountHomeViewController;

@end
