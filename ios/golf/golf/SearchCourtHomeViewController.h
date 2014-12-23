//
//  SearchCourtHomeViewController.h
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "DSLCalendarView.h"
#import "SQLUtilsObject.h"
#import "BDKNotifyHUD.h"
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>

@interface SearchCourtHomeViewController : BaseViewController<UITableViewDelegate,UITableViewDataSource,DSLCalendarViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate,MKMapViewDelegate>
{
    BOOL _isCurrentView;
    NSDictionary *_changeDic;
}
@property(nonatomic,strong)UITableView *conditionTable;
@property(nonatomic,strong)NSArray *titleArray;
@property(nonatomic,strong)NSString *selectCity;//选择的当前城市
@property(nonatomic,strong)NSString *selectDateStr;
@property(nonatomic,strong)NSDate *selectDate;
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
@property(nonatomic,strong)MKMapView *map;
@property(nonatomic,assign)float zoomLevel;
@property(nonatomic,strong)NSString *long_latStr;
@end
