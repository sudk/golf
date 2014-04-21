//
//  LoginUtilViewController.h
//  NeiMengPay
//
//  Created by qi zuowei on 12/19/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoginAndRegistDelegate.h"
#import "GolfNavigationController.h"
@interface LoginUtilViewController : GolfNavigationController<LoginAndRegistDelegate>{
    id _target;
    SEL _selector;
}
@property(nonatomic,retain)id _target;
@property(nonatomic,assign)SEL _selector;

-(void)setTarget:(id)target Selector:(SEL)selector;
@end
