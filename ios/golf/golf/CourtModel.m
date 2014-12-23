//
//  CourtModel.m
//  golf
//
//  Created by mahh on 14-4-10.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "CourtModel.h"
@implementation CourtModel

//自定义排序方法
-(NSComparisonResult)compareName:(CourtModel *)courtModel{
    //默认按球场名称排序
    NSComparisonResult result;
    if (_isUp) {
        result= [self.courtName compare:courtModel.courtName];
    }
    else
    {
        result= [courtModel.courtName compare:self.courtName];
    }
    
    return result;
}

-(NSComparisonResult)compareDistance:(CourtModel *)courtModel{
    //按距离排序
    NSComparisonResult result;
    if (_isUp) {
        result= [[NSNumber numberWithFloat:self.courtDistance] compare:[NSNumber numberWithFloat:courtModel.courtDistance]];//注意:基本数据类型要进行数据转换
    }
    else
    {
        result= [[NSNumber numberWithFloat:courtModel.courtDistance] compare:[NSNumber numberWithFloat:self.courtDistance]];
    }
    
    return result;
}
-(NSComparisonResult)comparePrice:(CourtModel *)courtModel{
    //按价格排序
    NSComparisonResult result;
    if (_isUp) {
        result= [[NSNumber numberWithFloat:self.courtPrice] compare:[NSNumber numberWithFloat:courtModel.courtPrice]];//注意:基本数据类型要进行数据转换
    }
    else
    {
        result= [[NSNumber numberWithFloat:courtModel.courtPrice] compare:[NSNumber numberWithFloat:self.courtPrice]];
    }
    
    return result;
}
@end
