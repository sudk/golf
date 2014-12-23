//
//  NSString+_Small.m
//  golf
//
//  Created by mahh on 14-5-1.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "NSStringSmall.h"

@implementation NSStringSmall
+(NSString *)smallSpliceStr:(NSString *)oldStr
{
    NSArray *oldArray=[oldStr componentsSeparatedByString:@".png"];
    NSString *newStr=[[oldArray objectAtIndex:0] stringByAppendingString:@"_small.png"];
    return newStr;
}
@end
