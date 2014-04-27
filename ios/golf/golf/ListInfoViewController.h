//
//  ListInfoViewController.h
//  golf
//
//  Created by mahh on 14-4-7.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "SQLUtilsObject.h"
#define infoLabelTag 1001

@interface ListInfoViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)NSString *infoTitle;
@property(nonatomic,strong)UITableView *listTable;
@property(nonatomic,strong)NSArray *infoArray;
@property(nonatomic,strong)NSDictionary *changeDic;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@end
