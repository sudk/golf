//
//  NMNavgationBarAdd.m
//  NeiMengPay
//
//  Created by qi zuowei on 12/29/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "GolfNavgationBarAdd.h"

@implementation  UINavigationBar(addColor)
-(void)needMyDisplay{
    //UIStatusBarStyleLightContent/UIStatusBarStyleBlackOpaque/UIStatusBarStyleBlackTranslucent/UIStatusBarStyleDefault
//    [self setBarStyle:UIStatusBarStyleLightContent];
    float sysVesion=[[[UIDevice currentDevice] systemVersion] floatValue];
    UIImage *UINavigationBarImg=[UIImage imageNamed:@"title_bg2.png"];
    if (sysVesion>=7.0) {
        UINavigationBarImg=[UIImage imageNamed:@"title_bg27.png"];
    }
    if (sysVesion>=5.0) {
        
        [self setBackgroundImage:UINavigationBarImg forBarMetrics:UIBarMetricsDefault];
    }
    else{
        [self setNeedsDisplay];
    }
}
-(void)drawRect:(CGRect)rect{
    float sysVesion=[[[UIDevice currentDevice] systemVersion] doubleValue];
    UIImage *UINavigationBarImg=[UIImage imageNamed:@"title_bg2.png"];
    if (sysVesion>=7.0) {
        UINavigationBarImg=[UIImage imageNamed:@"title_bg27.png"];
    }
    [super drawRect:rect];
    UIImage * image=UINavigationBarImg;
    [image drawInRect:rect];
}
@end
