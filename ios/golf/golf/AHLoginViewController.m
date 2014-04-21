//
//  AHLoginViewController.m
//  AiHuan
//
//  Created by xingdd on 14-4-14.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "AHLoginViewController.h"
#import "Utils.h"
#import "FSSysConfig.h"
#import "NewLoadingView.h"
#import "AppDelegate.h"


@interface AHLoginViewController ()

@end

@implementation AHLoginViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void)viewWillAppear:(BOOL)animated
{
    if ([[FSSysConfig getInstance]isLogin]==YES) {
        [_delegate completeLogin];
    }
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"登录";
    _httpUtils=[[HttpUtils alloc]init];
    _sqlUtils=[[SQLUtilsObject alloc]init];
    UIButton *registerBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    registerBtn.frame=CGRectMake(0, 0, 80, 44);
    [registerBtn setTitle:@"注册" forState:UIControlStateNormal];
    [registerBtn setBackgroundImage:[UIImage imageNamed:@""] forState:UIControlStateNormal];
    [registerBtn setBackgroundImage:[UIImage imageNamed:@""] forState:UIControlStateHighlighted];
    [registerBtn addTarget:self action:@selector(registerMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *registerItem=[[UIBarButtonItem alloc]initWithCustomView:registerBtn];
    self.navigationItem.rightBarButtonItem=registerItem;
    
    _accountField=[[UITextField alloc]initWithFrame:CGRectMake(20, 30, SCREEN_WIDTH-40, 50)];
    _accountField.placeholder=@"帐号/手机号";
    _accountField.text=@"13335130151";
    _accountField.delegate=self;
    _accountField.borderStyle=UITextBorderStyleRoundedRect;
    [self.view addSubview:_accountField];
    
    _pswdField=[[UITextField alloc]initWithFrame:CGRectMake(20, _accountField.frame.origin.y+_accountField.frame.size.height+15, SCREEN_WIDTH-40, 50)];
    _pswdField.placeholder=@"密码";
    _pswdField.text=@"123";
    _pswdField.secureTextEntry=YES;
    _pswdField.delegate=self;
    _pswdField.borderStyle=UITextBorderStyleRoundedRect;
    [self.view addSubview:_pswdField];
    
    UIButton *forgetPswdBtn=[[UIButton alloc]initWithFrame:CGRectMake(200, _pswdField.frame.origin.y+_pswdField.frame.size.height+10, 90, 40)];
    [forgetPswdBtn setTitle:@"忘记密码?" forState:UIControlStateNormal];
    [forgetPswdBtn setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
    forgetPswdBtn.titleLabel.font=[UIFont systemFontOfSize:14];
    forgetPswdBtn.backgroundColor=[UIColor clearColor];
    [forgetPswdBtn addTarget:self action:@selector(forgetPswdMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:forgetPswdBtn];
    
    UIButton *loginBtn=[[UIButton alloc]initWithFrame:CGRectMake(190, forgetPswdBtn.frame.origin.y+forgetPswdBtn.frame.size.height, 80, 60)];
    [loginBtn setTitle:@"立即登录" forState:UIControlStateNormal];
    [loginBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [loginBtn addTarget:self action:@selector(ahLoginMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:loginBtn];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -method

- (void)registerMethod
{
    AHRegisterViewController *registerViewController = [[AHRegisterViewController alloc] init];
    [self.navigationController pushViewController:registerViewController animated:YES];
}
-(void)ahLoginMethod
{
    if (_accountField.text.length==0) {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:@"账号不能为空" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    if (_pswdField.text.length==0) {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:@"密码不能为空" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    [NewLoadingView showHUDAddedTo:self.view animated:YES].labelText=@"登录中，请稍候...";
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahLoginResponseMethod:) name:@"com.golf.ahLoginMethod" object:nil];
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"user/login" forKey:@"cmd"];
    [dic setObject:_accountField.text forKey:@"phone"];
    [dic setObject:_pswdField.text forKey:@"passwd"]
    ;
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"user/login" andNotificationName:@"com.golf.ahLoginMethod" andViewControler:self.view];
}

-(void)ahLoginResponseMethod:(NSNotification*)notification
{
    [NewLoadingView hideHUDForView:self.view animated:YES];
    NSDictionary *loginDic=[notification object];
    NSLog(@"登录完成数据====%@",loginDic);
    NSNumber *codeNum=[loginDic objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        [[FSSysConfig getInstance]setIsLogin:YES];
        [[FSSysConfig getInstance]setLoginAccount:[loginDic objectForKey:@"user_name"]];
        NSArray *loginArray=[_sqlUtils query_loginInfo_tab];
        NSLog(@"loginArray====%@",loginArray);
        if ([loginArray count]>0) {
            [_sqlUtils delete_loginInfo_tab];
        }
        [_sqlUtils insert_loginInfo_tab:_accountField.text andPswd:_pswdField.text andBanlance:[loginDic objectForKey:@"balance"] andCard_no:[loginDic objectForKey:@"card_no"] andEmail:[loginDic objectForKey:@"email"] andPoint:[loginDic objectForKey:@"point"] andSex:[loginDic objectForKey:@"sex"] andUser_name:[loginDic objectForKey:@"user_name"] andRemark:[loginDic objectForKey:@"remark"]];
        [_delegate completeLogin];
    }
    else
    {
        self.notificationText=@"登录失败";
        [self displayNotification];
    }
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahLoginMethod" object:nil];
}
- (void)displayNotification {
    if (self.notify.isAnimating) return;
    [self.view addSubview:self.notify];
    //    [[[UIApplication sharedApplication]keyWindow] addSubview:self.notify];
    [self.notify presentWithDuration:1.5f speed:1.0f inView:self.view completion:^{
        [self.notify removeFromSuperview];
    }];
}

- (BDKNotifyHUD *)notify {
    if (_notify != nil) return _notify;
    _notify = [BDKNotifyHUD notifyHUDWithImage:[UIImage imageNamed:@""] text:_notificationText];
    _notify.center = CGPointMake(73, self.view.center.y - 20);
    return _notify;
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
-(void)forgetPswdMethod
{
    
}
@end
