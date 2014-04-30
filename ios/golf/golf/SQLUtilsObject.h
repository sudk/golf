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
//创建城市地区信息表
//地区
-(BOOL)create_area_tab;
-(void)insert_area_tab:(NSString *)areaID andArea:(NSString *)area andFatherID:(NSString *)fatherID;
-(NSMutableArray*)query_area_tab;
//城市
-(BOOL)create_city_tab;
-(void)insert_city_tab:(NSArray *)cityArray;
-(NSMutableArray*)query_city_tab;
-(NSMutableArray*)query_city_tab:(NSString *)fatherId;
-(NSString*)query_city_tab_cityId:(NSString *)cityName;
//省
-(BOOL)create_province_tab;
-(void)insert_province_tab:(NSDictionary *)provinceDic;
-(NSMutableArray*)query_province_tab;
@end
