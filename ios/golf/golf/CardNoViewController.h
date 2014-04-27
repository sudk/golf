//
//  CardNoViewController.h
//  golf
//
//  Created by mahh on 14-4-21.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "SQLUtilsObject.h"
#import "HttpUtils.h"
#import "NMTextField.h"

@interface CardNoViewController : BaseViewController<UITextFieldDelegate>
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)NMTextField *card_noField;
@property(nonatomic,strong)UIButton *bindBtn;
@end
