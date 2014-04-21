//
//  AHRegisterViewController.h
//  AiHuan
//
//  Created by xingdd on 14-4-15.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import "HttpUtils.h"
#import "BDKNotifyHUD.h"
#import "NMTextField.h"
#import "SQLUtilsObject.h"
#import "TBPickerView.h"

@interface AHRegisterViewController : BaseViewController<UITextFieldDelegate,TBPickerViewDelegate>
@property(nonatomic,strong)NMTextField *phnField;
@property(nonatomic,strong)NMTextField *pswdField;
@property(nonatomic,strong)NMTextField *user_nameField;
@property(nonatomic,strong)NMTextField *card_noField;
@property(nonatomic,strong)NMTextField *emailField;
//@property(nonatomic,strong)NMTextField *sexField;
@property(nonatomic,strong)UITextField *vcodeField;
@property(nonatomic,assign)BOOL isAgreeFlg;
@property(nonatomic,strong)HttpUtils *httpUtils;
@property(nonatomic,strong)BDKNotifyHUD *notify;
@property(nonatomic,strong)NSString *notificationText;
@property(nonatomic,strong)UIButton *vcodeBtn;
@property(nonatomic,strong)NSTimer *repeatTimer;
@property(nonatomic,assign)int second;//秒数
@property(nonatomic,strong)UIButton *registBtn;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,strong)TBPickerView *sexPickerView;
@property(nonatomic,strong)UIButton *sexBtn;
@property(nonatomic,strong)NSArray *sexArray;
@end
