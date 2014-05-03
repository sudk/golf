//
//  CourtDetailInfoViewController.h
//  golf
//
//  Created by mahh on 14-4-12.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "AHPreviewController.h"

@interface CourtDetailInfoViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UITableView *courtInfoTable;
@property(nonatomic,strong)UIScrollView *courtInfoScroll;
@property(nonatomic,strong)NSArray *courtInfoTitleArray;
@property(nonatomic,strong)NSMutableArray *courtInfoArray;
@property(nonatomic,strong)NSString *courtId;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)UIButton *phnBtn;//球场电话
@property(nonatomic,strong)UILabel *descInfo;//球场简介
@property(nonatomic,strong)UILabel *facilityInfo;//球场设施
@property(nonatomic,strong)AHPreviewController *preview;
@end
