//
//  ConfirmViewController.h
//  golf
//
//  Created by mahh on 14-5-3.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"

@interface ConfirmViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)NSString *order_id;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)UITableView *orderInfoTable;
@end
