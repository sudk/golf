//
//  CourtDetailInfoViewController.m
//  golf
//
//  Created by mahh on 14-4-12.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "CourtDetailInfoViewController.h"

@interface CourtDetailInfoViewController ()

@end

@implementation CourtDetailInfoViewController

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
    self.title=@"球场信息";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _courtInfoScroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64)];
    _courtInfoScroll.contentSize=CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT*1.5);
    [self.view addSubview:_courtInfoScroll];
    _courtInfoTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 270) style:UITableViewStylePlain];
    _courtInfoTable.dataSource=self;
    _courtInfoTable.delegate=self;
    _courtInfoTable.backgroundColor=[UIColor clearColor];
    _courtInfoTable.rowHeight=30;
    [_courtInfoScroll addSubview:_courtInfoTable];
    self.courtInfoTitleArray=@[@"球场模式",@"建立时间",@"球场面积",@"果岭草种",@"球场数据",@"设计师",@"球道长度",@"球道草种"];
    
    UIView *phnView=[[UIView alloc]initWithFrame:CGRectMake(0, _courtInfoTable.frame.origin.y+_courtInfoTable.frame.size.height+10, SCREEN_WIDTH, 40)];
    phnView.backgroundColor=[UIColor whiteColor];
    UILabel *phnLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 40)];
    phnLabel.backgroundColor=[UIColor clearColor];
    phnLabel.text=@"球场电话";
    
    UIButton *phnBtn=[[UIButton alloc]initWithFrame:CGRectMake(phnLabel.frame.origin.x+phnLabel.frame.size.width, 0, 180, 40)];
    [phnBtn setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
    [phnBtn setTitle:@"0531888888" forState:UIControlStateNormal];
    [phnBtn addTarget:self action:@selector(callMethod:) forControlEvents:UIControlEventTouchUpInside];
    [phnView addSubview:phnLabel];
    [phnView addSubview:phnBtn];
    [_courtInfoScroll addSubview:phnView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -TableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_courtInfoTitleArray count];
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell=[[UITableViewCell alloc]init];
    UILabel *titleLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 5, 80, 20)];
    titleLabel.text=[_courtInfoTitleArray objectAtIndex:[indexPath row]];
    [cell.contentView addSubview:titleLabel];
    tableView.separatorColor=[UIColor clearColor];
    return cell;
}
#pragma mark -method
-(IBAction)callMethod:(id)sender
{
    UIButton *btn=(UIButton*)sender;
    NSString *phn=[[btn titleLabel] text];
    UIWebView*callWebview =[[UIWebView alloc] init];
    
    NSString *telStr=[@"tel:" stringByAppendingString:phn];
    NSURL *telURL =[NSURL URLWithString:telStr];// 貌似tel:// 或者 tel: 都行
    
    [callWebview loadRequest:[NSURLRequest requestWithURL:telURL]];
    
    //记得添加到view上
    
    [self.view addSubview:callWebview];
}
@end
