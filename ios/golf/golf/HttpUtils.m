//
//  HttpUtils.m
//  AiHuan
//
//  Created by mahh on 14-4-14.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "HttpUtils.h"
#import "ASIFormDataRequest.h"
#import "Utils.h"
#import "FSSysConfig.h"

@implementation HttpUtils

-(void)startRequest:(NSDictionary *)postDic andUrl:(NSString *)urlStr andRequestField:(NSString *)field andNotificationName:(NSString *)notificationName
{
    self.requestField=field;
    NSString * encodedURLStr = [urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString:encodedURLStr];
    NSLog(@"url====%@",url);
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    NSError *error = nil;
    NSMutableData *jsonData =(NSMutableData*) [NSJSONSerialization dataWithJSONObject:postDic options:NSJSONWritingPrettyPrinted error:&error];
    [request setPostBody:jsonData];
	request.delegate = self;
    [request setRequestMethod:@"POST"];
    [request setUserInfo:[NSDictionary dictionaryWithObjectsAndKeys:notificationName,@"notificationName",nil]];
    [request startAsynchronous];
}

- (void)requestFinished:(ASIHTTPRequest *)request {
    NSString *str=[request responseString];
    NSLog(@"请求方法%@的str=====%@",_requestField,str);
    NSDictionary *jsonDic=[NSJSONSerialization JSONObjectWithData:[request responseData] options:NSJSONReadingAllowFragments error:nil];
    NSString *theNotificationName=[request.userInfo objectForKey:@"notificationName"];
        NSLog(@"请求方法%@的jsonDic======%@",_requestField,jsonDic);
        [[NSNotificationCenter defaultCenter]postNotificationName:theNotificationName object:jsonDic];
}
- (void)requestFailed:(ASIHTTPRequest *)request {
    NSError *error=[request error];
    NSLog(@"请求方法%@的error======%@",_requestField,error);
    NSString *theNotificationName=[request.userInfo objectForKey:@"notificationName"];
    [[NSNotificationCenter defaultCenter]postNotificationName:theNotificationName object:nil];
    self.notificationText=networkAbnormalInfo;
    [self displayNotification];
}
- (void)displayNotification {
    if (self.notify.isAnimating) return;
    [[[UIApplication sharedApplication]keyWindow] addSubview:self.notify];
    [self.notify presentWithDuration:1.5f speed:1.0f inView:[[UIApplication sharedApplication]keyWindow] completion:^{
        [self.notify removeFromSuperview];
    }];
}

- (BDKNotifyHUD *)notify {
    if (_notify != nil) return _notify;
    _notify = [BDKNotifyHUD notifyHUDWithImage:[UIImage imageNamed:@""] text:_notificationText];
    _notify.center = CGPointMake(73, [[UIApplication sharedApplication]keyWindow].center.y - 20);
    return _notify;
}
@end
