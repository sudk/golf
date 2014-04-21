//
//  ChangePswdViewController.m
//  golf
//
//  Created by mahh on 14-4-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "ChangePswdViewController.h"
#import "NewLoadingView.h"

@interface ChangePswdViewController ()

@end

@implementation ChangePswdViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    self.title=@"修改密码";
    _httpUtils=[[HttpUtils alloc]init];
    _sqlUtils=[[SQLUtilsObject alloc]init];
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    
    _smsTokenField=[[NMTextField alloc]initWithFrame:CGRectMake(10, 30, SCREEN_WIDTH-100, 40) WithText:@"验证码" WithRightView:nil withFlg:@"lone1"];
    _smsTokenField.textField.delegate=self;
    _smsTokenField.textField.text=@"1111";
    _smsTokenField.textField.borderStyle=UITextBorderStyleRoundedRect;
    [self.view addSubview:_smsTokenField];
    
    UIButton *smsTokenBtn=[[UIButton alloc]initWithFrame:CGRectMake(SCREEN_WIDTH-80, _smsTokenField.frame.origin.y, 70, 40)];
    [smsTokenBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    smsTokenBtn.titleLabel.font=[UIFont systemFontOfSize:12];
    [smsTokenBtn setTitle:@"获取验证码" forState:UIControlStateNormal];
    [smsTokenBtn setBackgroundColor:[UIColor orangeColor]];
    [smsTokenBtn addTarget:self action:@selector(smsTokenMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:smsTokenBtn];
    
    _pswdField=[[NMTextField alloc]initWithFrame:CGRectMake(10, _smsTokenField.frame.origin.y+_smsTokenField.frame.size.height+10, SCREEN_WIDTH-20, 40) WithText:@"新密码" WithRightView:nil withFlg:@"lone1"];
    _pswdField.textField.delegate=self;
    _pswdField.textField.secureTextEntry=YES;
    _pswdField.textField.borderStyle=UITextBorderStyleRoundedRect;
    [self.view addSubview:_pswdField];
    
    _confirmPswdField=[[NMTextField alloc]initWithFrame:CGRectMake(10, _pswdField.frame.origin.y+_pswdField.frame.size.height+10, SCREEN_WIDTH-20, 40) WithText:@"确认密码" WithRightView:nil withFlg:@"lone1"];
    _confirmPswdField.textField.delegate=self;
    _confirmPswdField.textField.secureTextEntry=YES;
    _confirmPswdField.textField.borderStyle=UITextBorderStyleRoundedRect;
    [self.view addSubview:_confirmPswdField];
    
    UIButton *submitBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, _confirmPswdField.frame.origin.y+_confirmPswdField.frame.size.height+20, SCREEN_WIDTH-20, 40)];
    [submitBtn setTitle:@"提交" forState:UIControlStateNormal];
    [submitBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [submitBtn addTarget:self action:@selector(submitMethod) forControlEvents:UIControlEventTouchUpInside];
    [submitBtn setBackgroundColor:[UIColor orangeColor]];
    [self.view addSubview:submitBtn];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)submitMethod
{
    if (_smsTokenField.textField.text.length==0) {
        UIAlertView *smsAlert=[[UIAlertView alloc]initWithTitle:nil message:@"验证码不能为空" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [smsAlert show];
        return;
    }
    if (_pswdField.textField.text.length==0) {
        UIAlertView *pswdAlert=[[UIAlertView alloc]initWithTitle:nil message:@"密码不能为空" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [pswdAlert show];
        return;
    }
    if (_confirmPswdField.textField.text.length==0) {
        UIAlertView *repswdAlert=[[UIAlertView alloc]initWithTitle:nil message:@"确认密码不能为空" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [repswdAlert show];
        return;
    }
    if (![_pswdField.textField.text isEqualToString:_confirmPswdField.textField.text]) {
        UIAlertView *reAlert=[[UIAlertView alloc]initWithTitle:nil message:@"两次输入密码不一致" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [reAlert show];
        return;
    }
    [NewLoadingView showHUDAddedTo:self.view animated:YES].labelText=@"提交中，请稍候...";
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahChangePwdResponseMethod:) name:@"com.golf.ahChangePwdMethod" object:nil];
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"user/changepwd" forKey:@"cmd"];
    [dic setObject:_smsTokenField.textField.text forKey:@"smstoken"];
    [dic setObject:_pswdField.textField.text forKey:@"passwd"];
    
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"user/changepwd" andNotificationName:@"com.golf.ahChangePwdMethod" andViewControler:self.view];
}
-(void)ahChangePwdResponseMethod:(NSNotification *)notification
{
    NSLog(@"修改密码回调＝＝＝%@",[notification object]);
    NSNumber *codeNum=[[notification object]objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        UIAlertView *successAlert=[[UIAlertView alloc]initWithTitle:nil message:@"修改密码成功" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [successAlert show];
    }
    else
    {
        UIAlertView *failAlert=[[UIAlertView alloc]initWithTitle:nil message:@"修改密码失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [failAlert show];
    }
    [NewLoadingView hideHUDForView:self.view animated:YES];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahChangePwdMethod" object:nil];
}
-(void)smsTokenMethod
{
    [NewLoadingView showHUDAddedTo:self.view animated:YES].labelText=@"获取中，请稍候...";
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahSmstokenResponseMethod:) name:@"com.golf.ahSmstokenMethod" object:nil];
    NSString *phone=[[[_sqlUtils query_loginInfo_tab]objectAtIndex:0] objectForKey:@"phone"];
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"user/smstoken" forKey:@"cmd"];
    [dic setObject:phone forKey:@"phone"];
    
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"user/smstoken" andNotificationName:@"com.golf.ahSmstokenMethod" andViewControler:self.view];
}
-(void)ahSmstokenResponseMethod:(NSNotification *)notification
{
    NSLog(@"获取验证码回调＝=＝%@",[notification object]);
    NSNumber *codeNum=[[notification object]objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        NSString *phone=[[[_sqlUtils query_loginInfo_tab]objectAtIndex:0] objectForKey:@"phone"];
        UIAlertView *successAlert=[[UIAlertView alloc]initWithTitle:nil message:[@"短信成功发送至" stringByAppendingString:phone] delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [successAlert show];
    }
    else
    {
        UIAlertView *failAlert=[[UIAlertView alloc]initWithTitle:nil message:@"获取验证码失败，请重新获取" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [failAlert show];
    }
    [NewLoadingView hideHUDForView:self.view animated:YES];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahSmstokenMethod" object:nil];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
@end
