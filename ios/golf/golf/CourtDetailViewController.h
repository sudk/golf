//
//  CourtDetailViewController.h
//  golf
//
//  Created by mahh on 14-4-10.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "DSLCalendarView.h"
#define courtNameLabelTag 1001
#define addressLabelTag   1002
#define raidersLabelTag   1003
#define cDetailNameLebelTag 1004
#define cDetailFacilityLebelTag 1005
#define cDetailPriceLebelTag  1006
#define cDetailPriceTypeLebelTag 1007

@interface CourtDetailViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate,DSLCalendarViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate>
@property(nonatomic,strong)NSString *courtName;//球场名称
@property(nonatomic,strong)UITableView *courtDetailTable;
@property(nonatomic,strong)NSArray *courtDetailArray;
@property(nonatomic,strong)NSString *dateStr;
@property(nonatomic,strong)NSString *timeStr;
@property(nonatomic,strong)DSLCalendarView *calendarView;
@property(nonatomic,strong)UIView *timePickView;
@property(nonatomic,strong)NSArray *timeArray;
@end
