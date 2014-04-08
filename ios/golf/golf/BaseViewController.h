//
//  BaseViewController.h
//  Aihuan
//
//  Created by mahh on 14-2-17.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BaseViewController : UIViewController
-(UIButton *)leftButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name;
-(UIButton *)rightButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name;
-(UIButton *)filterButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name;
-(UIButton *)showCartButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name;
-(UIBarButtonItem *)topBarButtonItem:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name;
-(void)baseBackMethod;
@end
