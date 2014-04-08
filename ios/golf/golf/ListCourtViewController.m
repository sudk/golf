//
//  ListCourtViewController.m
//  golf
//
//  Created by mahh on 14-4-8.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "ListCourtViewController.h"

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
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    UIToolbar *sortTool=[[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 44)];
    UIImageView *sortImgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 44)];
    sortImgv.image=[UIImage imageNamed:@"title_bg2"];
    [sortTool insertSubview:sortImgv atIndex:1];
    NSMutableArray *sortItems=[NSMutableArray array];
    UIBarButtonItem *spaceItem=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [sortItems addObject:spaceItem];
    UIButton *sortNameBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, 2, 80, 40)];
    [sortNameBtn setTitle:@"默认排序" forState:UIControlStateNormal];
    [sortNameBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [sortNameBtn addTarget:self action:@selector(sortNameMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sortNameItem=[[UIBarButtonItem alloc]initWithCustomView:sortNameBtn];
    [sortItems addObject:sortNameItem];
    [sortItems addObject:spaceItem];
    UIButton *sortPriceBtn=[[UIButton alloc]initWithFrame:CGRectMake(sortNameBtn.frame.origin.x+sortNameBtn.frame.size.width+30, 2, 80, 40)];
    [sortPriceBtn setTitle:@"价格最低" forState:UIControlStateNormal];
    [sortPriceBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [sortPriceBtn addTarget:self action:@selector(sortPriceMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sortPriceItem=[[UIBarButtonItem alloc]initWithCustomView:sortPriceBtn];
    [sortItems addObject:sortPriceItem];
    [sortItems addObject:spaceItem];
    UIButton *sortDistanceBtn=[[UIButton alloc]initWithFrame:CGRectMake(sortPriceBtn.frame.origin.x+sortPriceBtn.frame.size.width+30, 2, 80, 40)];
    [sortDistanceBtn setTitle:@"距离最近" forState:UIControlStateNormal];
    [sortDistanceBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [sortDistanceBtn addTarget:self action:@selector(sortDistanceMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *sortDistanceItem=[[UIBarButtonItem alloc]initWithCustomView:sortDistanceBtn];
    [sortItems addObject:sortDistanceItem];
    [sortTool setItems:sortItems];
    [self.view addSubview:sortTool];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -method
-(void)sortNameMethod
{
    
}
-(void)sortPriceMethod
{
    
}
-(void)sortDistanceMethod
{
    
}
@end
