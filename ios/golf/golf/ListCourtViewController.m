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
    UIBarButtonItem *leftItem=[[UIBarButtonItem alloc]initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem=leftItem;
    [leftItem release];
    /*
    UIToolbar *sortTool=[[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 34)];
    UIImageView *sortImgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 34)];
    sortImgv.image=[UIImage imageNamed:@"title_bg2"];
    [sortTool insertSubview:sortImgv atIndex:1];
    [sortImgv release];
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
    UIBarButtonItem *sortDistanceItem=[[UIBarButtonItem alloc]initWithCustomView:sortDistanceBtn];
    [sortItems addObject:sortDistanceItem];
    [sortTool setItems:sortItems];
    [sortDistanceItem release];
    [sortPriceBtn release];
    [sortDistanceBtn release];
    [sortPriceItem release];
    [spaceItem release];
    [sortNameItem release];
    [sortNameBtn release];
    */
    NSArray *items = @[@"默认排序", @"价格最低", @"距离最近"];
    ITTSegement *segment = [[ITTSegement alloc] initWithItems:items];
    segment.frame = CGRectMake(0, 0, 320, 40);
    segment.selectedIndex = 0;
    [segment addTarget:self action:@selector(sgAction:) forControlEvents:UIControlEventValueChanged];
    
    [self.view addSubview:segment];
    self.courtInfoArray=[NSMutableArray array];
    for (int i=0; i<[_courtArray count]; i++) {
        CourtModel *courtModel=[[CourtModel alloc]init];
        courtModel.courtName=[[_courtArray objectAtIndex:i] objectForKey:@"name"];
        courtModel.courtDistance=[[[_courtArray objectAtIndex:i] objectForKey:@"distance"] floatValue];
        courtModel.courtPrice=[[[_courtArray objectAtIndex:i] objectForKey:@"price"] floatValue];
        courtModel.isUp=YES;
        courtModel.courtAddress=[[_courtArray objectAtIndex:i] objectForKey:@"addr"];
        courtModel.courtImgUrl=[[_courtArray objectAtIndex:i] objectForKey:@"ico_img"];
        courtModel.courtId=[[_courtArray objectAtIndex:i] objectForKey:@"court_id"];
        
        [_courtInfoArray addObject:courtModel];
        [courtModel release];
    }
    
    NSMutableArray *sortedArray = (NSMutableArray*)[_courtInfoArray sortedArrayUsingSelector:@selector(compareName:)];
    self.courtInfoArray=sortedArray;//_courtInfoArray
    
    _courtInfoTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 34, SCREEN_WIDTH, SCREEN_HEIGHT-64-34) style:UITableViewStylePlain];
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
    searchVc.hidesBottomBarWhenPushed=NO;
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
    
    _courtInfoArray=(NSMutableArray*)sortedArray;
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
        cell=[[[ListCourtCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idetifier] autorelease];
    }
    CourtModel *tmpModel=[_courtInfoArray objectAtIndex:[indexPath row]];
    NSLog(@"tmpModel.courtImgUrl====%@",tmpModel.courtImgUrl);
    if ([tmpModel.courtImgUrl class]==[NSNull class]||tmpModel.courtImgUrl==nil||[tmpModel.courtImgUrl isEqualToString:@""]) {
        [cell.courtImgv setImage:[UIImage imageNamed:@"defaultImg"]];
    }
    else
    {
        [cell.courtImgv setImageWithURL:[NSURL URLWithString:tmpModel.courtImgUrl] placeholderImage:[UIImage imageNamed:@"defaultImg"]];
    }
    
    cell.courtNameLabel.text=tmpModel.courtName;
    cell.courtDistanceLabel.text=[[@"距当前" stringByAppendingString:[NSString stringWithFormat:@"%.2f",tmpModel.courtDistance/1000.0]] stringByAppendingString:@"公里"];
    float price=tmpModel.courtPrice/100;
    cell.courtPriceLabel.text=[NSString stringWithFormat:@"%.0f元",price];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    CourtModel *tmpModel=[self.courtInfoArray objectAtIndex:[indexPath row]];//_courtInfoArray
    CourtDetailViewController *tmpcourtDetailVc=[[CourtDetailViewController alloc]init];
    self.courtDetailVc=tmpcourtDetailVc;
    [tmpcourtDetailVc release];
    self.courtDetailVc.dateStr=_dateStr;
    self.courtDetailVc.timeStr=_timeStr;
    self.courtDetailVc.courtAddress=tmpModel.courtAddress;
    self.courtDetailVc.courtName=tmpModel.courtName;
    self.courtDetailVc.courtId=tmpModel.courtId;
    [self.navigationController pushViewController:self.courtDetailVc animated:YES];
}
@end
