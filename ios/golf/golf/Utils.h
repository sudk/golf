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
+(Utils*)getInstance;
+(UIImage*)scaleToSize:(UIImage*)img size:(CGSize)size;
+(UIColor *) colorWithHexString: (NSString *) stringToConvert;
//返回对应文件目录下的文件路径
-(NSString *)fileDirectory:(NSString *)subdirectories withFileName:(NSString *)fileName;
@end
