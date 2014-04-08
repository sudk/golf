//
//  SearchCourtHomeViewController.h
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "DSLCalendarView.h"

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
@end
