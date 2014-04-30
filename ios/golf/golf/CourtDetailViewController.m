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
    self.evaluateArray=@[@"80",@"80",@"80",@"80"];
 self.timeArray=@[@"04:00",@"04:30",@"05:00",@"05:30",@"06:00",@"06:30",@"07:00",@"07:30",@"08:00",@"08:30",@"09:00",@"09:30",@"10:00",@"10:30",@"11:00",@"11:30",@"12:00",@"12:30",@"13:00",@"13:30",@"14:00",@"14:30",@"15:00",@"15:30",@"16:00",@"16:30",@"17:00",@"17:30",@"18:00",@"18:30",@"19:00",@"19:30",@"20:00"];
    NSString *dateAndTimeStr=[[_dateStr stringByAppendingString:@"&"] stringByAppendingString:_timeStr];
    self.courtDetailArray=@[dateAndTimeStr,@"golf007",@"golf008",@"golf009",@"golf100"];
    UITableView *tmpcourtDetailTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64) style:UITableViewStyleGrouped];
    self.courtDetailTable=tmpcourtDetailTable;
    [tmpcourtDetailTable release];
    _courtDetailTable.delegate=self;
    _courtDetailTable.dataSource=self;
    _courtDetailTable.contentInset=UIEdgeInsetsMake(-40, 0, 0, 0);
    _courtDetailTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:self.courtDetailTable];
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
        if ([indexPath section]==1&&[indexPath row]!=0) {
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
                    [self customLabel:CGRectMake(0, 2, SCREEN_WIDTH, 40) withTag:courtNameLabelTag withCell:cell withText:_courtName];
                  break;
                }
                case 1:
                {
                    UIView *courtView=[[UIView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 165)];
                    UIButton *courtLargeBtn=[[UIButton alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 165)];
                    [courtLargeBtn setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] forState:UIControlStateNormal];
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
                    [self customLabel:CGRectMake(0, 3, 200, 40) withTag:addressLabelTag withCell:cell withText:_courtAddress];
                    UIButton *daoHangBtn=[self customButton:CGRectMake(205, 2, 45, 36) withCell:cell withTitle:@"导航" withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                    [daoHangBtn addTarget:self action:@selector(operateMethod) forControlEvents:UIControlEventTouchUpInside];
                    
                    [self customButton:CGRectMake(255, 3, 45, 40) withCell:cell withTitle:@"" withColor:[UIColor clearColor] withNormalImg:@"setting_arrow_normal" withHighlightedImg:@"setting_arrow_pressed"];
                    break;
                }
                case 3:
                {
                    [self customLabel:CGRectMake(0, 3, 200, 40) withTag:raidersLabelTag withCell:cell withText:@"林克思球场 18洞72杆"];
                    UIButton *showBtn=[self customButton:CGRectMake(205, 2, 45, 36) withCell:cell withTitle:@"查看" withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
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
                UIButton *dateBtn=[self customButton:CGRectMake(10, 2, 120, 36) withCell:cell withTitle:_dateStr withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                [dateBtn addTarget:self action:@selector(showDateMethod) forControlEvents:UIControlEventTouchUpInside];
                UIButton *timeBtn=[self customButton:CGRectMake(180, 2, 70, 36) withCell:cell withTitle:_timeStr withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                [timeBtn addTarget:self action:@selector(showTimeMethod) forControlEvents:UIControlEventTouchUpInside];
            }
            else
            {
                [self customLabel:CGRectMake(15, 2, 150, 30) withTag:cDetailNameLebelTag withCell:cell withText:[_courtDetailArray objectAtIndex:[indexPath row]]];
                [self customLabel:CGRectMake(15, 33, 150, 30) withTag:cDetailFacilityLebelTag withCell:cell withText:@"18洞果岭"];
                [self customLabel:CGRectMake(170, 15, 60, 35) withTag:cDetailPriceLebelTag withCell:cell withText:@"1150"];
                UIButton *cDetailOrderBtn=[self customButton:CGRectMake(235, 2, 60, 30) withCell:cell withTitle:@"预定" withColor:[UIColor blackColor] withNormalImg:@"" withHighlightedImg:@""];
                [cDetailOrderBtn addTarget:self action:@selector(courtOrderMehtod) forControlEvents:UIControlEventTouchUpInside];
                [self customLabel:CGRectMake(235, 33, 80, 30) withTag:cDetailPriceTypeLebelTag withCell:cell withText:@"全额预付"];
                
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
    [customBtn setTitle:title forState:UIControlStateNormal];
    [customBtn setTitleColor:color forState:UIControlStateNormal];
    [customBtn setBackgroundImage:[UIImage imageNamed:normalImgName] forState:UIControlStateNormal];
    [customBtn setBackgroundImage:[UIImage imageNamed:highlightedImgName] forState:UIControlStateHighlighted];
    [cell.contentView addSubview:customBtn];
    return customBtn;
}
-(void)customLabel:(CGRect)customFrame withTag:(int)customTag withCell:(UITableViewCell *)cell withText:(NSString*)textStr
{
    UILabel *customLabel=[[UILabel alloc]initWithFrame:customFrame];
    customLabel.backgroundColor=[UIColor clearColor];
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
    [tmpdetailInfo release];
    self.detailInfo.courtId=_courtId;
    [self.navigationController pushViewController:self.detailInfo animated:YES];
}
-(void)showDateMethod
{
    DSLCalendarView *tmpcalendarView=[[DSLCalendarView alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-20-330, SCREEN_WIDTH, 330)];
    self.calendarView=tmpcalendarView;
    [tmpcalendarView release];
    _calendarView.delegate = self;
    _calendarView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:self.calendarView];
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
-(void)courtOrderMehtod
{
    
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
        NSLog( @"Selected %d/%d - %d/%d", range.startDay.day, range.startDay.month, range.endDay.day, range.endDay.month);
        NSDateFormatter *format=[[NSDateFormatter alloc]init];
        [format setDateFormat:@"MM月dd日 EE"];
        _dateStr=[format stringFromDate:range.startDay.date];
        [_courtDetailTable reloadData];
        if (_calendarView!=nil) {
            [_calendarView removeFromSuperview];
        }
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
    self.preview.previewImgArray=@[@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"];
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
@end
