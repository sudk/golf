//
//  ListCourtViewController.h
//  golf
//
//  Created by mahh on 14-4-8.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"

@interface ListCourtViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)NSString *courtTitle;
@property(nonatomic,strong)UITableView *courtInfoTable;
@property(nonatomic,strong)NSArray *courtInfoArray;
@property(nonatomic,strong)NSString *dateStr;
@property(nonatomic,strong)NSString *timeStr;
@end
