//
//  AccountInfoViewController.h
//  golf
//
//  Created by mahh on 14-4-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
#import "SQLUtilsObject.h"

@interface AccountInfoViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UITableView *infoTable;
@property(nonatomic,strong)NSArray *titleArray;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,strong)NSMutableArray *contentArray;
@end
