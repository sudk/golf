//
//  ConfirmViewController.m
//  golf
//
//  Created by mahh on 14-5-3.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "ConfirmViewController.h"

@interface ConfirmViewController ()

@end

@implementation ConfirmViewController

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
    self.title=@"订单";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _httpUtils=[[HttpUtils alloc]init];
    self.view.backgroundColor=[Utils colorWithHexString:@"#edebf3"];
    [self showOrderInfo];
    [self updateOrderTableInfo];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)updateOrderTableInfo
{
    if (_orderInfoTable==nil) {
        _orderInfoTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64) style:UITableViewStyleGrouped];
        _orderInfoTable.delegate=self;
        _orderInfoTable.dataSource=self;
        _orderInfoTable.backgroundColor=[UIColor clearColor];
        [self.view addSubview:_orderInfoTable];
    }
    else
    {
        [_orderInfoTable reloadData];
    }
    
}
-(void)showOrderInfo
{
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"order/info" forKey:@"cmd"];
    [dic setObject:_order_id forKey:@"order_id"];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(golfShowOrderInfoMethod:) name:@"com.golf.golfShowOrderInfoMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"order/info" andNotificationName:@"com.golf.golfShowOrderInfoMethod" andViewControler:nil];
}
-(void)golfShowOrderInfoMethod:(NSNotification *)notificatin
{
    NSDictionary *orderInfoDic=[notificatin object];
    NSLog(@"订单信息====%@",orderInfoDic);
    NSNumber *statusNum=[orderInfoDic objectForKey:@"status"];
    if ([statusNum intValue]==0) {
        
    }
    else
    {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:[orderInfoDic objectForKey:@"desc"] delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
    }
    
}
#pragma mark -TableViewDelegate
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section==0) {
        return 4;
    }
    else
    {
        return 10;
    }
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell=[[UITableViewCell alloc]init];
    tableView.separatorColor=[UIColor clearColor];
    switch ([indexPath section]) {
        case 0:
        {
            switch ([indexPath row]) {
                case 0:
                {
                    UILabel *agentLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 18)];
                    agentLabel.backgroundColor=[UIColor clearColor];
                    agentLabel.text=@"提供商家  ";
                    [cell.contentView addSubview:agentLabel];
                    break;
                }
                case 1:
                {
                    UILabel *orderStatusLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 18)];
                    orderStatusLabel.backgroundColor=[UIColor clearColor];
                    orderStatusLabel.text=@"订单状态  ";
                    [cell.contentView addSubview:orderStatusLabel];
                    break;
                }
                case 2:
                {
                    UILabel *orderPriceLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 18)];
                    orderPriceLabel.backgroundColor=[UIColor clearColor];
                    orderPriceLabel.text=@"订单总额  ";
                    [cell.contentView addSubview:orderPriceLabel];
                    break;
                }
                case 3:
                {
                    UILabel *orderPayTypeLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 18)];
                    orderPayTypeLabel.backgroundColor=[UIColor clearColor];
                    orderPayTypeLabel.text=@"付款方式  ";
                    [cell.contentView addSubview:orderPayTypeLabel];
                    break;
                }
                default:
                    break;
            }
           break;
        }
        case 1:
        {
            switch ([indexPath row]) {
                case 0:
                {
                    
                    break;
                }
                    
                    
                default:
                    break;
            }
            break;
        }
            
        default:
            break;
    }
    return cell;
}
@end
