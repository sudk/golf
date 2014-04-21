//
//  AccountInfoViewController.m
//  golf
//
//  Created by mahh on 14-4-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "AccountInfoViewController.h"
#import "AccountInfoCell.h"
#import "ChangePswdViewController.h"

@interface AccountInfoViewController ()

@end

@implementation AccountInfoViewController

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
    self.title=@"帐户信息";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    self.titleArray=@[@"姓名:",@"绑定手机:",@"帐户余额:",@"帐户积分:",@"修改密码"];
    _contentArray=[NSMutableArray array];
    _sqlUtils=[[SQLUtilsObject alloc]init];
    NSArray *tmpArray=[_sqlUtils query_loginInfo_tab];
    [_contentArray addObject:[[tmpArray objectAtIndex:0]objectForKey:@"user_name"]];
    [_contentArray addObject:[[tmpArray objectAtIndex:0]objectForKey:@"phone"]];
    NSString *balanceStr=[[tmpArray objectAtIndex:0]objectForKey:@"balance"];
    if (balanceStr==nil||[balanceStr isEqualToString:@""]) {
        balanceStr=@"0";
    }
    [_contentArray addObject:[balanceStr stringByAppendingString:@"元"]];
    NSString *pointStr=[[tmpArray objectAtIndex:0]objectForKey:@"point"];
    if (pointStr==nil||[pointStr isEqualToString:@""]) {
        pointStr=@"0";
    }
    [_contentArray addObject:[pointStr stringByAppendingString:@"分"]];
    if (IOS_VERSION>=7.0) {
        self.automaticallyAdjustsScrollViewInsets=NO;
    }
    _infoTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64) style:UITableViewStylePlain];
    _infoTable.delegate=self;
    _infoTable.dataSource=self;
    _infoTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:_infoTable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_titleArray count];
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier=@"accountInfoCell";
    AccountInfoCell *cell=[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        cell=[[AccountInfoCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    cell.titleLabel.text=[_titleArray objectAtIndex:[indexPath row]];
    if ([indexPath row]!=[_titleArray count]-1) {
        cell.contentLabel.text=[_contentArray objectAtIndex:[indexPath row]];
    }
    else
    {
        cell.accessoryType=UITableViewCellAccessoryDisclosureIndicator;
    }
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath row]==[_titleArray count]-1) {
        ChangePswdViewController *changePswd=[[ChangePswdViewController alloc]init];
        [self.navigationController pushViewController:changePswd animated:YES];
    }
}
@end
