//
//  SearchCourtHomeViewController.h
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "DSLCalendarView.h"
#import "HttpUtils.h"
#import "SQLUtilsObject.h"
#import "BDKNotifyHUD.h"

@interface SearchCourtHomeViewController : BaseViewController<UITableViewDelegate,UITableViewDataSource,DSLCalendarViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate>
{
    BOOL _isCurrentView;
    NSDictionary *_changeDic;
}
@property(nonatomic,strong)UITableView *conditionTable;
@property(nonatomic,strong)NSArray *titleArray;
@property(nonatomic,strong)NSString *selectCity;//选择的当前城市
@property(nonatomic,strong)NSString *selectDateStr;
@property(nonatomic,strong)NSString *selectTimeStr;
@property(nonatomic,strong)NSMutableArray *contentArray;
@property(nonatomic,strong)DSLCalendarView *calendarView;
@property(nonatomic,strong)NSDictionary *changeDic;
@property(nonatomic,strong)NSArray *timeArray;
@property(nonatomic,strong)UIView *timePickView;
@property(nonatomic,strong)NSString *selectTime;
@property(nonatomic,assign)BOOL isFromUpdate;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,strong)BDKNotifyHUD *notify;
@property(nonatomic,strong)NSString *notificationText;
@property(nonatomic,strong)NSArray *iconArray;
@property(nonatomic,strong)UIView *allCalendarView;
@end
