//
//  ChangePswdViewController.h
//  golf
//
//  Created by mahh on 14-4-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "NMTextField.h"
#import "SQLUtilsObject.h"

@interface ChangePswdViewController : BaseViewController<UITextFieldDelegate>
@property(nonatomic,strong)NMTextField *smsTokenField;
@property(nonatomic,strong)NMTextField *pswdField;
@property(nonatomic,strong)NMTextField *confirmPswdField;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@end
