//
//  AHLoginViewController.h
//  AiHuan
//
//  Created by xingdd on 14-4-14.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "HttpUtils.h"
#import "AHRegisterViewController.h"
#import "SQLUtilsObject.h"
#import "BDKNotifyHUD.h"
#import "LoginAndRegistDelegate.h"

@interface AHLoginViewController : BaseViewController<UITextFieldDelegate,LoginAndRegistDelegate>
@property(nonatomic,strong)UITextField *pswdField;
@property(nonatomic,strong)UITextField *accountField;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,assign)BOOL isAutoLoginFlg;
@property(nonatomic,strong)BDKNotifyHUD *notify;
@property(nonatomic,strong)NSString *notificationText;
@property(strong,nonatomic)id<LoginAndRegistDelegate> delegate;

@end
