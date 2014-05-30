//
//  CourtDetailViewController.m
//  golf
//
//  Created by mahh on 14-4-10.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "CourtDetailViewController.h"
#import "UIImageView+WebCache.h"
#import "UIButton+WebCache.h"


@interface CourtDetailViewController ()

@end

@implementation CourtDetailViewController

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
    self.title=@"球场详情";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    self.evaluateArray=[NSMutableArray array];
    self.fairwayImgArray=[NSArray array];
    for (int i=0; i<4; i++) {
        [_evaluateArray addObject:@""];
    }
    self.httpUtils=[[HttpUtils alloc]init];
    self.courtImgArray=[NSArray array];
    self.timeArray=@[@"04:00",@"04:30",@"05:00",@"05:30",@"06:00",@"06:30",@"07:00",@"07:30",@"08:00",@"08:30",@"09:00",@"09:30",@"10:00",@"10:30",@"11:00",@"11:30",@"12:00",@"12:30",@"13:00",@"13:30",@"14:00",@"14:30",@"15:00",@"15:30",@"16:00",@"16:30",@"17:00",@"17:30",@"18:00",@"18:30",@"19:00",@"19:30",@"20:00"];
    NSString *dateAndTimeStr=[[_dateStr stringByAppendingString:@" "] stringByAppendingString:_timeStr];
    self.courtDetailArray=[NSMutableArray array];
    [_courtDetailArray addObject:dateAndTimeStr];
    UITableView *tmpcourtDetailTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64) style:UITableViewStyleGrouped];
    self.courtDetailTable=tmpcourtDetailTable;
    [tmpcourtDetailTable release];
    _courtDetailTable.delegate=self;
    _courtDetailTable.dataSource=self;
    _courtDetailTable.contentInset=UIEdgeInsetsMake(-40, 0, 0, 0);
    _courtDetailTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:self.courtDetailTable];
    NSOperationQueue *oprationQueue = [[NSOperationQueue alloc] init];
    NSInvocationOperation *invocation = [[NSInvocationOperation alloc] initWithTarget:self selector:@selector(golfgetCourtInfo) object:nil];
    invocation.queuePriority = NSOperationQueuePriorityHigh;//设置线程优先级
    [oprationQueue addOperation:invocation];
    
    NSInvocationOperation *invocation2 = [[NSInvocationOperation alloc] initWithTarget:self selector:@selector(golfgetComment) object:nil];
    [oprationQueue addOperation:invocation2];
    
    NSInvocationOperation *invocation3 = [[NSInvocationOperation alloc] initWithTarget:self selector:@selector(golfgetPrice) object:nil];
    [oprationQueue addOperation:invocation3];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -TableViewDatabase
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
        return [_courtDetailArray count];
    }
}
-(float)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath section]==0&&[indexPath row]==1) {
        return 165;
    }
    else
    {
        if ([indexPath section]==1&&[indexPath row]!=0&&[indexPath row]!=1) {
            return 65;
        }
        return 40;
    }
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier=@"courtDetailIdentifier";
    UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        cell=[[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier] autorelease];
    }
    while ([cell.contentView.subviews lastObject] != nil) {
        [(UIView*)[cell.contentView.subviews lastObject] removeFromSuperview];  //删除并进行重新分配
    }
    switch ([indexPath section]) {
        case 0:
        {
            switch ([indexPath row]) {
                case 0:
                {
                    [self customLabel:CGRectMake(0, 2, SCREEN_WIDTH, 40) withTag:courtNameLabelTag withCell:cell withText:_courtName withFontSize:18];
                  break;
                }
                case 1:
                {
                    
                    UIView *courtView=[[UIView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 165)];
                    UIButton *courtLargeBtn=[[UIButton alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 165)];
                    [courtLargeBtn  setBackgroundImageWithURL:[NSURL URLWithString:[NSStringSmall  smallSpliceStr:self.courtImgStr]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"defaultImg"]];
                    [courtLargeBtn addTarget:self action:@selector(showImg) forControlEvents:UIControlEventTouchUpInside];
                    [courtView addSubview:courtLargeBtn];
                    UIButton *evaluateBtn=[[UIButton alloc]initWithFrame:CGRectMake(0, 125, SCREEN_WIDTH, 40)];
                    NSString *btnTitle=[[[[[[[@"设计" stringByAppendingString:[_evaluateArray objectAtIndex:0]] stringByAppendingString:@" 草坪"] stringByAppendingString:[_evaluateArray objectAtIndex:1]]stringByAppendingString:@" 设施"] stringByAppendingString:[_evaluateArray objectAtIndex:2]]stringByAppendingString:@" 服务"] stringByAppendingString:[_evaluateArray objectAtIndex:2]];
                    evaluateBtn.titleLabel.font=[UIFont systemFontOfSize:14];
                    evaluateBtn.titleLabel.textAlignment=NSTextAlignmentLeft;
                    [evaluateBtn setTitle:btnTitle forState:UIControlStateNormal];
                    evaluateBtn.alpha=0.5;
                    [evaluateBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
                    [evaluateBtn setBackgroundColor:[UIColor blackColor]];
                    [evaluateBtn addTarget:self action:@selector(evaluateMethod) forControlEvents:UIControlEventTouchUpInside];
                    [courtView addSubview:evaluateBtn];
                    UIImageView *imgv=[[UIImageView alloc]initWithFrame:CGRectMake(270, 127, 36, 36)];
                    imgv.image=[UIImage imageNamed:@"setting_arrow_normal"];
                    [courtView addSubview:imgv];
                    [cell.contentView addSubview:courtView];
                    [imgv release];
                    [courtView release];
                    break;
                }
                case 2:
                {
                    [self customLabel:CGRectMake(3, 3, 210, 40) withTag:addressLabelTag withCell:cell withText:_courtAddress withFontSize:14];
                    UIButton *daoHangBtn=[self customButton:CGRectMake(215, 2, 35, 36) withCell:cell withTitle:@"导航" withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                    [daoHangBtn addTarget:self action:@selector(operateMethod) forControlEvents:UIControlEventTouchUpInside];
                    
                    [self customButton:CGRectMake(255, 3, 45, 40) withCell:cell withTitle:@"" withColor:[UIColor clearColor] withNormalImg:@"setting_arrow_normal" withHighlightedImg:@"setting_arrow_pressed"];
                    break;
                }
                case 3:
                {
                    [self customLabel:CGRectMake(3, 3, 210, 40) withTag:raidersLabelTag withCell:cell withText:self.courtDateStr withFontSize:14];
                    UIButton *showBtn=[self customButton:CGRectMake(215, 2, 35, 36) withCell:cell withTitle:@"查看" withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                    [showBtn addTarget:self action:@selector(showMethod) forControlEvents:UIControlEventTouchUpInside];
                    
                    [self customButton:CGRectMake(255, 3, 45, 40) withCell:cell withTitle:@"" withColor:[UIColor clearColor] withNormalImg:@"setting_arrow_normal" withHighlightedImg:@"setting_arrow_pressed"];
                    break;
                }
                    
                default:
                    break;
            }
            break;
        }
        case 1:
        {
            if ([indexPath row]==0) {
                self.dateBtn=[self customButton:CGRectMake(10, 2, 120, 36) withCell:cell withTitle:_dateStr withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                [_dateBtn addTarget:self action:@selector(showDateMethod) forControlEvents:UIControlEventTouchUpInside];
                self.timeBtn=[self customButton:CGRectMake(180, 2, 70, 36) withCell:cell withTitle:_timeStr withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                [_timeBtn addTarget:self action:@selector(showTimeMethod) forControlEvents:UIControlEventTouchUpInside];
            }
            else if ([indexPath row]==1)
            {
                [self customLabel:CGRectMake(15, 2, 150, 35) withTag:cAgentNameLebelTag withCell:cell withText:[_courtDetailArray objectAtIndex:[indexPath row]] withFontSize:18];
            }
            else
            {
                [self customLabel:CGRectMake(15, 2, 150, 30) withTag:cDetailNameLebelTag withCell:cell withText:[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"court_name"] withFontSize:14];
                NSString *greenStr=@"";
                if ([[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"is_green"] intValue]==1) {
                    greenStr=@"果岭/";
                }
                NSString *caddieStr=@"";
                if ([[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"is_caddie"] intValue]==1) {
                    caddieStr=@"僮/";
                }
                NSString *carStr=@"";
                if ([[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"is_car"] intValue]==1) {
                    carStr=@"车/";
                }
                NSString *wardrobeStr=@"";
                if ([[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"is_wardrobe"] intValue]==1) {
                    carStr=@"柜";
                }
                NSString *serviceStr=[[[greenStr stringByAppendingString:caddieStr] stringByAppendingString:carStr] stringByAppendingString:wardrobeStr];
                [self customLabel:CGRectMake(15, 33, 150, 30) withTag:cDetailFacilityLebelTag withCell:cell withText:serviceStr withFontSize:14];
                [self customLabel:CGRectMake(170, 15, 60, 35) withTag:cDetailPriceLebelTag withCell:cell withText:[@"￥" stringByAppendingString:[NSString stringWithFormat:@"%.0f",[[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"price"] floatValue]/100]] withFontSize:14];
                UIButton *cDetailOrderBtn=[self customButton:CGRectMake(235, 2, 60, 30) withCell:cell withTitle:@"预订" withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                cDetailOrderBtn.tag=[indexPath row];
                [cDetailOrderBtn addTarget:self action:@selector(courtOrderMehtod:) forControlEvents:UIControlEventTouchUpInside];
                NSString *pay_typeStr;
                if ([[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"pay_type"] intValue]==0) {
                    pay_typeStr=@"现付";
                }
                else if ([[[_courtDetailArray objectAtIndex:[indexPath row]] objectForKey:@"pay_type"] intValue]==1)
                {
                    pay_typeStr=@"全额预付";
                }
                else
                {
                    pay_typeStr=@"押金";
                }
                [self customLabel:CGRectMake(235, 33, 80, 30) withTag:cDetailPriceTypeLebelTag withCell:cell withText:pay_typeStr withFontSize:14];
                
            }
            break;
        }
        default:
            break;
    }
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath section]==0&&[indexPath row]==3) {
        [self showMethod];
    }
}
#pragma mark -method
-(UIButton*)customButton:(CGRect)customFrame withCell:(UITableViewCell *)cell withTitle:(NSString *)title withColor:(UIColor*)color withNormalImg:(NSString *)normalImgName withHighlightedImg:(NSString *)highlightedImgName
{
    UIButton *customBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    customBtn.frame=customFrame;
    customBtn.titleLabel.font=[UIFont systemFontOfSize:14];
    [customBtn setTitle:title forState:UIControlStateNormal];
    [customBtn setTitleColor:color forState:UIControlStateNormal];
    [customBtn setBackgroundImage:[UIImage imageNamed:normalImgName] forState:UIControlStateNormal];
    [customBtn setBackgroundImage:[UIImage imageNamed:highlightedImgName] forState:UIControlStateHighlighted];
    [cell.contentView addSubview:customBtn];
    return customBtn;
}
-(void)customLabel:(CGRect)customFrame withTag:(int)customTag withCell:(UITableViewCell *)cell withText:(NSString*)textStr withFontSize:(int)size
{
    UILabel *customLabel=[[UILabel alloc]initWithFrame:customFrame];
    customLabel.backgroundColor=[UIColor clearColor];
    customLabel.font=[UIFont systemFontOfSize:size];
    customLabel.tag=customTag;
    [cell.contentView addSubview:customLabel];
    [customLabel release];
    
    UILabel *customLabelContent=(UILabel *)[cell.contentView viewWithTag:customTag];
    customLabelContent.text=textStr;
}
-(void)operateMethod
{
    LMapViewController *tmplMap=[[LMapViewController alloc]init];
    self.lMap=tmplMap;
    [tmplMap release];
    self.lMap.courtAddress=self.courtAddress;
    [self.navigationController pushViewController:self.lMap.retain animated:NO];
}

-(void)showMethod
{
    CourtDetailInfoViewController *tmpdetailInfo=[[CourtDetailInfoViewController alloc]init];
    self.detailInfo=tmpdetailInfo;
    self.detailInfo.courtInfoArray=self.courtInfoArray;
    self.detailInfo.fairwayImgArray=self.fairwayImgArray;
    self.detailInfo.descInfoStr=self.descInfoStr;
    self.detailInfo.phnStr=self.phnStr;
    self.detailInfo.facilityInfoStr=self.facilityInfoStr;
    [tmpdetailInfo release];
    self.detailInfo.courtId=_courtId;
    [self.navigationController pushViewController:self.detailInfo animated:YES];
}
-(void)showDateMethod
{
//    DSLCalendarView *tmpcalendarView=[[DSLCalendarView alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-20-330, SCREEN_WIDTH, 330)];
    UIView *tmpallCalendarView=[[UIView alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-20-374, SCREEN_WIDTH, 374)];
    self.allCalendarView=tmpallCalendarView;
    [tmpallCalendarView release];
    UIToolbar *toolBar=[[UIToolbar alloc]initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, 44)];
    UIImageView *imgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 44)];
    imgv.image=[UIImage imageNamed:@"title_bg2"];
    [toolBar insertSubview:imgv atIndex:1];
    NSMutableArray *itemArray=[NSMutableArray array];
    UIButton *cancelBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, 2, 60, 40)];
    [cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
    [cancelBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [cancelBtn addTarget:self action:@selector(datePickCancelMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *cancelItem=[[UIBarButtonItem alloc]initWithCustomView:cancelBtn];
    [itemArray addObject:cancelItem];
    UIBarButtonItem *spaceItem=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [itemArray addObject:spaceItem];
    
    [toolBar setItems:itemArray];
    [_allCalendarView addSubview:toolBar];
    DSLCalendarView *tmpcalendarView=[[DSLCalendarView alloc]initWithFrame:CGRectMake(0,44 , SCREEN_WIDTH, 330)];
    self.calendarView=tmpcalendarView;
    [tmpcalendarView release];
    _calendarView.delegate = self;
    _calendarView.backgroundColor=[UIColor whiteColor];
    [self.allCalendarView addSubview:self.calendarView];
    [self.view addSubview:self.allCalendarView];
}
-(void)datePickCancelMethod
{
    if (_allCalendarView!=nil) {
        [_allCalendarView removeFromSuperview];
    }
}
-(void)showTimeMethod
{
    self.timePickView=[[UIView alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-20-49-260, SCREEN_WIDTH, 260)];
    UIToolbar *toolBar=[[UIToolbar alloc]initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, 44)];
    UIImageView *imgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 44)];
    imgv.image=[UIImage imageNamed:@"title_bg2"];
    [toolBar insertSubview:imgv atIndex:1];
    NSMutableArray *itemArray=[NSMutableArray array];
    UIButton *cancelBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, 2, 60, 40)];
    [cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
    [cancelBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [cancelBtn addTarget:self action:@selector(timePickCancelMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *cancelItem=[[UIBarButtonItem alloc]initWithCustomView:cancelBtn];
    [itemArray addObject:cancelItem];
    UIBarButtonItem *spaceItem=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [itemArray addObject:spaceItem];
    UIButton *sureBtn=[[UIButton alloc]initWithFrame:CGRectMake(240, 2, 60, 40)];
    [sureBtn setTitle:@"确定" forState:UIControlStateNormal];
    [sureBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [sureBtn addTarget:self action:@selector(timePickSureMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sureItem=[[UIBarButtonItem alloc]initWithCustomView:sureBtn];
    [itemArray addObject:sureItem];
    [toolBar setItems:itemArray];
    [_timePickView addSubview:toolBar];
    UIPickerView *pick=[[UIPickerView alloc]initWithFrame:CGRectMake(0, 44, SCREEN_WIDTH, 216)];
    pick.backgroundColor=[UIColor whiteColor];
    pick.delegate=self;
    pick.dataSource=self;
    [_timePickView addSubview:pick];
    [self.view addSubview:_timePickView];
    [sureItem release];
    [sureBtn release];
    [cancelItem release];
    [cancelBtn release];
    [spaceItem release];
    [toolBar release];
    [imgv release];

}
//球位预约提示框
-(IBAction)courtOrderMehtod:(id)sender
{
    int tag=(int)[sender tag];
    
    self.view.alpha=0.8;
    [self.view setUserInteractionEnabled:NO];
    UIView *tmpcourtOrderView=[[UIView alloc]initWithFrame:CGRectMake(10, 60, SCREEN_WIDTH-20, SCREEN_HEIGHT-120)];
    self.courtOrderView=tmpcourtOrderView;
    [tmpcourtOrderView release];
    _courtOrderView.backgroundColor=[Utils colorWithHexString:@"#e2ebee"];
    UIButton *closeBtn=[[UIButton alloc]initWithFrame:CGRectMake(_courtOrderView.frame.size.width-40, 5, 30, 30)];
    [closeBtn setImage:[UIImage imageNamed:@"f_close"] forState:UIControlStateNormal];
    [closeBtn addTarget:self action:@selector(closeOrderView) forControlEvents:UIControlEventTouchUpInside];
    [_courtOrderView addSubview:closeBtn];
    UILabel *courtNameLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 9, _courtOrderView.frame.size.width-50, 20)];
    courtNameLabel.backgroundColor=[UIColor clearColor];
    courtNameLabel.text=[[@"由" stringByAppendingString:[[_courtDetailArray objectAtIndex:tag] objectForKey:@"court_name"]] stringByAppendingString:@"提供"];
    self.selectOrderCourtName=[[_courtDetailArray objectAtIndex:tag] objectForKey:@"court_name"];
    courtNameLabel.font=[UIFont systemFontOfSize:16];
    courtNameLabel.textColor=[Utils colorWithHexString:@"#919394"];
    [_courtOrderView addSubview:courtNameLabel];
    
    UILabel *ttimeLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, courtNameLabel.frame.size.height+courtNameLabel.frame.origin.y+4,80, 15)];
    ttimeLabel.backgroundColor=[UIColor clearColor];
    ttimeLabel.textColor=[UIColor blackColor];
    ttimeLabel.text=@"开球时间:";
    ttimeLabel.font=[UIFont systemFontOfSize:14];
    [_courtOrderView addSubview:ttimeLabel];
    
    UILabel *ctimeLabel=[[UILabel alloc]initWithFrame:CGRectMake(90, courtNameLabel.frame.size.height+courtNameLabel.frame.origin.y+4, courtNameLabel.frame.size.width-80, 15)];
    ctimeLabel.backgroundColor=[UIColor clearColor];
    ctimeLabel.textColor=[UIColor blueColor];
    NSDateFormatter *format=[[NSDateFormatter alloc]init];
    [format setDateFormat:@"yyyy-MM-dd"];
    NSString *selectdateStr=[format stringFromDate:_selectDate];
    ctimeLabel.text=[[selectdateStr stringByAppendingString:@"  "] stringByAppendingString:_timeStr];
    self.selectOrderCourtTime=[[selectdateStr stringByAppendingString:@"  "] stringByAppendingString:_timeStr];
    
    ctimeLabel.font=[UIFont systemFontOfSize:14];
    [_courtOrderView addSubview:ctimeLabel];
    
    UILabel *tpriceLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, ctimeLabel.frame.size.height+ctimeLabel.frame.origin.y+4,80, 15)];
    tpriceLabel.backgroundColor=[UIColor clearColor];
    tpriceLabel.textColor=[UIColor blackColor];
    tpriceLabel.text=@"价格包含:";
    tpriceLabel.font=[UIFont systemFontOfSize:14];
    [_courtOrderView addSubview:tpriceLabel];
    
    NSString *greenStr=@"";
    if ([[[_courtDetailArray objectAtIndex:tag] objectForKey:@"is_green"] intValue]==1) {
        greenStr=@"果岭/";
    }
    NSString *caddieStr=@"";
    if ([[[_courtDetailArray objectAtIndex:tag] objectForKey:@"is_caddie"] intValue]==1) {
        caddieStr=@"僮/";
    }
    NSString *carStr=@"";
    if ([[[_courtDetailArray objectAtIndex:tag] objectForKey:@"is_car"] intValue]==1) {
        carStr=@"车/";
    }
    NSString *wardrobeStr=@"";
    if ([[[_courtDetailArray objectAtIndex:tag] objectForKey:@"is_wardrobe"] intValue]==1) {
        carStr=@"柜";
    }
    NSString *serviceStr=[[[greenStr stringByAppendingString:caddieStr] stringByAppendingString:carStr] stringByAppendingString:wardrobeStr];
    UILabel *cpriceLabel=[[UILabel alloc]initWithFrame:CGRectMake(90, ctimeLabel.frame.size.height+ctimeLabel.frame.origin.y+4, courtNameLabel.frame.size.width-80, 15)];
    cpriceLabel.backgroundColor=[UIColor clearColor];
    cpriceLabel.textColor=[UIColor blackColor];
    cpriceLabel.text=serviceStr;
    cpriceLabel.font=[UIFont systemFontOfSize:14];
    [_courtOrderView addSubview:cpriceLabel];
    
    UIImageView *imgv=[[UIImageView alloc]initWithFrame:CGRectMake(10, cpriceLabel.frame.size.height+cpriceLabel.frame.origin.y+14, _courtOrderView.frame.size.width-20, 2)];
    imgv.image=[UIImage imageNamed:@"f_virature"];
    [_courtOrderView addSubview:imgv];
    
    UILabel *torderRemarkLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, imgv.frame.size.height+imgv.frame.origin.y+8,80, 18)];
    torderRemarkLabel.backgroundColor=[UIColor clearColor];
    torderRemarkLabel.textColor=[UIColor blackColor];
    torderRemarkLabel.text=@"预定说明";
    torderRemarkLabel.font=[UIFont systemFontOfSize:16];
    [_courtOrderView addSubview:torderRemarkLabel];
    
    UILabel *corderRemarkLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, torderRemarkLabel.frame.size.height+torderRemarkLabel.frame.origin.y+4,courtNameLabel.frame.size.width, 200)];
    corderRemarkLabel.backgroundColor=[UIColor clearColor];
    corderRemarkLabel.textColor=[UIColor blackColor];
    corderRemarkLabel.text=[[_courtDetailArray objectAtIndex:tag] objectForKey:@"cancel_remark"];
    corderRemarkLabel.numberOfLines=10;
    corderRemarkLabel.lineBreakMode=NSLineBreakByWordWrapping;
    corderRemarkLabel.font=[UIFont systemFontOfSize:14];
    [_courtOrderView addSubview:corderRemarkLabel];
    
    UILabel *priceTypeLabel=[[UILabel alloc]initWithFrame:CGRectMake(1, _courtOrderView.frame.size.height-42, 180, 40)];
    priceTypeLabel.backgroundColor=[Utils colorWithHexString:@"#fc6929"];
    priceTypeLabel.textColor=[UIColor whiteColor];
    self.selectOrderCourtPriceType=[[_courtDetailArray objectAtIndex:tag] objectForKey:@"pay_type"];
    NSString *pay_typeStr;
    if ([[[_courtDetailArray objectAtIndex:tag] objectForKey:@"pay_type"] intValue]==0) {
        pay_typeStr=@"现付";
    }
    else if ([[[_courtDetailArray objectAtIndex:tag] objectForKey:@"pay_type"] intValue]==1)
    {
        pay_typeStr=@"全额预付";
    }
    else
    {
        pay_typeStr=@"押金";
    }
    self.selectOrderCourtPrice=[[_courtDetailArray objectAtIndex:tag] objectForKey:@"price"];
    priceTypeLabel.text=[[@"￥" stringByAppendingString:[NSString stringWithFormat:@"%.0f",[[[_courtDetailArray objectAtIndex:tag] objectForKey:@"price"] floatValue]/100]] stringByAppendingString:pay_typeStr];
    ///
    self.selectOrderCourtRelationId=[[_courtDetailArray objectAtIndex:tag] objectForKey:@"court_id"];
    self.selectOrderCourtAgentid=[[_courtDetailArray objectAtIndex:tag] objectForKey:@"agent_id"];

    [self customCorderMethod:priceTypeLabel andRoundingCorner:UIRectCornerBottomLeft];
    ///
    [_courtOrderView addSubview:priceTypeLabel];
    
    UIButton *nextBtn=[[UIButton alloc]initWithFrame:CGRectMake(181, priceTypeLabel.frame.origin.y, 119, 40)];
    [nextBtn setBackgroundColor:[Utils colorWithHexString:@"#f84519"]];
    [nextBtn setTitle:@"下一步" forState:UIControlStateNormal];
    [nextBtn addTarget:self action:@selector(writeOrderMthod) forControlEvents:UIControlEventTouchUpInside];
    [self customCorderMethod:nextBtn andRoundingCorner:UIRectCornerBottomRight];
    [_courtOrderView addSubview:nextBtn];
    _courtOrderView.layer.cornerRadius=8.0;
    [[UIApplication sharedApplication].keyWindow addSubview:_courtOrderView];
}
-(void)customCorderMethod:(UIView *)targetView andRoundingCorner:(int)roundingCornerType
{
    UIBezierPath *maskPath=[UIBezierPath bezierPathWithRoundedRect:targetView.bounds byRoundingCorners:roundingCornerType cornerRadii:CGSizeMake(8, 8)];
    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = targetView.bounds;
    maskLayer.path = maskPath.CGPath;
    targetView.layer.mask = maskLayer;
}
-(void)writeOrderMthod
{
    [self closeOrderView];
    WriteOrderViewController *tmpwriteOrderViewController=[[WriteOrderViewController alloc]init];
    self.writeOrderViewController=tmpwriteOrderViewController;
    [tmpwriteOrderViewController release];
    self.writeOrderViewController.courtName=self.selectOrderCourtName;
    self.writeOrderViewController.courtTime=self.selectOrderCourtTime;
    self.writeOrderViewController.courtPrice=self.selectOrderCourtPrice;
    self.writeOrderViewController.courtType=self.selectOrderCourtPriceType;
    self.writeOrderViewController.orderType=@"0";
    self.writeOrderViewController.relation_id=self.selectOrderCourtRelationId;
    self.writeOrderViewController.agent_id=self.selectOrderCourtAgentid;
    [self.navigationController pushViewController:self.writeOrderViewController animated:YES];
}
-(void)closeOrderView
{
    self.view.alpha=1.0;
    [self.view setUserInteractionEnabled:YES];
    [_courtOrderView removeFromSuperview];
}
-(void)timePickCancelMethod
{
    if (_timePickView!=nil) {
        [_timePickView removeFromSuperview];
    }
}
-(void)timePickSureMethod
{
    if (_timePickView!=nil) {
        [_timePickView removeFromSuperview];
    }
    [_courtDetailTable reloadData];
}
//获取球场信息
-(void)golfgetCourtInfo
{
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"court/info" forKey:@"cmd"];
    [dic setObject:_courtId forKey:@"court_id"];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(golfgetCourtInfoMethod:) name:@"com.golf.golfgetCourtInfoMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"court/info" andNotificationName:@"com.golf.golfgetCourtInfoMethod" andViewControler:nil];
}
-(void)golfgetCourtInfoMethod:(NSNotification *)notification
{
    NSDictionary *getCourtInfoDic=[notification object];
    NSLog(@"球场详情界面的球场信息回调=======%@",getCourtInfoDic);
    NSNumber *statusNum=[getCourtInfoDic objectForKey:@"status"];
    if ([statusNum intValue]==0) {
        //成功
        self.courtImgArray=[[getCourtInfoDic objectForKey:@"data"] objectForKey:@"court_imgs"];
        if ([self.courtImgArray count]==0) {
            self.courtImgStr=@"";
        }
        else
        {
            self.courtImgStr=[[[getCourtInfoDic objectForKey:@"data"] objectForKey:@"court_imgs"] objectAtIndex:0];
        }
        self.courtDateStr=[[getCourtInfoDic objectForKey:@"data"] objectForKey:@"court_data"];
        NSDictionary *dataArray=[getCourtInfoDic objectForKey:@"data"];
        NSString *modelStr=[dataArray  objectForKey:@"model"];
        if (modelStr==nil) {
            modelStr=@"";
        }
        NSString *create_yearStr=[dataArray  objectForKey:@"create_year"];
        if (create_yearStr==nil) {
            create_yearStr=@"";
        }
        NSString *areaStr=[dataArray  objectForKey:@"area"];
        if (areaStr==nil) {
            areaStr=@"";
        }
        NSString *green_grassStr=[dataArray  objectForKey:@"green_grass"];
        if (green_grassStr==nil) {
            green_grassStr=@"";
        }
        NSString *court_dataStr=[dataArray objectForKey:@"court_data"];
        if (court_dataStr==nil) {
            court_dataStr=@"";
        }
        NSString *designerStr=[dataArray objectForKey:@"designer"];
        if (designerStr==nil) {
            designerStr=@"";
        }
        NSString *fairway_lengthStr=[dataArray  objectForKey:@"fairway_length"];
        if (fairway_lengthStr==nil) {
            fairway_lengthStr=@"";
        }
        NSString *fairway_grassStr=[dataArray  objectForKey:@"fairway_grass"];
        if (fairway_grassStr==nil) {
            fairway_grassStr=@"";
        }
        self.courtInfoArray=[NSMutableArray array];
        [_courtInfoArray addObject:modelStr];
        [_courtInfoArray addObject:create_yearStr];
        [_courtInfoArray addObject:areaStr];
        [_courtInfoArray addObject:green_grassStr];
        [_courtInfoArray addObject:court_dataStr];
        [_courtInfoArray addObject:designerStr];
        [_courtInfoArray addObject:fairway_lengthStr];
        [_courtInfoArray addObject:fairway_grassStr];
        self.fairwayImgArray=[dataArray  objectForKey:@"fairway_imgs"];
        self.phnStr=[dataArray  objectForKey:@"phone"];
        self.facilityInfoStr=[dataArray  objectForKey:@"facilities"];
        self.descInfoStr=[dataArray  objectForKey:@"remark"];
        [_courtDetailTable reloadData];
    }
    else
    {
        self.notificationText=networkAbnormalInfo;
        [self displayNotification];
    }
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.golfgetCourtInfoMethod" object:nil];
}
//获取球场评论
-(void)golfgetComment
{
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"court/comment" forKey:@"cmd"];
    [dic setObject:_courtId forKey:@"court_id"];
    [dic setObject:@"0" forKey:@"_pg_"];

    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(golfgetCommentMethod:) name:@"com.golf.golfgetCommentMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"court/comment" andNotificationName:@"com.golf.golfgetCommentMethod" andViewControler:nil];
}
-(void)golfgetCommentMethod:(NSNotification *)notification
{
    NSDictionary *commentDic=[notification object];
    NSLog(@"球场详情界面的评论信息回调=====%@",commentDic);
    if (commentDic==nil) {
        [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.golfgetCommentMethod" object:nil];
        return;
        
    }
    NSNumber *statusNum=[commentDic objectForKey:@"status"];
    if ([statusNum intValue]==0) {
        if ([self.evaluateArray count]!=0) {
            [self.evaluateArray removeAllObjects];
        }
        self.evaluateArray=[NSMutableArray array];
        [_evaluateArray addObject:[commentDic objectForKey:@"design_total"]];
        [_evaluateArray addObject:[commentDic objectForKey:@"lawn_total"]];
        [_evaluateArray addObject:[commentDic objectForKey:@"facilitie_total"]];
        [_evaluateArray addObject:[commentDic objectForKey:@"service_total"]];
        [_courtDetailTable reloadData];
    }
    else
    {
        self.notificationText=networkAbnormalInfo;
        [self displayNotification];
    }
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.golfgetCommentMethod" object:nil];
}
//获取球场报价
-(void)golfgetPrice
{
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"court/price" forKey:@"cmd"];
    [dic setObject:_courtId forKey:@"court_id"];
    [dic setObject:@"0" forKey:@"type"];
    
    NSDateFormatter *format=[[NSDateFormatter alloc]init];
    [format setDateFormat:@"yyyy-MM-dd"];
    NSString *selectdateStr=[format stringFromDate:_selectDate];
    NSLog(@"球场详情界面的_selectDate====%@",_selectDate);
    NSString *date_timeStr;
    if (_timeBtn.titleLabel.text.length==0) {
        date_timeStr=[[selectdateStr stringByAppendingString:@" "] stringByAppendingString:_timeStr];
    }
    else
    {
        date_timeStr=[[selectdateStr stringByAppendingString:@" "] stringByAppendingString:_timeBtn.titleLabel.text];
    }
    NSLog(@"date_timeStr====%@",date_timeStr);
    [dic setObject:date_timeStr forKey:@"date_time"];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(golfgetPriceMethod:) name:@"com.golf.golfgetPriceMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"court/price" andNotificationName:@"com.golf.golfgetPriceMethod" andViewControler:nil];
}
-(void)golfgetPriceMethod:(NSNotification*)notification
{
    NSDictionary *priceDic=[notification object];
    NSLog(@"球场详情界面的球场报价=====%@",priceDic);
    NSNumber *statusNum=[priceDic objectForKey:@"status"];
    if ([statusNum intValue]==0) {
        if ([_courtDetailArray count]!=0) {
            [_courtDetailArray removeAllObjects];
        }
        self.courtDetailArray=[NSMutableArray array];
        
        NSString *dateAndTimeStr=[[_dateStr stringByAppendingString:@" "] stringByAppendingString:_timeStr];
        [_courtDetailArray addObject:dateAndTimeStr];
        [_courtDetailArray addObject:[[[priceDic objectForKey:@"data"] objectAtIndex:0] objectForKey:@"agent_name"]];
        NSArray *detailData=[priceDic objectForKey:@"data"];
        if ([detailData count]==0) {
            [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.golfgetPriceMethod" object:nil];
            return;
        }
        for (int i=0; i<[detailData count]; i++) {
            [_courtDetailArray addObject:[detailData objectAtIndex:i]];
        }
        [_courtDetailTable reloadData];
    }
    else
    {
        self.notificationText=networkAbnormalInfo;
        [self displayNotification];
    }
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.golfgetPriceMethod" object:nil];
}
#pragma mark -UIPickerViewDataSource

/* return cor of pickerview*/
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}
/*return row number*/
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [_timeArray count];
}

/*return component row str*/
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return [_timeArray objectAtIndex:row];
}

/*choose com is component,row's function*/
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    _timeStr=[_timeArray objectAtIndex:row];
}

#pragma mark - DSLCalendarViewDelegate methods

- (void)calendarView:(DSLCalendarView *)calendarView didSelectRange:(DSLCalendarRange *)range {
    if (range != nil) {
        self.selectDate=range.startDay.date;
        NSLog( @"Selected %d/%d - %d/%d", range.startDay.day, range.startDay.month, range.endDay.day, range.endDay.month);
        NSDateFormatter *format=[[NSDateFormatter alloc]init];
        [format setDateFormat:@"MM月dd日 EE"];
        _dateStr=[format stringFromDate:range.startDay.date];
        [_courtDetailTable reloadData];
//        if (_calendarView!=nil) {
            [_allCalendarView removeFromSuperview];
//        }
        [format release];
    }
    else {
        NSLog( @"No selection" );
    }
}

- (DSLCalendarRange*)calendarView:(DSLCalendarView *)calendarView didDragToDay:(NSDateComponents *)day selectingRange:(DSLCalendarRange *)range {
    if (NO) { // Only select a single day
        return [[DSLCalendarRange alloc] initWithStartDay:day endDay:day];
    }
    else if (NO) { // Don't allow selections before today
        NSDateComponents *today = [[NSDate date] dslCalendarView_dayWithCalendar:calendarView.visibleMonth.calendar];
        
        NSDateComponents *startDate = range.startDay;
        NSDateComponents *endDate = range.endDay;
        
        if ([self day:startDate isBeforeDay:today] && [self day:endDate isBeforeDay:today]) {
            return nil;
        }
        else {
            if ([self day:startDate isBeforeDay:today]) {
                startDate = [today copy];
            }
            if ([self day:endDate isBeforeDay:today]) {
                endDate = [today copy];
            }
            
            return [[DSLCalendarRange alloc] initWithStartDay:startDate endDay:endDate];
        }
    }
    return range;
}

- (void)calendarView:(DSLCalendarView *)calendarView willChangeToVisibleMonth:(NSDateComponents *)month duration:(NSTimeInterval)duration {
    NSLog(@"Will show %@ in %.3f seconds", month, duration);
}

- (void)calendarView:(DSLCalendarView *)calendarView didChangeToVisibleMonth:(NSDateComponents *)month {
    NSLog(@"Now showing %@", month);
}

- (BOOL)day:(NSDateComponents*)day1 isBeforeDay:(NSDateComponents*)day2 {
    return ([day1.date compare:day2.date] == NSOrderedAscending);
}
-(void)showImg
{
    AHPreviewController *tmppreview=[[AHPreviewController alloc]init];
    self.preview=tmppreview;
    [tmppreview release];
    self.preview.previewImgArray=self.courtImgArray;
    self.preview.previewTitle=@"图片浏览";
    [self.navigationController pushViewController:self.preview animated:YES];
}
-(void)evaluateMethod
{
    EvaluateViewController *tmpevaluate=[[EvaluateViewController alloc]init];
    self.evaluate=tmpevaluate;
    [tmpevaluate release];
    [self.navigationController pushViewController:self.evaluate animated:YES];
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

@end
