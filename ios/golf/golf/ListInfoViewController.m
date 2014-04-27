//
//  ListInfoViewController.m
//  golf
//
//  Created by mahh on 14-4-7.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "ListInfoViewController.h"
#import "SearchCourtHomeViewController.h"

@interface ListInfoViewController ()

@end

@implementation ListInfoViewController

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
    self.title=_infoTitle;
    _sqlUtils=[[SQLUtilsObject alloc]init];
//    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    UIButton *cityBackBtn=[self leftButton:@"" andHighlightedName:@""];
    [cityBackBtn addTarget:self action:@selector(cityBackMethod) forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]initWithCustomView:cityBackBtn];
    if ([_infoTitle isEqualToString:@"城市"]) {
//        self.infoArray=@[@"青岛",@"上海",@"昆明",@"北京",@"济南",@"深圳"];
        NSArray *provinceArray=[_sqlUtils query_province_tab];
        NSArray *cityArray=[_sqlUtils query_city_tab];
       NSMutableArray *tmpInfoArray=[NSMutableArray array];
//        for (int i=0; i<[provinceArray count]; i++) {
        for (int i=0; i<[cityArray count]; i++)
        {
//            [tmpInfoArray addObject:[[provinceArray objectAtIndex:i] objectForKey:@"province"]];
            [tmpInfoArray addObject:[[cityArray objectAtIndex:i] objectForKey:@"city"]];
        }
        self.infoArray=tmpInfoArray;
    }
    else
    {
        self.infoArray=@[@"200元",@"300元",@"400元",@"500元",@"600元",@"700元"];
    }
    
    _listTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64)];
    _listTable.delegate=self;
    _listTable.dataSource=self;
    _listTable.rowHeight=35;
    _listTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:_listTable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -UITableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_infoArray count];
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier=@"listinfoIdentifier";
    UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    UILabel *infoLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 2, SCREEN_WIDTH-20, 31)];
    infoLabel.backgroundColor=[UIColor clearColor];
    infoLabel.tag=infoLabelTag;
    [cell.contentView addSubview:infoLabel];
    
    UILabel *infoLabelContent=(UILabel*)[cell.contentView viewWithTag:infoLabelTag];
    infoLabelContent.text=[_infoArray objectAtIndex:[indexPath row]];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *tmpDic=[NSDictionary dictionaryWithObject:[_infoArray objectAtIndex:[indexPath row]] forKey:[[_changeDic allKeys]objectAtIndex:0]];
    NSArray *viewControls=[self.navigationController viewControllers];
    SearchCourtHomeViewController *searchVc=(SearchCourtHomeViewController *)[viewControls objectAtIndex:[viewControls count]-2];
    searchVc.isFromUpdate=YES;
    searchVc.changeDic=tmpDic;
    [self.navigationController popToViewController:searchVc animated:YES];
}
-(void)cityBackMethod
{
    NSArray *viewControls=[self.navigationController viewControllers];
    SearchCourtHomeViewController *searchVc=(SearchCourtHomeViewController *)[viewControls objectAtIndex:[viewControls count]-2];
    searchVc.isFromUpdate=NO;
    [self.navigationController popToViewController:searchVc animated:YES];
}
@end
