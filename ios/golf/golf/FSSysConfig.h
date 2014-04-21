//
//  FSSysConfig.h
//  AiHuan
//
//  Created by mahh on 14-4-11.
//  Copyright (c) 2014年 mahh. All rights reserved.
//用于工程中全局变量中转

#import <Foundation/Foundation.h>

@interface FSSysConfig : NSObject
@property(nonatomic,assign)BOOL isLogin;//用户isLogin
@property(nonatomic,strong)NSString *loginAccount;//登录帐号
@property(nonatomic,strong)NSDictionary *keyDic;//value:key key:keyid
@property(nonatomic,strong)NSString *os;
@property(nonatomic,strong)NSString *model;
@property(nonatomic,strong)NSString *deviceid;
+(FSSysConfig*)getInstance;
@end
