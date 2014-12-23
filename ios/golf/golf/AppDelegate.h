//
//  AppDelegate.h
//  golf
//
//  Created by mahh on 14-4-1.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GolfMainViewController.h"
#import "GolfNavigationController.h"
#import "SQLUtilsObject.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property(strong,nonatomic)GolfMainViewController *golfMainViewController;
@property(strong,nonatomic)GolfNavigationController *golfNavigationController;
@property(strong,nonatomic)SQLUtilsObject *sqlUtils;
@end
