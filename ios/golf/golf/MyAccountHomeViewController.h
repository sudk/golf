//
//  MyAccountHomeViewController.h
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "SQLUtilsObject.h"

@interface MyAccountHomeViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UITableView *accountTable;
@property(nonatomic,strong)NSArray *section1Array;
@property(nonatomic,strong)NSArray *section2Array;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@end
