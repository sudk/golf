//
//  WriteOrderViewController.m
//  golf
//
//  Created by mahh on 14-5-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "WriteOrderViewController.h"
#import "FSSysConfig.h"
#import "AHLoginViewController.h"
#import "ConfirmViewController.h"

@interface WriteOrderViewController ()

@end

@implementation WriteOrderViewController

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
    self.title=@"填写订单";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    self.view.backgroundColor=[Utils colorWithHexString:@"#edebf3"];
    _pNum=1;
    _sqlUtils=[[SQLUtilsObject alloc]init];
    _httpUtils=[[HttpUtils alloc]init];
    NSArray *loginInfo=[_sqlUtils query_loginInfo_tab];
    if ([loginInfo count]>0) {
        _personName=[[loginInfo objectAtIndex:0] objectForKey:@"user_name"];
        _personPhn=[[loginInfo objectAtIndex:0] objectForKey:@"phone"];
    }
    
    _orderTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64) style:UITableViewStyleGrouped];
    _orderTable.delegate=self;
    _orderTable.dataSource=self;
    _orderTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:_orderTable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -TableviewDelegate
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 4;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath section]==2) {
        return 65;
    }
    else
    {
        return 40;
    }
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section==0) {
        return 2;
    }
    else if (section==1)
    {
        return 3;
    }
    else if (section==2)
    {
        return 1;
    }
    else
    {
        return 1;
    }
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier=@"orderTableviewcell";
    UITableViewCell *cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    cell.backgroundColor=[Utils colorWithHexString:@"#fcfcfc"];
    switch ([indexPath section]) {
        case 0:
        {
            switch ([indexPath row]) {
                case 0:
                {
                    UILabel *nameLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 36)];
                    nameLabel.backgroundColor=[UIColor clearColor];
                    nameLabel.text=_courtName;
                    [cell.contentView addSubview:nameLabel];
                    break;
                }
                case 1:
                {
                    UILabel *timeLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 36)];
                    timeLabel.backgroundColor=[UIColor clearColor];
                    timeLabel.text=[@"打球时间:" stringByAppendingString:_courtTime];
                    [cell.contentView addSubview:timeLabel];
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
                    UILabel *personsLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, 80, 36)];
                    personsLabel.backgroundColor=[UIColor clearColor];
                    personsLabel.text=@"打球人数";
                    [cell.contentView addSubview:personsLabel];
                    
                    _removepBtn=[[UIButton alloc]initWithFrame:CGRectMake(105, 3.5, 31, 33)];
                    [_removepBtn setImage:[UIImage imageNamed:@"f_decs"] forState:UIControlStateNormal];
                    [_removepBtn setEnabled:NO];
                    [_removepBtn addTarget:self action:@selector(removepMethod) forControlEvents:UIControlEventTouchUpInside];
                    [cell.contentView addSubview:_removepBtn];
                    
                    _pNumLabel=[[UILabel alloc]initWithFrame:CGRectMake(136, 3.5, 40, 33)];
                    _pNumLabel.text=[NSString stringWithFormat:@"%d人",_pNum];
                    _pNumLabel.backgroundColor=[Utils colorWithHexString:@"#dedede"];
                    [cell.contentView addSubview:_pNumLabel];
                    
                    _addpBtn=[[UIButton alloc]initWithFrame:CGRectMake(176, 3.5, 31, 33)];
                    [_addpBtn setImage:[UIImage imageNamed:@"f_add"] forState:UIControlStateNormal];
                    [_addpBtn addTarget:self action:@selector(addpMethod) forControlEvents:UIControlEventTouchUpInside];
                    [cell.contentView addSubview:_addpBtn];
                    break;
                }
                case 1:
                {
                    UILabel *personNameLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, 100, 36)];
                    personNameLabel.backgroundColor=[UIColor clearColor];
                    personNameLabel.text=@"打球人姓名";
                    [cell.contentView addSubview:personNameLabel];
                    
                    _pNameField=[[UITextField alloc]initWithFrame:CGRectMake(115, 2, 190, 36)];
                    _pNameField.delegate=self;
                    if (_personName==nil||[_personName isEqualToString:@""]) {
                        _pNameField.placeholder=@"请填写打球人名字";
                    }
                    else
                    {
                        _pNameField.text=_personName;
                    }
                    [cell.contentView addSubview:_pNameField];
                    break;
                }
                case 2:
                {
                    UILabel *personPhnLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, 80, 36)];
                    personPhnLabel.backgroundColor=[UIColor clearColor];
                    personPhnLabel.text=@"手机号码";
                    [cell.contentView addSubview:personPhnLabel];
                    
                    _phnField=[[UITextField alloc]initWithFrame:CGRectMake(115, 2, 190, 36)];
                    _phnField.delegate=self;
                    if (_personPhn==nil) {
                        _phnField.placeholder=@"用于接收通知短信";
                    }
                    else
                    {
                        _phnField.text=_personPhn;
                    }
                    [cell.contentView addSubview:_phnField];
                    break;
                }
                    
                default:
                    break;
            }
            break;
        }
        case 2:
        {
            UILabel *priceLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 7.5, 80, 30)];
            priceLabel.text=@"订单总价";
            priceLabel.backgroundColor=[UIColor clearColor];
            [cell.contentView addSubview:priceLabel];
            
            UILabel *priceTextLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 37.5, 80, 30)];
            priceTextLabel.text=[@"￥" stringByAppendingString:[NSString stringWithFormat:@"%.0f",[_courtPrice floatValue]*_pNum/100]];
            priceTextLabel.textColor=[UIColor orangeColor];
            priceTextLabel.backgroundColor=[UIColor clearColor];
            [cell.contentView addSubview:priceTextLabel];
            
            NSString *pay_typeStr;
            if ([_courtType intValue]==0) {
                pay_typeStr=@"现付";
            }
            else if ([_courtType intValue]==1)
            {
                pay_typeStr=@"全额预付";
            }
            else
            {
                pay_typeStr=@"押金";
            }
            UILabel *priceTypeLabel=[[UILabel alloc]initWithFrame:CGRectMake(100, 15, 190, 35)];
            priceTypeLabel.text=[pay_typeStr stringByAppendingString:priceTextLabel.text];
            priceTypeLabel.backgroundColor=[UIColor clearColor];
            [cell.contentView addSubview:priceTypeLabel];
            break;
        }
        case 3:
        {
            UIView *tempView = [[UIView alloc]initWithFrame:cell.frame];
            [cell setBackgroundView:tempView];
            [cell setBackgroundColor:[UIColor clearColor]];
            UIButton  *sureBtn=[UIButton buttonWithType:UIButtonTypeCustom];
            sureBtn.frame=CGRectMake(12, 0, SCREEN_WIDTH-24, 40);
            [sureBtn setTitle:@"确认并预定" forState:UIControlStateNormal];
            [sureBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [sureBtn setBackgroundColor:[UIColor orangeColor]];
            [sureBtn addTarget:self action:@selector(sureMethod) forControlEvents:UIControlEventTouchUpInside];
            cell.accessoryType=UITableViewCellAccessoryNone;
            [cell.contentView addSubview:sureBtn];
            break;
        }
        default:
            break;
    }
    return cell;
}
-(void)removepMethod
{
    if (_pNum>1) {
        _pNum-=1;
        _pNumLabel.text=[NSString stringWithFormat:@"%d人",_pNum];
        if (_pNum==1) {
            [_removepBtn setImage:[UIImage imageNamed:@"f_decs"] forState:UIControlStateNormal];
            [_removepBtn setEnabled:NO];
        }
        else if (_pNum==golfMaxPerson-1)
        {
            [_addpBtn setEnabled:YES];
            [_addpBtn setImage:[UIImage imageNamed:@"f_add"] forState:UIControlStateNormal];
        }
    }
}
-(void)addpMethod
{
    if (_pNum<golfMaxPerson) {
        _pNum+=1;
        _pNumLabel.text=[NSString stringWithFormat:@"%d人",_pNum];
        if (_pNum==2) {
            [_removepBtn setEnabled:YES];
            [_removepBtn setImage:[UIImage imageNamed:@"f_dec"] forState:UIControlStateNormal];
        }
        if (_pNum==golfMaxPerson) {
            [_addpBtn setEnabled:NO];
            [_addpBtn setImage:[UIImage imageNamed:@"f_add_s"] forState:UIControlStateNormal];
        }
    }
}
-(void)sureMethod
{
    if (_pNameField.text.length==0) {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:@"填写打球人名字" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    if (_phnField.text.length==0) {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:@"填写手机号码" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"order/create" forKey:@"cmd"];
    [dic setObject:_orderType forKey:@"type"];
    [dic setObject:_relation_id forKey:@"relation_id"];
    [dic setObject:_courtName forKey:@"relation_name"];
    [dic setObject:_courtTime forKey:@"tee_time"];
    [dic setObject:[NSString stringWithFormat:@"%d",_pNum] forKey:@"count"];
    [dic setObject:[NSString stringWithFormat:@"%d",[_courtPrice intValue]] forKey:@"unitprice"];
    [dic setObject:[NSString stringWithFormat:@"%d",[_courtPrice intValue]*_pNum] forKey:@"amount"];
    [dic setObject:_courtType forKey:@"pay_type"];
    [dic setObject:_pNameField.text forKey:@"contact"];
    [dic setObject:_phnField.text forKey:@"phone"];
    [dic setObject:_agent_id forKey:@"agent_id"];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(golfCreateOrderMethod:) name:@"com.golf.golfCreateOrderMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"court/create" andNotificationName:@"com.golf.golfCreateOrderMethod" andViewControler:nil];
}
-(void)golfCreateOrderMethod:(NSNotification*)notification
{
    NSDictionary *createOrderDic=[notification object];
    NSLog(@"生成订单结果回调====%@",createOrderDic);
    NSNumber *statusNum=[createOrderDic objectForKey:@"status"];
    if ([statusNum intValue]==0) {
        NSString *orderId=[[[createOrderDic objectForKey:@"data"] objectAtIndex:0] objectForKey:@"order_id"];
        [[FSSysConfig getInstance]setNowOrderId:orderId];
        if ([[FSSysConfig getInstance]isLogin]==NO) {
            [[FSSysConfig getInstance]setIsFromWriteOrderLogin:YES];
            AHLoginViewController *login=[[AHLoginViewController alloc] init];
            [self.navigationController pushViewController:login animated:YES];
        }
        else
        {
            ConfirmViewController *confirm=[[ConfirmViewController alloc]init];
            confirm.order_id=orderId;
            [self.navigationController pushViewController:confirm animated:YES];
        }
    }
    else
    {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:nil message:[createOrderDic objectForKey:@"desc"] delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
    }
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"com.golf.golfCreateOrderMethod" object:nil];
    
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
@end
