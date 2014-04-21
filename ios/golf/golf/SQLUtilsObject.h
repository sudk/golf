//
//  SQLUtilsObject.h
//  AiHuan
//
//  Created by mahh on 14-2-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>
#define AHFilename    @"AHData.sqlite3"
#define AHDirectory @"AHSql"

@interface SQLUtilsObject : NSObject
{
    sqlite3 *_database;
}
@property(nonatomic)sqlite3 *database;

//登录信息表
-(BOOL)create_loginInfo_tab;
-(void)insert_loginInfo_tab:(NSString *)phone andPswd:(NSString *)pswd andBanlance:(NSString *)banlance andCard_no:(NSString *)card_no andEmail:(NSString *)email andPoint:(NSString *)point andSex:(NSString *)sex andUser_name:(NSString *)user_name andRemark:(NSString *)remark;
-(void)update_loginInfo_tab:(NSString *)loginAccount andPswd:(NSString *)pswd;
-(NSMutableArray*)query_loginInfo_tab;
-(void)delete_loginInfo_tab;

@end
