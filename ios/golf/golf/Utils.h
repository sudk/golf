//
//  Utils.h
//  Aihuan
//
//  Created by mahh on 14-2-18.
//  Copyright (c) 2014年 mahh. All rights reserved.
//项目中用到的公共类

#import <Foundation/Foundation.h>
#define DEFAULT_VOID_COLOR [UIColor whiteColor]

@interface Utils : NSObject
+(UIImage*)scaleToSize:(UIImage*)img size:(CGSize)size;
+(UIColor *) colorWithHexString: (NSString *) stringToConvert;
+(NSString *)fileDirectory:(NSString *)subdirectories;
@end
