//
//  ListCourtViewController.m
//  golf
//
//  Created by mahh on 14-4-8.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "ListCourtViewController.h"
#import "SDWebImageManager.h"
#import "pinyin.h"
#import "ChineseString.h"
#import "CourtModel.h"
#import "ListCourtCell.h"
#import "UIImageView+WebCache.h"
#import "ITTSegement.h"
#import "CourtDetailViewController.h"
#import "SearchCourtHomeViewController.h"

@interface ListCourtViewController ()

@end

@implementation ListCourtViewController

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
    self.title=_courtTitle;
    UIButton *backBtn=[self leftButton:@"" andHighlightedName:@""];
    [backBtn addTarget:self action:@selector(backMethod) forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]initWithCustomView:backBtn];
//    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    UIToolbar *sortTool=[[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 34)];
    UIImageView *sortImgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 34)];
    sortImgv.image=[UIImage imageNamed:@"title_bg2"];
    [sortTool insertSubview:sortImgv atIndex:1];
    NSMutableArray *sortItems=[NSMutableArray array];
    UIBarButtonItem *spaceItem=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [sortItems addObject:spaceItem];
    UIButton *sortNameBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, 2, 80, 30)];
    [sortNameBtn setTitle:@"默认排序" forState:UIControlStateNormal];
    [sortNameBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    [sortNameBtn addTarget:self action:@selector(sortNameMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sortNameItem=[[UIBarButtonItem alloc]initWithCustomView:sortNameBtn];
    [sortItems addObject:sortNameItem];
    [sortItems addObject:spaceItem];
    UIButton *sortPriceBtn=[[UIButton alloc]initWithFrame:CGRectMake(sortNameBtn.frame.origin.x+sortNameBtn.frame.size.width+30, 2, 80, 30)];
    [sortPriceBtn setTitle:@"价格最低" forState:UIControlStateNormal];
    [sortPriceBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    [sortPriceBtn addTarget:self action:@selector(sortPriceMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sortPriceItem=[[UIBarButtonItem alloc]initWithCustomView:sortPriceBtn];
    [sortItems addObject:sortPriceItem];
    [sortItems addObject:spaceItem];
    UIButton *sortDistanceBtn=[[UIButton alloc]initWithFrame:CGRectMake(sortPriceBtn.frame.origin.x+sortPriceBtn.frame.size.width+30, 2, 80, 30)];
    [sortDistanceBtn setTitle:@"距离最近" forState:UIControlStateNormal];
    [sortDistanceBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    [sortDistanceBtn addTarget:self action:@selector(sortDistanceMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sortDistanceItem=[[UIBarButtonItem alloc]initWithCustomView:sortDistanceBtn];
    [sortItems addObject:sortDistanceItem];
    [sortTool setItems:sortItems];
//    [self.view addSubview:sortTool];
    NSArray *items = @[@"默认排序", @"价格最低", @"距离最近"];
    ITTSegement *segment = [[ITTSegement alloc] initWithItems:items];
    segment.frame = CGRectMake(0, 0, 320, 40);
    segment.selectedIndex = 0;
    [segment addTarget:self action:@selector(sgAction:) forControlEvents:UIControlEventValueChanged];
    
    [self.view addSubview:segment];
    CourtModel *courtModel=[[CourtModel alloc]init];
    courtModel.courtName=@"广东珠海高尔夫";
    courtModel.courtDistance=54.70;
    courtModel.courtPrice=200;
    courtModel.isUp=YES;
    courtModel.courtImgUrl=@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg";
    CourtModel *courtModel1=[[CourtModel alloc]init];
    courtModel1.courtName=@"深圳名商高尔夫";
    courtModel1.courtDistance=131.53;
    courtModel1.courtPrice=570;
    courtModel1.isUp=YES;
    courtModel1.courtImgUrl=@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg";
    CourtModel *courtModel2=[[CourtModel alloc]init];
    courtModel2.courtName=@"北京惠州高尔夫";
    courtModel2.courtDistance=61.05;
    courtModel2.courtPrice=590;
    courtModel2.isUp=YES;
    courtModel2.courtImgUrl=@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg";
    CourtModel *courtModel3=[[CourtModel alloc]init];
    courtModel3.courtName=@"西安惠州高尔夫";
    courtModel3.courtDistance=61.04;
    courtModel3.courtPrice=590;
    courtModel3.isUp=YES;
    courtModel3.courtImgUrl=@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg";
    CourtModel *courtModel4=[[CourtModel alloc]init];
    courtModel4.courtName=@"广东惠州高尔夫";
    courtModel4.courtDistance=61.03;
    courtModel4.courtPrice=590;
    courtModel4.isUp=YES;
    courtModel4.courtImgUrl=@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg";

    self.courtInfoArray=@[courtModel,courtModel1,courtModel2,courtModel3,courtModel4];
    NSArray *sortedArray = [_courtInfoArray sortedArrayUsingSelector:@selector(compareName:)];
    _courtInfoArray=sortedArray;
    
    _courtInfoTable=[[UITableView alloc]initWithFrame:CGRectMake(0, sortTool.frame.origin.y+sortTool.frame.size.height, SCREEN_WIDTH, SCREEN_HEIGHT-64-sortTool.frame.size.height) style:UITableViewStylePlain];
    _courtInfoTable.delegate=self;
    _courtInfoTable.dataSource=self;
    _courtInfoTable.rowHeight=65;
    _courtInfoTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:_courtInfoTable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -method
-(void)backMethod
{
    NSArray *viewcontrols=[self.navigationController viewControllers];
    SearchCourtHomeViewController *searchVc=(SearchCourtHomeViewController*)[viewcontrols objectAtIndex:[viewcontrols count]-2];
    searchVc.isFromCourtList=YES;
    [self.navigationController popToViewController:searchVc animated:YES];
}
-(void)sgAction:(ITTSegement *)sg
{
    NSLog(@"%d %d", sg.selectedIndex, sg.currentState);
    for (CourtModel *model in _courtInfoArray) {
        if (sg.currentState == UPStates) {
            model.isUp = YES;
        } else {
            model.isUp = NO;
        }
    }
    NSArray *sortedArray;
    if (sg.selectedIndex == 0) {
         sortedArray= [_courtInfoArray sortedArrayUsingSelector:@selector(compareName:)];
    } else if (sg.selectedIndex == 1) {
        sortedArray = [_courtInfoArray sortedArrayUsingSelector:@selector(comparePrice:)];
    } else {
        sortedArray = [_courtInfoArray sortedArrayUsingSelector:@selector(compareDistance:)];
    }
    
    _courtInfoArray=sortedArray;
    [_courtInfoTable reloadData];
}

#pragma mark -TableViewDelegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_courtInfoArray count];
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *idetifier=@"listCourtCell";
    ListCourtCell *cell=[tableView dequeueReusableCellWithIdentifier:idetifier];
    if (cell==nil) {
        cell=[[ListCourtCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idetifier];
    }
    CourtModel *tmpModel=[_courtInfoArray objectAtIndex:[indexPath row]];
    [cell.courtImgv setImageWithURL:[NSURL URLWithString:tmpModel.courtImgUrl] placeholderImage:[UIImage imageNamed:@"defaultImg"]];
    cell.courtNameLabel.text=tmpModel.courtName;
    cell.courtDistanceLabel.text=[[@"距市中心" stringByAppendingString:[NSString stringWithFormat:@"%.2f",tmpModel.courtDistance]] stringByAppendingString:@"公里"];
    cell.courtPriceLabel.text=[NSString stringWithFormat:@"%.0f",tmpModel.courtPrice];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    CourtModel *tmpModel=[_courtInfoArray objectAtIndex:[indexPath row]];
    CourtDetailViewController *courtDetailVc=[[CourtDetailViewController alloc]init];
    courtDetailVc.dateStr=_dateStr;
    courtDetailVc.timeStr=_timeStr;
    courtDetailVc.courtName=tmpModel.courtName;
    [self.navigationController pushViewController:courtDetailVc animated:YES];
}
@end
