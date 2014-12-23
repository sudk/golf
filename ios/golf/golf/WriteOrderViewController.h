//
//  WriteOrderViewController.h
//  golf
//
//  Created by mahh on 14-5-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "SQLUtilsObject.h"

@interface WriteOrderViewController : BaseViewController<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate>
@property(nonatomic,strong)UITableView *orderTable;
@property(nonatomic,strong)NSString *courtName;
@property(nonatomic,strong)NSString *courtTime;
@property(nonatomic,strong)NSString *courtPrice;
@property(nonatomic,strong)NSString *courtType;
@property(nonatomic,strong)UIButton *removepBtn;
@property(nonatomic,strong)UILabel *pNumLabel;
@property(nonatomic,strong)UIButton *addpBtn;
@property(nonatomic,assign)int pNum;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,strong)NSString *personName;
@property(nonatomic,strong)NSString *personPhn;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)NSString *orderType;
@property(nonatomic,strong)NSString *relation_id;
@property(nonatomic,strong)UITextField *pNameField;
@property(nonatomic,strong)UITextField *phnField;
@property(nonatomic,strong)NSString *agent_id;
@end
