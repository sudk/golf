//
//  EvaluateViewController.h
//  golf
//
//  Created by mahh on 14-4-15.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"

@interface EvaluateViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UITableView *evaluateTable;
@property(nonatomic,strong)NSArray *evaluateArray;
@end
