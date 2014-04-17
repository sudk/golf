//
//  EvaluateViewController.m
//  golf
//
//  Created by mahh on 14-4-15.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "EvaluateViewController.h"
#import "EvaluateCell.h"

@interface EvaluateViewController ()

@end

@implementation EvaluateViewController

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
    self.title=@"球场点评";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    UIButton *submitBtn=[[UIButton alloc]initWithFrame:CGRectMake(230, 2, 90, 40)];
    [submitBtn addTarget:self action:@selector(submitMethod) forControlEvents:UIControlEventTouchUpInside];
    [submitBtn setTitle:@"提交评论" forState:UIControlStateNormal];
    [submitBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]initWithCustomView:submitBtn];
    NSArray *noArray=@[@"80",@"80",@"80",@"80"];//设计，草坪，设施，服务
    self.evaluateArray=@[noArray,noArray,noArray];
    _evaluateTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64) style:UITableViewStylePlain];
    _evaluateTable.delegate=self;
    _evaluateTable.dataSource=self;
    _evaluateTable.rowHeight=90;
    _evaluateTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:_evaluateTable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_evaluateArray count];
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier=@"evaluateCell";
    EvaluateCell *cell=[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        cell=[[EvaluateCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    NSString *title=[[[[[[[@"设计" stringByAppendingString:[[_evaluateArray objectAtIndex:[indexPath row]] objectAtIndex:0]] stringByAppendingString:@" 草坪"] stringByAppendingString:[[_evaluateArray objectAtIndex:[indexPath row]] objectAtIndex:1]]stringByAppendingString:@" 设施"] stringByAppendingString:[[_evaluateArray objectAtIndex:[indexPath row]] objectAtIndex:2]]stringByAppendingString:@" 服务"] stringByAppendingString:[[_evaluateArray objectAtIndex:[indexPath row]] objectAtIndex:2]];
    cell.titleLabel.text=title;
    cell.timeLabel.text=@"2014-04-15 22:10";
    cell.descLabel.text=@"环境好";
    return cell;
}
-(void)submitMethod
{
    
}
@end
