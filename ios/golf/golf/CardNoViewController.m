//
//  CardNoViewController.m
//  golf
//
//  Created by mahh on 14-4-21.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "CardNoViewController.h"
#import "Utils.h"
#import "NewLoadingView.h"

@interface CardNoViewController ()

@end

@implementation CardNoViewController

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
    self.title=@"我的会员卡";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _sqlUtils=[[SQLUtilsObject alloc]init];
    _httpUtils=[[HttpUtils alloc]init];
    NSString *cardNo=[[[_sqlUtils query_loginInfo_tab]objectAtIndex:0] objectForKey:@"card_no"];
    if (cardNo==nil||[cardNo isEqualToString:@""]) {
        _card_noField=[[NMTextField alloc]initWithFrame:CGRectMake(20, 30, SCREEN_WIDTH-40, 40) WithText:@"会员卡号" WithRightView:nil withFlg:@"lone1"];
        _card_noField.textField.delegate=self;
        _card_noField.textField.borderStyle=UITextBorderStyleRoundedRect;
        _card_noField.textField.returnKeyType = UIReturnKeyDone;
        [_card_noField.textField becomeFirstResponder];
        [self.view addSubview:_card_noField];
        
        _bindBtn=[[UIButton alloc]initWithFrame:CGRectMake(20, _card_noField.frame.origin.y+_card_noField.frame.size.height+10, SCREEN_WIDTH-40, 40)];
        [_bindBtn setTitle:@"绑定会员卡"forState:UIControlStateNormal];
        [_bindBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_bindBtn setBackgroundColor:[UIColor grayColor]];
        [_bindBtn setEnabled:NO];
        [_bindBtn addTarget:self action:@selector(bindMethod) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:_bindBtn];
    }
    else
    {
        
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
- (void)textFieldDidEndEditing:(UITextField *)textField
{
    if (_card_noField.textField.text.length>0) {
        [_bindBtn setEnabled:YES];
       [_bindBtn setBackgroundColor:[Utils colorWithHexString:@"#118bdf"]];
    }
}
-(void)bindMethod
{
    [NewLoadingView showHUDAddedTo:self.view animated:YES].labelText=@"绑定中，请稍候...";
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahBandCardMethod:) name:@"com.golf.ahBandCardMethod" object:nil];
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"user/bandcard" forKey:@"cmd"];
    [dic setObject:_card_noField.textField.text forKey:@"card_no"];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"user/bandcard" andNotificationName:@"com.golf.ahBandCardMethod" andViewControler:self.view];
}
-(void)ahBandCardMethod:(NSNotification *)notification
{
    NSLog(@"绑定会员卡回调＝＝＝%@",[notification object]);
    NSNumber *codeNum=[[notification object]objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        UIAlertView *successAlert=[[UIAlertView alloc]initWithTitle:nil message:@"绑定成功" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [successAlert show];
    }
    else
    {
        UIAlertView *failAlert=[[UIAlertView alloc]initWithTitle:nil message:[[notification object]objectForKey:@"desc"] delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [failAlert show];
    }
    [NewLoadingView hideHUDForView:self.view animated:YES];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahBandCardMethod" object:nil];
}

@end
