//
//  MyAccountHomeViewController.m
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "MyAccountHomeViewController.h"
#import "FSSysConfig.h"
#import "AccountCell.h"
#import "GolfTabBarController.h"
#import "AccountInfoViewController.h"

@interface MyAccountHomeViewController ()

@end

@implementation MyAccountHomeViewController

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
    self.title=@"我的帐户";
    self.section1Array=@[@"帐户信息",@"我的会员卡",@"我的成绩"];
    self.section2Array=@[@"帐户充值",@"消费明细",@"订单明细"];
    _httpUtils=[[HttpUtils alloc]init];
    _sqlUtils=[[SQLUtilsObject alloc]init];
    if (IOS_VERSION>=7.0) {
        self.automaticallyAdjustsScrollViewInsets=NO;
    }
    _accountTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64-49) style:UITableViewStyleGrouped];
    _accountTable.backgroundColor=[UIColor clearColor];
    _accountTable.delegate=self;
    _accountTable.dataSource=self;
    _accountTable.rowHeight=40;
    _accountTable.separatorColor=[UIColor clearColor];
    [self.view addSubview:_accountTable];
}

-(void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    //帐户信息（修改密码），，我的会员卡，我的成绩
    //    帐户充值，消费明细，订单明细
    //    退出帐户
    return 3;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section==0) {
        return 3;
    }
    else if (section==1)
    {
        return 3;
    }
    else
    {
        return 1;
    }
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier=@"myaccountCell";
    AccountCell *cell=[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        cell=[[AccountCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    if ([indexPath section]==0) {
        switch ([indexPath row]) {
            case 0:
            {
                cell.backgroundView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"setting_up"]];
                break;
            }
            case 1:
            {
                cell.backgroundView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"setting_middle"]];
                break;
            }
            case 2:
            {
                cell.backgroundView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"setting_down"]];
                break;
            }
            default:
                break;
        }
        cell.titleLabel.text=[_section1Array objectAtIndex:[indexPath row]];
    }
    else if ([indexPath section]==1)
    {
        switch ([indexPath row]) {
            case 0:
            {
                cell.backgroundView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"setting_up"]];
                break;
            }
            case 1:
            {
                cell.backgroundView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"setting_middle"]];
                break;
            }
            case 2:
            {
                cell.backgroundView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"setting_down"]];
                break;
            }
            default:
                break;
        }
        cell.titleLabel.text=[_section2Array objectAtIndex:[indexPath row]];
    }
    else
    {
        UIView *tempView = [[UIView alloc]initWithFrame:cell.frame];
        [cell setBackgroundView:tempView];
        [cell setBackgroundColor:[UIColor clearColor]];
        UIButton  *exitLoginBtn=[UIButton buttonWithType:UIButtonTypeCustom];
        exitLoginBtn.frame=CGRectMake(20, 0, SCREEN_WIDTH-40, 40);
        [exitLoginBtn setTitle:@"退出帐户" forState:UIControlStateNormal];
        [exitLoginBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [exitLoginBtn setBackgroundImage:[UIImage imageNamed:@"btn_logout"] forState:UIControlStateNormal];
        [exitLoginBtn addTarget:self action:@selector(exitAction) forControlEvents:UIControlEventTouchUpInside];
        cell.accessoryType=UITableViewCellAccessoryNone;
        [cell.contentView addSubview:exitLoginBtn];
    }
    
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    switch ([indexPath section]) {
        case 0:
        {
            switch ([indexPath row]) {
                case 0:
                {
                    AccountInfoViewController *info=[[AccountInfoViewController alloc]init];
                    self.navigationController.hidesBottomBarWhenPushed=YES;
                    [self.navigationController pushViewController:info animated:YES];
                    break;
                }
                case 1:
                {
                    break;
                }
                case 2:
                {
                    break;
                }
                default:
                    break;
            }
            break;
        }
        case 1:
        {
            break;
        }
            
        default:
            break;
    }
    
}
-(void)exitAction
{
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(ahLogoutResponseMethod:) name:@"com.golf.ahLogoutMethod" object:nil];
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"user/logout" forKey:@"cmd"];
    
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"user/logout" andNotificationName:@"com.golf.ahLogoutMethod" andViewControler:self.view];
}
-(void)ahLogoutResponseMethod:(NSNotification *)notification
{
    NSLog(@"退出登录回调===%@",[notification object]);
    NSNumber *codeNum=[[notification object]objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        [_sqlUtils delete_loginInfo_tab];
        [[FSSysConfig getInstance]setIsLogin:NO];
        GolfTabBarController *golfTab=(GolfTabBarController*)self.navigationController.tabBarController;
        [golfTab _butttonActionWithtag4];
    }
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahLogoutMethod" object:nil];
}
@end
