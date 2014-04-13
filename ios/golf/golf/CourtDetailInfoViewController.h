//
//  CourtDetailInfoViewController.h
//  golf
//
//  Created by mahh on 14-4-12.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"

@interface CourtDetailInfoViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UITableView *courtInfoTable;
@property(nonatomic,strong)UIScrollView *courtInfoScroll;
@property(nonatomic,strong)NSArray *courtInfoTitleArray;
@property(nonatomic,strong)NSArray *courtInfoArray;
@end
