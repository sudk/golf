//
//  CourtModel.h
//  golf
//
//  Created by mahh on 14-4-10.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CourtModel : NSObject
@property(nonatomic,strong)NSString *courtName;
@property(nonatomic,assign)float courtDistance;
@property(nonatomic,assign)float courtPrice;
@property(nonatomic,strong)NSString *courtImgUrl;
@property(nonatomic,assign)BOOL isUp;

-(NSComparisonResult)compareName:(CourtModel *)courtModel;
-(NSComparisonResult)compareDistance:(CourtModel *)courtModel;
-(NSComparisonResult)comparePrice:(CourtModel *)courtModel;
@end
