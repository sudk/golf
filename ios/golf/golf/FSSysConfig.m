//
//  FSSysConfig.m
//  AiHuan
//
//  Created by mahh on 14-4-11.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "FSSysConfig.h"

@implementation FSSysConfig
- (id)init {
    self = [super init];
    if (self) {
        _isLogin=NO;
        _loginAccount=@"Flex";
        _keyDic=nil;
        _os=@"2";
        _model=@"iphone4s";
        _deviceid=@"0a8b5c4d6d7e4939c80c8326788868d8";
    }
    return self;
}
+(FSSysConfig*)getInstance
{
    static FSSysConfig *instance;
    @synchronized(self)
    {
        if (instance==nil) {
            instance=[[FSSysConfig alloc]init];
        }
    }
    return instance;
}
@end
