//
//  HttpUtils.h
//  AiHuan
//
//  Created by mahh on 14-4-14.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BDKNotifyHUD.h"

@interface HttpUtils : NSObject
@property(nonatomic,strong)NSString *requestField;
@property(nonatomic,strong)BDKNotifyHUD *notify;
@property(nonatomic,strong)NSString *notificationText;
@property(nonatomic,strong)UIView *errorView;
-(void)startRequest:(NSDictionary *)postDic andUrl:(NSString *)urlStr andRequestField:(NSString *)field andNotificationName:(NSString *)notificationName andViewControler:(UIView *)view;

@end
