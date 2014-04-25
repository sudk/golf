//
//  SearchCourtHomeViewController.m
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "SearchCourtHomeViewController.h"
#import "DSLCalendarView.h"
#import "ListInfoViewController.h"
#import "KeyWordViewController.h"
#import "ListCourtViewController.h"
#import "FSSysConfig.h"
#import "CityViewController.h"

@interface SearchCourtHomeViewController ()

@end

@implementation SearchCourtHomeViewController

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
    if (_isCurrentView==YES) {
        _isCurrentView=NO;
    }
    else
    {
        if (_isFromUpdate==YES) {
            _isFromUpdate=NO;
            NSLog(@"_contentArray====%@",_contentArray);
            [_contentArray replaceObjectAtIndex:[[[_changeDic allKeys]objectAtIndex:0] intValue] withObject:[[_changeDic allValues]objectAtIndex:0]];
            [_conditionTable reloadData];
        }
    }
}
- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    _httpUtils=[[HttpUtils alloc]init];
    //默认登录
    _sqlUtils=[[SQLUtilsObject alloc]init];
    NSArray *loginInfo=[_sqlUtils query_loginInfo_tab];
    NSLog(@"loginInfo====%@",loginInfo);
    if ([loginInfo count]>0) {
        NSString *account=[[loginInfo objectAtIndex:0]objectForKey:@"phone"];
        NSString *pswd=[[loginInfo objectAtIndex:0]objectForKey:@"pswd"];
        
        NSMutableDictionary *dic=[NSMutableDictionary dictionary];
        [dic setObject:@"user/login" forKey:@"cmd"];
        [dic setObject:account forKey:@"phone"];
        [dic setObject:pswd forKey:@"passwd"]
        ;
        [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(defaultLoginMethod:) name:@"com.golf.ahLoginMethod" object:nil];
        [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"user/login" andNotificationName:@"com.golf.ahLoginMethod" andViewControler:nil];
    }
    //
    _isCurrentView=YES;
    self.title=@"球场搜索";
    self.iconArray=@[[UIImage imageNamed:@"direction"],[UIImage imageNamed:@"date"],[UIImage imageNamed:@"time"],[UIImage imageNamed:@"caculate"],[UIImage imageNamed:@"note"]];
    self.timeArray=@[@"04:00",@"04:30",@"05:00",@"05:30",@"06:00",@"06:30",@"07:00",@"07:30",@"08:00",@"08:30",@"09:00",@"09:30",@"10:00",@"10:30",@"11:00",@"11:30",@"12:00",@"12:30",@"13:00",@"13:30",@"14:00",@"14:30",@"15:00",@"15:30",@"16:00",@"16:30",@"17:00",@"17:30",@"18:00",@"18:30",@"19:00",@"19:30",@"20:00"];
    self.titleArray=@[@"城市",@"打球日期",@"打球时间",@"价格",@"关键字"];
    self.contentArray=[NSMutableArray array];
    self.changeDic=[NSDictionary dictionary];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"EE";
    NSTimeInterval  interval = 24*60*60;
    NSString *weekStr = [formatter stringFromDate:[[NSDate date]initWithTimeInterval:interval sinceDate:[NSDate date]]];
    if (_selectDateStr==nil) {
        _selectDateStr=[@"明天" stringByAppendingString:weekStr];;
    }
    NSArray *tmpContent=@[@"青岛",_selectDateStr,@"9:00",@"选择价格",@"球场名称"];
    for (int i=0; i<[tmpContent count]; i++) {
        [_contentArray addObject:[tmpContent objectAtIndex:i]];
    }
    self.conditionTable=[[UITableView alloc]initWithFrame:CGRectMake(10, 10, SCREEN_WIDTH-20, 200) style:UITableViewStylePlain];
    _conditionTable.dataSource=self;
    _conditionTable.delegate=self;
    _conditionTable.rowHeight=40;
    _conditionTable.backgroundColor=[UIColor clearColor];
    _conditionTable.scrollEnabled=NO;
    [self.view addSubview:_conditionTable];
    
    UIButton  *searchBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, _conditionTable.frame.origin.y+_conditionTable.frame.size.height+20, SCREEN_WIDTH-20, 40)];
    [searchBtn setTitle:@"搜索" forState:UIControlStateNormal];
    [searchBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [searchBtn setBackgroundColor:[Utils colorWithHexString:@"#fc701f"]];
    [searchBtn addTarget:self action:@selector(searchMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:searchBtn];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -UITableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 5;
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier=@"conditionCellIdentifier";
    UITableViewCell *cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    UIImageView *imgv=[[UIImageView alloc]initWithFrame:CGRectMake(4, 2, 30, 30)];
    imgv.image=[_iconArray objectAtIndex:[indexPath row]];
    [cell.contentView addSubview:imgv];
    UILabel *cityLabel=[[UILabel alloc]initWithFrame:CGRectMake(42, 2, 80, 36)];
    cityLabel.text=[_titleArray objectAtIndex:[indexPath row]];
    cityLabel.backgroundColor=[UIColor clearColor];
    [cell.contentView addSubview:cityLabel];
    
    UILabel *contentLabel=[[UILabel alloc]initWithFrame:CGRectMake(150, 2, 120, 36)];
    contentLabel.text=[_contentArray objectAtIndex:[indexPath row]];
    contentLabel.backgroundColor=[UIColor clearColor];
    [cell.contentView addSubview:contentLabel];
    
    if ([indexPath row]==0) {
        
        UIButton *cityNameBtn=[self customBtn:[_contentArray objectAtIndex:[indexPath row]]];
        cityNameBtn.tag=[indexPath row];
        [cityNameBtn addTarget:self action:@selector(cityNameMethod:) forControlEvents:UIControlEventTouchUpInside];
        [cell.contentView addSubview:cityNameBtn];
    }
    else if ([indexPath row]==1)
    {
        UIButton *dateBtn=[self customBtn:[_contentArray objectAtIndex:[indexPath row]]];
        dateBtn.tag=[indexPath row];
        [dateBtn addTarget:self action:@selector(selectDateMethod:) forControlEvents:UIControlEventTouchUpInside];
        [cell.contentView addSubview:dateBtn];
    }
    else if ([indexPath row]==2)
    {
        
        UIButton *timeBtn=[self customBtn:[_contentArray objectAtIndex:[indexPath row]]];
        timeBtn.tag=[indexPath row];
        [timeBtn addTarget:self action:@selector(selectTimeMethod:) forControlEvents:UIControlEventTouchUpInside];
        [cell.contentView addSubview:timeBtn];
    }
    else if ([indexPath row]==3)
    {
        UIButton *priceBtn=[self customBtn:[_contentArray objectAtIndex:[indexPath row]]];
        priceBtn.tag=[indexPath row];
        [priceBtn addTarget:self action:@selector(selectPriceMethod:) forControlEvents:UIControlEventTouchUpInside];
        [cell.contentView addSubview:priceBtn];
    }
    else if ([indexPath row]==4)
    {
        UIButton *keywordBtn=[self customBtn:[_contentArray objectAtIndex:[indexPath row]]];
        keywordBtn.tag=[indexPath row];
        [keywordBtn addTarget:self action:@selector(selectCourtMethod:) forControlEvents:UIControlEventTouchUpInside];
        [cell.contentView addSubview:keywordBtn];
    }
    
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    switch ([indexPath row]) {
        case 0:
        {
            [self scityNameMethod:0];
            break;
        }
        case 1:
        {
            [self sselectDateMethod:1];
            break;
        }
        case 2:
        {
            [self sselectTimeMethod:2];
            break;
        }
        case 3:
        {
            [self sselectPriceMethod:3];
            break;
        }
        case 4:
        {
            [self sselectCourtMethod:4];
            break;
        }
        default:
            break;
    }
}
#pragma mark method
-(UIButton *)customBtn:(NSString *)titleStr
{
    UIButton *customBtn=[[UIButton alloc]initWithFrame:CGRectMake(250, 2, 44, 31)];
    [customBtn setImage:[UIImage imageNamed:@"setting_arrow_normal"] forState:UIControlStateNormal];
    [customBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    return customBtn;
}
-(void)cityNameMethod:(id)sender
{
    int tag=(int)[sender tag];
    [self cityNameCustomMethod:tag];
    
}
-(void)scityNameMethod:(int)tag
{
    [self cityNameCustomMethod:tag];
    
}
-(void)cityNameCustomMethod:(int)tag
{
    _changeDic=[NSDictionary dictionaryWithObject:[_contentArray objectAtIndex:tag] forKey:[NSString stringWithFormat:@"%d",tag]];
    CityViewController *city=[[CityViewController alloc]init];
    city.changeDic=_changeDic;
    [self.navigationController pushViewController:city animated:YES];
//    [self selectCustomMethod:@"城市"];
}
-(void)selectDateMethod:(id)sender
{
    int tag=(int)[sender tag];
    [self selectDateCustomMethod:tag];
}
-(void)sselectDateMethod:(int)tag
{
    [self selectDateCustomMethod:tag];
}
-(void)selectDateCustomMethod:(int)tag
{
    self.changeDic=[NSDictionary dictionaryWithObject:[_contentArray objectAtIndex:tag] forKey:[NSString stringWithFormat:@"%d",tag]];
    self.allCalendarView=[[UIView alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-20-374, SCREEN_WIDTH, 374)];
    self.calendarView=[[DSLCalendarView alloc]initWithFrame:CGRectMake(0,44 , SCREEN_WIDTH, 330)];//SCREEN_HEIGHT-20-330
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
    _calendarView.delegate = self;
    _calendarView.backgroundColor=[UIColor whiteColor];
    [_allCalendarView addSubview:_calendarView];
    [self.view addSubview:_allCalendarView];
}
-(void)selectTimeMethod:(id)sender
{
    int tag=(int)[sender tag];
    [self selectTimeCustomMethod:tag];
}
-(void)sselectTimeMethod:(int)tag
{
    [self selectTimeCustomMethod:tag];
}
-(void)selectTimeCustomMethod:(int)tag
{
    self.changeDic=[NSDictionary dictionaryWithObject:[_contentArray objectAtIndex:tag] forKey:[NSString stringWithFormat:@"%d",tag]];
    _timePickView=[[UIView alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-20-49-260, SCREEN_WIDTH, 260)];
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
}
-(void)selectPriceMethod:(id)sender
{
    int tag=(int)[sender tag];
    [self selectPriceCustomMethod:tag];
}
-(void)sselectPriceMethod:(int)tag
{
    [self selectPriceCustomMethod:tag];
}
-(void)selectPriceCustomMethod:(int)tag
{
    _changeDic=[NSDictionary dictionaryWithObject:[_contentArray objectAtIndex:tag] forKey:[NSString stringWithFormat:@"%d",tag]];
    [self selectCustomMethod:@"价格"];
}
-(void)selectCourtMethod:(id)sender
{
    int tag=[sender tag];
    [self selectCourtCustomMethod:tag];
}
-(void)sselectCourtMethod:(int)tag
{
    [self selectCourtCustomMethod:tag];
}
-(void)selectCourtCustomMethod:(int)tag
{
    _changeDic=[NSDictionary dictionaryWithObject:[_contentArray objectAtIndex:tag] forKey:[NSString stringWithFormat:@"%d",tag]];
    KeyWordViewController *keyWord=[[KeyWordViewController alloc]init];
    keyWord.changeDic=_changeDic;
    [self.navigationController pushViewController:keyWord animated:YES];
}
-(void)selectCustomMethod:(NSString *)infoTitle
{
    ListInfoViewController *listInfoVc=[[ListInfoViewController alloc]init];
    listInfoVc.infoTitle=infoTitle;
    listInfoVc.changeDic=_changeDic;
    [self.navigationController pushViewController:listInfoVc animated:YES];
}
-(void)datePickCancelMethod
{
    if (_allCalendarView!=nil) {
        [_allCalendarView removeFromSuperview];
    }
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
    NSDictionary *tmpDic=[NSDictionary dictionaryWithObject:_selectTime forKey:[[self.changeDic allKeys]objectAtIndex:0]];
    [_contentArray replaceObjectAtIndex:[[[tmpDic allKeys]objectAtIndex:0] intValue] withObject:[[tmpDic allValues]objectAtIndex:0]];
    [_conditionTable reloadData];
}
-(void)searchMethod
{
    ListCourtViewController *courtVc=[[ListCourtViewController alloc]init];
    courtVc.courtTitle=[[[_contentArray objectAtIndex:0] stringByAppendingString:[_contentArray objectAtIndex:1]] stringByAppendingString:[_contentArray objectAtIndex:2]];
    courtVc.dateStr=[_contentArray objectAtIndex:1];
    courtVc.timeStr=[_contentArray objectAtIndex:2];
    self.hidesBottomBarWhenPushed=YES;
    [self.navigationController pushViewController:courtVc animated:YES];
}

#pragma mark - DSLCalendarViewDelegate methods

- (void)calendarView:(DSLCalendarView *)calendarView didSelectRange:(DSLCalendarRange *)range {
    if (range != nil) {
        NSLog( @"Selected %d/%d - %d/%d", range.startDay.day, range.startDay.month, range.endDay.day, range.endDay.month);
        NSDateFormatter *format=[[NSDateFormatter alloc]init];
        [format setDateFormat:@"MM月dd日 EE"];
        NSString *str=[format stringFromDate:range.startDay.date];
        NSDictionary *tmpDic=[NSDictionary dictionaryWithObject:str forKey:[[self.changeDic allKeys] objectAtIndex:0]];
        [_contentArray replaceObjectAtIndex:[[[tmpDic allKeys]objectAtIndex:0] intValue] withObject:[[tmpDic allValues]objectAtIndex:0]];
        [_conditionTable reloadData];
//        if (_calendarView!=nil) {
            [_allCalendarView removeFromSuperview];
//        }
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
    _selectTime=[_timeArray objectAtIndex:row];
}
-(void)defaultLoginMethod:(NSNotification *)notification
{
    NSDictionary *loginDic=[notification object];
    NSLog(@"默认自动登录完成数据====%@",loginDic);
    NSNumber *codeNum=[loginDic objectForKey:@"status"];
    if ([codeNum intValue]==0) {
        [[FSSysConfig getInstance]setIsLogin:YES];
        [[FSSysConfig getInstance]setLoginAccount:[loginDic objectForKey:@"user_name"]];
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

@end
