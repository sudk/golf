//
//  KeyWordViewController.h
//  golf
//
//  Created by mahh on 14-4-7.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"

@interface KeyWordViewController : BaseViewController<UISearchBarDelegate,UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UISearchBar *searchBar;
@property(nonatomic,strong)NSArray *courtNameArray;
@property(nonatomic,strong)NSMutableDictionary *resultDic;//搜索结果
@property(nonatomic,strong)NSMutableArray *searchByName;
@property(nonatomic,strong)NSMutableArray *searchByPhone;
@property(nonatomic,strong)NSDictionary *changeDic;
@property(nonatomic,strong)UITableView *courtNameTable;
@end
