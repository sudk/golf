//
//  ListCourtViewController.h
//  golf
//
//  Created by mahh on 14-4-8.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "CourtDetailViewController.h"

@interface ListCourtViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)NSString *courtTitle;
@property(nonatomic,strong)UITableView *courtInfoTable;
@property(nonatomic,strong)NSMutableArray *courtInfoArray;
@property(nonatomic,strong)NSArray *courtArray;//球场数组
@property(nonatomic,strong)NSString *dateStr;
@property(nonatomic,strong)NSString *timeStr;
@property(nonatomic,strong)CourtDetailViewController *courtDetailVc;
@end
