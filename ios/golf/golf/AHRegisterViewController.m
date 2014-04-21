//
//  AHRegisterViewController.m
//  AiHuan
//
//  Created by xingdd on 14-4-15.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "AHRegisterViewController.h"
#import "NewLoadingView.h"
#import "FSSysConfig.h"
#import "Utils.h"
#import "AppDelegate.h"

@interface AHRegisterViewController ()

@end

@implementation AHRegisterViewController

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
    self.title = @"注册";
    self.sexArray=@[@"男",@"女"];
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _httpUtils=[[HttpUtils alloc]init];
    _phnField=[[NMTextField alloc]initWithFrame:CGRectMake(20, 30, SCREEN_WIDTH-40, 40) WithText:@"手机号" WithRightView:nil withFlg:@"lone1"];
    _phnField.textField.placeholder=@"请输入手机号";
    _phnField.textField.text=@"13335130151";
    _phnField.textField.delegate=self;
    _phnField.textField.borderStyle=UITextBorderStyleRoundedRect;
    _phnField.textField.returnKeyType = UIReturnKeyDone;
    [self.view addSubview:_phnField];
    
    _pswdField=[[NMTextField alloc]initWithFrame:CGRectMake(20, _phnField.frame.origin.y+_phnField.frame.size.height+10, SCREEN_WIDTH-40, 40) WithText:@"密码" WithRightView:nil withFlg:@"lone1"];
    _pswdField.textField.placeholder=@"请输入密码";
    _pswdField.textField.text=@"123";
    _pswdField.textField.secureTextEntry=YES;
    _pswdField.textField.delegate=self;
    _pswdField.textField.borderStyle=UITextBorderStyleRoundedRect;
    _pswdField.textField.returnKeyType = UIReturnKeyDone;
    [self.view addSubview:_pswdField];
    
    _user_nameField=[[NMTextField alloc]initWithFrame:CGRectMake(20,_pswdField.frame.origin.y+_pswdField.frame.size.height+10, SCREEN_WIDTH-40, 40) WithText:@"用户名" WithRightView:nil withFlg:@"lone1"];
    _user_nameField.textField.delegate=self;
    _user_nameField.textField.borderStyle=UITextBorderStyleRoundedRect;
    _user_nameField.textField.returnKeyType = UIReturnKeyDone;
    [self.view addSubview:_user_nameField];
    _card_noField=[[NMTextField alloc]initWithFrame:CGRectMake(20, _user_nameField.frame.origin.y+_user_nameField.frame.size.height+10, SCREEN_WIDTH-40, 40) WithText:@"会员卡号" WithRightView:nil withFlg:@"lone1"];
    _card_noField.textField.delegate=self;
    _card_noField.textField.borderStyle=UITextBorderStyleRoundedRect;
    _card_noField.textField.returnKeyType = UIReturnKeyDone;
    [self.view addSubview:_card_noField];
    _emailField=[[NMTextField alloc]initWithFrame:CGRectMake(20, _card_noField.frame.origin.y+_card_noField.frame.size.height+10, SCREEN_WIDTH-40, 40) WithText:@"邮箱" WithRightView:nil withFlg:@"lone1"];
    _emailField.textField.delegate=self;
    _emailField.textField.borderStyle=UITextBorderStyleRoundedRect;
    _emailField.textField.returnKeyType = UIReturnKeyDone;
    [self.view addSubview:_emailField];
    UILabel *sexLabel=[[UILabel alloc]initWithFrame:CGRectMake(20, _emailField.frame.origin.y+_emailField.frame.size.height+10, 40, 40)];
    sexLabel.text=@"性别";
    sexLabel.backgroundColor=[UIColor clearColor];
    [self.view addSubview:sexLabel];
    
    _sexBtn=[self createCustomBtn];
    [_sexBtn setTitle:@"男" forState:UIControlStateNormal];
    [self.view addSubview:_sexBtn];
    
    //    _sexField=[[NMTextField alloc]initWithFrame:CGRectMake(20, _emailField.frame.origin.y+_emailField.frame.size.height+10, SCREEN_WIDTH-40, 40) WithText:@"性别" WithRightView:nil withFlg:@"lone1"];
    //    _sexField.textField.delegate=self;
    //    _sexField.textField.borderStyle=UITextBorderStyleRoundedRect;
    //    _sexField.textField.returnKeyType = UIReturnKeyDone;
    //    [self.view addSubview:_sexField];
    
    _vcodeField=[[UITextField alloc]initWithFrame:CGRectMake(20, _pswdField.frame.origin.y+_pswdField.frame.size.height+15, (SCREEN_WIDTH-40)/2, 50)];
    _vcodeField.placeholder=@"请输入验证码";
    _vcodeField.delegate=self;
    _vcodeField.borderStyle=UITextBorderStyleRoundedRect;
    _vcodeField.returnKeyType = UIReturnKeyDone;
    //    [self.view addSubview:_vcodeField];
    
    self.vcodeBtn=[[UIButton alloc]initWithFrame:CGRectMake(_vcodeField.frame.origin.x+_vcodeField.frame.size.width+2, _vcodeField.frame.origin.y, 120, 40)];
    [_vcodeBtn setTitle:@"获取验证码" forState:UIControlStateNormal];
    [_vcodeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [_vcodeBtn addTarget:self action:@selector(vcodeMethod) forControlEvents:UIControlEventTouchUpInside];
    //    [self.view addSubview:_vcodeBtn];
    
    _registBtn=[[UIButton alloc]initWithFrame:CGRectMake(30, _sexBtn.frame.origin.y+_sexBtn.frame.size.height+20, SCREEN_WIDTH-60 , 40)];
    [_registBtn setTitle:@"提交" forState:UIControlStateNormal];
    [_registBtn setEnabled:NO];
    [_registBtn setBackgroundColor:[UIColor grayColor]];
    [_registBtn addTarget:self action:@selector(registMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_registBtn];
}

-(void)viewWillAppear:(BOOL)animated
{
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(UIButton*)createCustomBtn
{
    UIButton *customBtn=[[UIButton alloc]initWithFrame:CGRectMake(65, _emailField.frame.origin.y+_emailField.frame.size.height+10, SCREEN_WIDTH-40-65, 40)];
    [customBtn setBackgroundImage:[UIImage imageNamed:@"selectList.png"] forState:UIControlStateNormal];
    [customBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [customBtn addTarget:self action:@selector(customSelect:) forControlEvents:UIControlEventTouchUpInside];
    return customBtn;
}
- (void)registMethod
{
    [NewLoadingView showHUDAddedTo:self.view animated:YES].labelText=@"注册中，请稍候...";
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahRegistMethod:) name:@"com.golf.ahRegistMethod" object:nil];
    
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"user/registe" forKey:@"cmd"];
    [dic setObject:_phnField.textField.text forKey:@"phone"];
    ;
    [dic setObject:_pswdField.textField.text forKey:@"passwd"];
    [dic setObject:_user_nameField.textField.text forKey:@"user_name"];
    [dic setObject:_card_noField.textField.text forKey:@"card_no"];
    ;
    [dic setObject:_emailField.textField.text forKey:@"email"];
    [dic setObject:_sexBtn.titleLabel.text forKey:@"sex"];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"register" andNotificationName:@"com.golf.ahRegistMethod" andViewControler:self.view];
}

-(void)ahRegistMethod:(NSNotification*)notification
{
    NSDictionary *registDic=[notification object];
    NSLog(@"注册成功回调====%@",registDic);
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahRegistMethod" object:nil];
    NSNumber *codeNum=[registDic objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        [[FSSysConfig getInstance]setIsLogin:YES];
        [[FSSysConfig getInstance]setLoginAccount:[registDic objectForKey:@"user_name"]];
        _sqlUtils=[[SQLUtilsObject alloc]init];
        NSArray *loginArray=[_sqlUtils query_loginInfo_tab];
        NSLog(@"loginArray====%@",loginArray);
        if ([loginArray count]>0) {
            [_sqlUtils delete_loginInfo_tab];
        }
        [_sqlUtils insert_loginInfo_tab:_phnField.textField.text andPswd:_pswdField.textField.text andBanlance:[registDic objectForKey:@"balance"] andCard_no:[registDic objectForKey:@"card_no"] andEmail:[registDic objectForKey:@"email"] andPoint:[registDic objectForKey:@"point"] andSex:[registDic objectForKey:@"sex"] andUser_name:[registDic objectForKey:@"user_name"] andRemark:[registDic objectForKey:@"remark"]];
        [self.navigationController popViewControllerAnimated:YES];
    }
    else
    {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:[registDic objectForKey:@"desc"] delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        [NewLoadingView hideHUDForView:self.view animated:YES];
        return;
    }
    [NewLoadingView hideHUDForView:self.view animated:YES];
    
}
- (void)displayNotification {
    if (self.notify.isAnimating) return;
    [self.view addSubview:self.notify];
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
-(void)vcodeMethod
{
    _second=1;
    NSTimer *timer=[NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(changeBtnTitle) userInfo:nil repeats:YES];
    self.repeatTimer=timer;
    [_vcodeBtn setEnabled:NO];
    //调用获取验证码接口
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahGetvcodeMethod:) name:@"com.golf.ahGetvcodeMethod" object:nil];
    
    
    //检查网络连接
    self.notificationText=networkAbnormalInfo;
    [self displayNotification];
    [NewLoadingView hideHUDForView:self.view animated:YES];
    return;
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"getvcode" forKey:@"method"];
    [dic setObject:_phnField.textField.text forKey:@"phn"];
    [dic setObject:[[[[FSSysConfig getInstance]keyDic]allKeys]objectAtIndex:0] forKey:@"keyid"];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"register" andNotificationName:@"com.golf.ahGetvcodeMethod" andViewControler:nil];
}
-(void)changeBtnTitle
{
    if (_second==60) {
        [_repeatTimer invalidate];
        _second=1;
        [_vcodeBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        [_vcodeBtn setEnabled:YES];
    }
    else
    {
        int tmpSecond=60-_second;
        NSString *titleStr=[[@"重新获取" stringByAppendingString:[NSString stringWithFormat:@"%d",tmpSecond]] stringByAppendingString:@"s"];
        [_vcodeBtn setTitle:titleStr forState:UIControlStateNormal];
        _second++;
    }
}
-(void)ahGetvcodeMethod:(NSNotification*)notification
{
    NSLog(@"获取验证码回调===%@",[notification object]);
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahGetvcodeMethod" object:nil];
}
#pragma mark -UITextFieldDelegate
- (void)textFieldDidEndEditing:(UITextField *)textField
{
    if (_phnField.textField.text.length>0&&_pswdField.textField.text.length>0) {
        [_registBtn setBackgroundColor:[UIColor orangeColor]];
        [_registBtn setEnabled:YES];
    }
    else
    {
        [_registBtn setEnabled:NO];
    }
}


//在关闭键盘时恢复位置
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    if (textField==_card_noField.textField||textField==_emailField.textField) {
        NSTimeInterval animationDuration = 0.30f;
        [UIView beginAnimations:@"ResizeForKeyboard" context:nil];
        [UIView setAnimationDelay:animationDuration];
        float Y=0;
        if (IOS_VERSION>= 7.0) {
            Y=64;
        }
        self.view.frame = CGRectMake(0, Y, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
        [UIView commitAnimations];
    }
    //    if ([textField isEqual:_phnField.textField]) {
    //        [_pswdField.textField becomeFirstResponder];
    //    }
    //    else if ([textField isEqual:_pswdField.textField])
    //    {
    //        [_user_nameField.textField becomeFirstResponder];
    //    }
    //    else if ([textField isEqual:_user_nameField.textField])
    //    {
    //        [_card_noField.textField becomeFirstResponder];
    //    }
    //    else if ([textField isEqual:_card_noField.textField])
    //    {
    //        [_emailField.textField becomeFirstResponder];
    //    }
    //    else if ([textField isEqual:_emailField.textField])
    //    {
    //        [_sexField.textField becomeFirstResponder];
    //    }
    [textField resignFirstResponder];
    return YES;
}
//打开键盘时调整位置
-(void)textFieldDidBeginEditing:(UITextField *)textField{
    if (textField == _card_noField.textField) {
        [self adjustFrame:-60];
    }
    else if (textField==_emailField.textField)
    {
        [self adjustFrame:-60];
    }
}
-(void)adjustFrame:(float)Y
{
    NSTimeInterval animationDuration = 0.30f;
    [UIView beginAnimations:@"ResizeForKeyBoard" context:nil];
    [UIView setAnimationDuration:animationDuration];
    float width = self.view.frame.size.width;
    float height = self.view.frame.size.height;
    CGRect rect = CGRectMake(0.0f, Y, width, height);
    self.view.frame = rect;
    [UIView commitAnimations];
}
-(IBAction)customSelect:(id)sender
{
    _sexPickerView=[[TBPickerView alloc] initWithFrame:CGRectMake(0,SCREEN_HEIGHT, SCREEN_WIDTH, 260) AndDataArray:_sexArray];
    _sexPickerView.backgroundColor=[UIColor whiteColor];
    _sexPickerView.delegate=self;
    [self.view addSubview:_sexPickerView];
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.3];
    _sexPickerView.frame=CGRectMake(0,SCREEN_HEIGHT-260, SCREEN_WIDTH, 260);
    [UIView commitAnimations];
}
#pragma mark -TBPickerViewDelegate
- (void)pickerView:(TBPickerView *)pickerView didSelectRow:(NSInteger)row{
        [_sexBtn setTitle:[_sexArray objectAtIndex:row] forState:UIControlStateNormal];
}
- (void)confirmpickerView:(TBPickerView *)pickerView didSelectRow:(NSInteger)row{
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.3];
    _sexPickerView.frame=CGRectMake(0, SCREEN_HEIGHT*2, SCREEN_WIDTH, 260);
    [UIView commitAnimations];
    NSLog(@"_sexBtn.titleLabel.text====%@",_sexBtn.titleLabel.text);
   
}

@end
