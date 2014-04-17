//
//  NMNavgationBarAdd.m
//  NeiMengPay
//
//  Created by qi zuowei on 12/29/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "GolfNavgationBarAdd.h"
#import "Utils.h"

@implementation  UINavigationBar(addColor)
-(void)needMyDisplay{
    float sysVesion=[[[UIDevice currentDevice] systemVersion] floatValue];
//    UIImage *UINavigationBarImg=[UIImage imageNamed:@"title_bg2.png"];
    UIImage *UINavigationBarImg=[self buttonImageFromColor:[Utils colorWithHexString:@"#237fca"]];
    
    if (sysVesion>=7.0) {
//        UINavigationBarImg=[UIImage imageNamed:@"title_bg27.png"];
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
//    UIImage *UINavigationBarImg=[UIImage imageNamed:@"title_bg2.png"];
    UIImage *UINavigationBarImg=[self buttonImageFromColor:[Utils colorWithHexString:@"#237fca"]];
    if (sysVesion>=7.0) {
//        UINavigationBarImg=[UIImage imageNamed:@"title_bg27.png"];
    }
    [super drawRect:rect];
    UIImage * image=UINavigationBarImg;
    [image drawInRect:rect];
}

- (UIImage *) buttonImageFromColor:(UIColor *)color {
    CGRect rect = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return img;
}
@end
