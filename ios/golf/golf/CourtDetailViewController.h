//
//  CourtDetailViewController.h
//  golf
//
//  Created by mahh on 14-4-10.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "DSLCalendarView.h"
#import "LMapViewController.h"
#import "CourtDetailInfoViewController.h"
#import "AHPreviewController.h"
#import "EvaluateViewController.h"
#import "BDKNotifyHUD.h"
#import "WriteOrderViewController.h"
#define courtNameLabelTag 1001
#define addressLabelTag   1002
#define raidersLabelTag   1003
#define cDetailNameLebelTag 1004
#define cDetailFacilityLebelTag 1005
#define cDetailPriceLebelTag  1006
#define cDetailPriceTypeLebelTag 1007
#define cAgentNameLebelTag  1008

@interface CourtDetailViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate,DSLCalendarViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate>
@property(nonatomic,strong)NSString *courtName;//球场名称
@property(nonatomic,strong)UITableView *courtDetailTable;
@property(nonatomic,strong)NSMutableArray *courtDetailArray;
@property(nonatomic,strong)NSString *dateStr;
@property(nonatomic,strong)NSString *timeStr;
@property(nonatomic,strong)DSLCalendarView *calendarView;
@property(nonatomic,strong)UIView *timePickView;
@property(nonatomic,strong)NSArray *timeArray;
@property(nonatomic,strong)NSMutableArray *evaluateArray;//设计草坪设施服务
@property(nonatomic,strong)NSString *courtAddress;
@property(nonatomic,strong)NSString *courtId;
@property(nonatomic,strong)LMapViewController *lMap;
@property(nonatomic,strong)CourtDetailInfoViewController *detailInfo;
@property(nonatomic,strong)AHPreviewController *preview;
@property(nonatomic,strong)EvaluateViewController *evaluate;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)NSArray *courtImgArray;
@property(nonatomic,strong)NSString *courtImgStr;
@property(nonatomic,strong)NSString *courtDateStr;
@property(nonatomic,strong)BDKNotifyHUD *notify;
@property(nonatomic,strong)NSString *notificationText;
@property(nonatomic,strong)UIView *allCalendarView;
@property(nonatomic,strong)UIButton *dateBtn;
@property(nonatomic,strong)UIButton *timeBtn;
@property(nonatomic,strong)NSDate *selectDate;
@property(nonatomic,strong)NSMutableArray *courtInfoArray;
@property(nonatomic,strong)NSArray *fairwayImgArray;
@property(nonatomic,strong)NSString *descInfoStr;
@property(nonatomic,strong)NSString *phnStr;
@property(nonatomic,strong)NSString *facilityInfoStr;
@property(nonatomic,strong)UIView *courtOrderView;//球位预约提示框
@property(nonatomic,strong)WriteOrderViewController *writeOrderViewController;
@property(nonatomic,strong)NSString *selectOrderCourtName;
@property(nonatomic,strong)NSString *selectOrderCourtTime;
@property(nonatomic,strong)NSString *selectOrderCourtPrice;
@property(nonatomic,strong)NSString *selectOrderCourtPriceType;
@property(nonatomic,strong)NSString *selectOrderCourtRelationId;
@property(nonatomic,strong)NSString *selectOrderCourtAgentid;
@end
