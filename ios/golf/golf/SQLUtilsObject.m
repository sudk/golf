//
//  SQLUtilsObject.m
//  AiHuan
//
//  Created by mahh on 14-2-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "SQLUtilsObject.h"
#import "Utils.h"

@implementation SQLUtilsObject
@synthesize database=_database;
/**
 创建，打开数据库
 */
- (BOOL)openDB:(BOOL)isContact {
	//获取数据库路径
	NSString *path = [[Utils getInstance]fileDirectory:AHDirectory withFileName:AHFilename];
    NSLog(@"sqlite3 database path=======%@",path);
    NSLog(@"sqlite3_open([path UTF8String], &_database)=====%d",sqlite3_open([path UTF8String], &_database));
    //如果发现数据库不存在则利用sqlite3_open创建数据库（
	if(sqlite3_open([path UTF8String], &_database) == SQLITE_OK) {
		return YES;
    } else {
		//如果创建并打开数据库失败则关闭数据库
		sqlite3_close(_database);
		NSLog(@"Error: open database file.");
		return NO;
    }
}

/**
 登录信息表
 */
-(BOOL)create_loginInfo_tab
{
    char *sql = "CREATE TABLE IF NOT EXISTS loginInfoTb (ROW INTEGER PRIMARY KEY, phone TEXT,pswd TEXT,balance text,card_no text,email text,point text,sex text,user_name text,remark text)";
    return [self create_customInfo_tab:sql andTableName:@"登录信息表"];
}
-(void)insert_loginInfo_tab:(NSString *)phone andPswd:(NSString *)pswd andBanlance:(NSString *)banlance andCard_no:(NSString *)card_no andEmail:(NSString *)email andPoint:(NSString *)point andSex:(NSString *)sex andUser_name:(NSString *)user_name andRemark:(NSString *)remark
{
    if ([self create_loginInfo_tab]==YES)
    {
        sqlite3_stmt *stmt;
        char *update = "INSERT OR REPLACE INTO loginInfoTb (phone,pswd,balance,card_no,email,point,sex,user_name,remark) VALUES (?,?,?,?,?,?,?,?,?);";
        if (sqlite3_prepare_v2(_database, update, -1, &stmt, nil) == SQLITE_OK) {
            sqlite3_bind_text(stmt, 1, [phone UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 2, [pswd UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 3, [banlance UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 4, [card_no UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 5, [email UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 6, [point UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 7, [sex UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 8, [user_name UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(stmt, 9, [remark UTF8String], -1, SQLITE_TRANSIENT);
            
        }
        if (sqlite3_step(stmt) != SQLITE_DONE)
        {
            NSString *desc=[[@"insert_personInfo_tab  " stringByAppendingString:@"登录信息表"] stringByAppendingString:@"字段fail"];
            NSLog(@"%@",desc);
        }
        else
        {
            NSString *desc=[[@"insert_personInfo_tab  " stringByAppendingString:@"登录信息表"] stringByAppendingString:@"字段success"];
            NSLog(@"%@",desc);
        }
        sqlite3_finalize(stmt);
        sqlite3_close(_database);
    }
}
-(void)update_loginInfo_tab:(NSString *)loginAccount andPswd:(NSString *)pswd
{
    if ([self create_loginInfo_tab]) {
        if ([self openDB:NO]) {
            sqlite3_stmt *statement=nil;
            //组织SQL语句
            char *sql ="update loginInfoTb set pswd=? where loginAccount=?";
            //将SQL语句放入sqlite3_stmt中
            int success = sqlite3_prepare_v2(_database, sql, -1, &statement, NULL);
            if (success != SQLITE_OK) {
                NSLog(@"Error: failed to loginInfoTb");
            }
            sqlite3_bind_text(statement, 1, [loginAccount UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(statement, 2, [pswd UTF8String], -1, SQLITE_TRANSIENT);
            //执行SQL语句。这里是更新数据库
            success = sqlite3_step(statement);
            //如果执行失败
            if (success == SQLITE_ERROR) {
                NSLog(@"Error: failed to update the loginInfoTb database with message.");
            }
            //释放statement
            sqlite3_finalize(statement);
            //执行成功后依然要关闭数据库
            sqlite3_close(_database);
        }
    }
}
-(void)delete_loginInfo_tab
{
    if ([self create_loginInfo_tab]==YES) {
		sqlite3_stmt *statement;
		//组织SQL语句
		static char *sql = "delete from  loginInfoTb";
		//将SQL语句放入sqlite3_stmt中
		int success = sqlite3_prepare_v2(_database, sql, -1, &statement, NULL);
		if (success != SQLITE_OK) {
			NSLog(@"Error: failed to delete loginInfoTb");
		}
		//执行SQL语句。这里是更新数据库
		success = sqlite3_step(statement);
		//释放statement
		sqlite3_finalize(statement);
		
		//如果执行失败
		if (success == SQLITE_ERROR) {
			NSLog(@"Error: failed to delete loginInfoTb");
		}
        else
        {
            NSLog(@"delete loginInfoTb success");
        }
		//执行成功后依然要关闭数据库
		sqlite3_close(_database);
	}
}
-(NSMutableArray*)query_loginInfo_tab
{
    NSMutableArray *loginInfoArray=[[NSMutableArray alloc] init];
	//判断数据库是否打开
	if ([self create_loginInfo_tab]) {
        sqlite3_stmt *statement = nil;
//        loginAccount TEXT,pswd TEXT
        //sql语句
        char *sql = "SELECT phone,pswd,balance,card_no,email,point,sex,user_name,remark FROM loginInfoTb";
        
        if (sqlite3_prepare_v2(_database, sql, -1, &statement, NULL) != SQLITE_OK) {
            NSLog(@"Error: failed to prepare statement with loginInfoTb.");
        }
        else {
            //查询结果集中一条一条的遍历所有的记录，这里的数字对应的是列值。
            while (sqlite3_step(statement) == SQLITE_ROW) {
                NSMutableDictionary *loginInfoDic=[NSMutableDictionary dictionary];
                char *phoneChar = (char *)sqlite3_column_text(statement, 0);
                [self queryCustomField:phoneChar andCustomDic:loginInfoDic andCustomKey:@"phone"];
                char *pswdChar = (char *)sqlite3_column_text(statement, 1);
                [self queryCustomField:pswdChar andCustomDic:loginInfoDic andCustomKey:@"pswd"];
                char *balanceChar = (char *)sqlite3_column_text(statement, 2);
                [self queryCustomField:balanceChar andCustomDic:loginInfoDic andCustomKey:@"balance"];
                char *card_noChar = (char *)sqlite3_column_text(statement, 3);
                [self queryCustomField:card_noChar andCustomDic:loginInfoDic andCustomKey:@"card_no"];
                char *emailChar = (char *)sqlite3_column_text(statement, 4);
                [self queryCustomField:emailChar andCustomDic:loginInfoDic andCustomKey:@"email"];
                char *pointChar = (char *)sqlite3_column_text(statement, 5);
                [self queryCustomField:pointChar andCustomDic:loginInfoDic andCustomKey:@"point"];
                char *sexChar = (char *)sqlite3_column_text(statement, 6);
                [self queryCustomField:sexChar andCustomDic:loginInfoDic andCustomKey:@"sex"];
                char *user_nameChar = (char *)sqlite3_column_text(statement, 7);
                [self queryCustomField:user_nameChar andCustomDic:loginInfoDic andCustomKey:@"user_name"];
                char *remarkChar = (char *)sqlite3_column_text(statement, 8);
                [self queryCustomField:remarkChar andCustomDic:loginInfoDic andCustomKey:@"remark"];
                [loginInfoArray addObject:loginInfoDic];
            }
        }
        sqlite3_finalize(statement);
        sqlite3_close(_database);
    }
	return loginInfoArray;
}
-(void)queryCustomField:(char*)customChar andCustomDic:(NSMutableDictionary*)customDic andCustomKey:(NSString *)customKey
{
    NSString *customStr;
    if (customChar!=nil) {
        customStr = [[NSString alloc]
                           initWithUTF8String:customChar];
    }
    else
    {
        customStr=@"";
    }
    [customDic setObject:customStr forKey:customKey];
}
////地区
//-(BOOL)create_area_tab
//{
//    }
//-(void)insert_area_tab:(NSString *)areaID andArea:(NSString *)area andFatherID:(NSString *)fatherID;
//-(NSMutableArray*)query_area_tab;
//城市
-(BOOL)create_city_tab
{
    char *sql = "CREATE TABLE IF NOT EXISTS cityInfoTb (ROW INTEGER PRIMARY KEY,cityID TEXT,city TEXT,fatherID TEXT)";
    return [self create_customInfo_tab:sql andTableName:@"城市列表"];
}
-(void)insert_city_tab:(NSArray *)cityArray
{
    if ([self create_city_tab]==YES)
    {
        char* errmsg;
        if ([self openDB:NO]) {
            sqlite3_exec(_database, "begin transaction", 0, 0, &errmsg);
            for (int i=0; i<[cityArray count]; i++) {
                sqlite3_stmt *stmt;
                NSString *cityIDStr=[[cityArray objectAtIndex:i] objectForKey:@"cityID"];
                NSString *cityStr=[[cityArray objectAtIndex:i] objectForKey:@"city"];
                NSString *fatherIDStr=[[cityArray objectAtIndex:i] objectForKey:@"fatherID"];
                char *update = "INSERT OR REPLACE INTO cityInfoTb (cityID,city,fatherID) VALUES (?,?,?);";
                if (sqlite3_prepare_v2(_database, update, -1, &stmt, nil) == SQLITE_OK) {
                    sqlite3_bind_text(stmt, 1, [cityIDStr UTF8String], -1, SQLITE_TRANSIENT);
                    sqlite3_bind_text(stmt, 2, [cityStr UTF8String], -1, SQLITE_TRANSIENT);
                    sqlite3_bind_text(stmt, 3, [fatherIDStr UTF8String], -1, SQLITE_TRANSIENT);
                }
                NSLog(@"sqlite3_step(stmt)====%d",sqlite3_step(stmt));
                if (sqlite3_step(stmt) != SQLITE_DONE)
                {
                    sqlite3_close(_database);
                }
                sqlite3_finalize(stmt);// 这个sql语句的利用结束了，就调用这个函数
            }
            sqlite3_exec(_database, "commit transaction", 0, 0, &errmsg);
            sqlite3_close(_database);
        }
    }

}
-(NSMutableArray*)query_city_tab
{
    NSMutableArray *cityInfoArray=[[NSMutableArray alloc] init];
	//判断数据库是否打开
	if ([self create_city_tab]) {
        sqlite3_stmt *statement = nil;
        //sql语句
        char *sql = "SELECT cityID,city,fatherID FROM cityInfoTb";
        
        if (sqlite3_prepare_v2(_database, sql, -1, &statement, NULL) != SQLITE_OK) {
            NSLog(@"Error: failed to prepare statement with cityInfoTb.");
        }
        else {
            //查询结果集中一条一条的遍历所有的记录，这里的数字对应的是列值。
            while (sqlite3_step(statement) == SQLITE_ROW) {
                NSMutableDictionary *cityInfoDic=[NSMutableDictionary dictionary];
                char *cityIdChar = (char *)sqlite3_column_text(statement, 0);
                [self queryCustomField:cityIdChar andCustomDic:cityInfoDic andCustomKey:@"cityId"];
                char *cityChar = (char *)sqlite3_column_text(statement, 1);
                [self queryCustomField:cityChar andCustomDic:cityInfoDic andCustomKey:@"city"];
                
                char *fatherIDChar = (char *)sqlite3_column_text(statement, 2);
                [self queryCustomField:fatherIDChar andCustomDic:cityInfoDic andCustomKey:@"fatherID"];
                [cityInfoArray addObject:cityInfoDic];
            }
        }
        sqlite3_finalize(statement);
        sqlite3_close(_database);
    }
	return cityInfoArray;
}
-(NSMutableArray*)query_city_tab:(NSString *)fatherId
{
    NSMutableArray *cityInfoArray=[[NSMutableArray alloc] init];
	//判断数据库是否打开
	if ([self create_city_tab]) {
        sqlite3_stmt *statement = nil;
        //sql语句
        char *sql = "SELECT cityID,city,fatherID FROM cityInfoTb where fatherID=?";
        
        if (sqlite3_prepare_v2(_database, sql, -1, &statement, NULL) != SQLITE_OK) {
            NSLog(@"Error: failed to prepare statement with cityInfoTb.");
        }
        else {
            //查询结果集中一条一条的遍历所有的记录，这里的数字对应的是列值。
            sqlite3_bind_text(statement, 1, [fatherId UTF8String], -1, SQLITE_TRANSIENT);
            while (sqlite3_step(statement) == SQLITE_ROW) {
                NSMutableDictionary *cityInfoDic=[NSMutableDictionary dictionary];
                char *cityIdChar = (char *)sqlite3_column_text(statement, 0);
                [self queryCustomField:cityIdChar andCustomDic:cityInfoDic andCustomKey:@"cityId"];
                char *cityChar = (char *)sqlite3_column_text(statement, 1);
                [self queryCustomField:cityChar andCustomDic:cityInfoDic andCustomKey:@"city"];
                
                char *fatherIDChar = (char *)sqlite3_column_text(statement, 2);
                [self queryCustomField:fatherIDChar andCustomDic:cityInfoDic andCustomKey:@"fatherID"];
                [cityInfoArray addObject:cityInfoDic];
            }
        }
        sqlite3_finalize(statement);
        sqlite3_close(_database);
    }
	return cityInfoArray;
}

//省
-(BOOL)create_province_tab
{
    char *sql = "CREATE TABLE IF NOT EXISTS provinceInfoTb (ROW INTEGER PRIMARY KEY, provinceId TEXT,province TEXT)";
    return [self create_customInfo_tab:sql andTableName:@"省列表"];

}
-(void)insert_province_tab:(NSDictionary *)provinceDic
{
        if ([self create_province_tab]==YES)
    {
        char* errmsg;
        if ([self openDB:NO]) {
            sqlite3_exec(_database, "begin transaction", 0, 0, &errmsg);
            for (int i=0; i<[[provinceDic allValues] count]; i++) {
                sqlite3_stmt *stmt;
                NSString *provinceIdStr=[[provinceDic allKeys] objectAtIndex:i];
                NSString *provinceStr=[[provinceDic allValues] objectAtIndex:i];
                char *update = "INSERT OR REPLACE INTO provinceInfoTb (provinceId,province) VALUES (?,?);";
                if (sqlite3_prepare_v2(_database, update, -1, &stmt, nil) == SQLITE_OK) {
                    sqlite3_bind_text(stmt, 1, [provinceIdStr UTF8String], -1, SQLITE_TRANSIENT);
                    sqlite3_bind_text(stmt, 2, [provinceStr UTF8String], -1, SQLITE_TRANSIENT);
                }
                if (sqlite3_step(stmt) != SQLITE_DONE)
                {
                    sqlite3_close(_database);
                }
                sqlite3_finalize(stmt);// 这个sql语句的利用结束了，就调用这个函数
            }
            sqlite3_exec(_database, "commit transaction", 0, 0, &errmsg);
            sqlite3_close(_database);
        }
    }
}
-(NSMutableArray*)query_province_tab
{
    NSMutableArray *provinceInfoArray=[[NSMutableArray alloc] init];
	//判断数据库是否打开
	if ([self create_province_tab]) {
        sqlite3_stmt *statement = nil;
        //sql语句
        char *sql = "SELECT provinceId,province FROM provinceInfoTb";
        
        if (sqlite3_prepare_v2(_database, sql, -1, &statement, NULL) != SQLITE_OK) {
            NSLog(@"Error: failed to prepare statement with provinceInfoTb.");
        }
        else {
            //查询结果集中一条一条的遍历所有的记录，这里的数字对应的是列值。
            while (sqlite3_step(statement) == SQLITE_ROW) {
                NSMutableDictionary *provinceInfoDic=[NSMutableDictionary dictionary];
                char *provinceIdChar = (char *)sqlite3_column_text(statement, 0);
                [self queryCustomField:provinceIdChar andCustomDic:provinceInfoDic andCustomKey:@"provinceId"];
                char *provinceChar = (char *)sqlite3_column_text(statement, 1);
                [self queryCustomField:provinceChar andCustomDic:provinceInfoDic andCustomKey:@"province"];
                [provinceInfoArray addObject:provinceInfoDic];
            }
        }
        sqlite3_finalize(statement);
        sqlite3_close(_database);
    }
	return provinceInfoArray;
}
/**
 共通类
 */
-(BOOL)create_customInfo_tab:(char*)sql andTableName:(NSString *)tableName
{
    if ([self openDB:NO]) {
        sqlite3_stmt *statement;
        NSInteger sqlReturn = sqlite3_prepare_v2(_database, sql, -1, &statement, nil);
        //如果SQL语句解析出错的话程序返回
        if(sqlReturn != SQLITE_OK) {
            NSString *errorStr=[@"Error: failed to create  " stringByAppendingString:tableName];
            NSLog(@"%@",errorStr);
            sqlite3_finalize(statement);
            sqlite3_close(_database);
            return NO;
        }
        //执行SQL语句
        int success = sqlite3_step(statement);
        //释放sqlite3_stmt
        sqlite3_finalize(statement);//釋放prepared statement object
        
        //执行SQL语句失败
        if ( success != SQLITE_DONE) {
            NSString *errorStr1=[@"Error1: failed to create  " stringByAppendingString:tableName];
            NSLog(@"%@",errorStr1);
            sqlite3_close(_database);
            return NO;
        }
        NSString *successStr=[[@"Create table '" stringByAppendingString:tableName] stringByAppendingString:@"' successed."];
        NSLog(@"%@",successStr);
        return YES;
    }
    return NO;
}
@end
