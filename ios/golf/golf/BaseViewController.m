//
//  BaseViewController.m
//  Aihuan
//
//  Created by mahh on 14-2-17.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"

@interface BaseViewController ()

@end

@implementation BaseViewController

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
    self.view.backgroundColor=[UIColor whiteColor];
    if ([self respondsToSelector:@selector(setEdgesForExtendedLayout:)])
    {
        [self setEdgesForExtendedLayout:UIRectEdgeNone];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)setTitle:(NSString *)title
{
    UILabel *titleLabel=[[UILabel alloc]initWithFrame:CGRectZero];
    titleLabel.text=title;
    titleLabel.font=[UIFont systemFontOfSize:21];
    titleLabel.backgroundColor=[UIColor clearColor];
    titleLabel.textColor=NAVBAR_TITLE_COLOR;
    [titleLabel sizeToFit];
    self.navigationItem.titleView=titleLabel;
}
-(UIButton *)leftButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name
{
    UIButton *leftBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    leftBtn.frame=CGRectMake(0, 0, 44, 44);
    [leftBtn setTitle:@"返回" forState:UIControlStateNormal];
    [leftBtn setBackgroundImage:[UIImage imageNamed:normal_img_name] forState:UIControlStateNormal];
    [leftBtn setBackgroundImage:[UIImage imageNamed:highlighted_img_name] forState:UIControlStateHighlighted];
    return leftBtn;

}
-(UIButton *)rightButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name
{
    UIButton *righttBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    righttBtn.frame=CGRectMake(0, 0, 44, 44);
    [righttBtn setTitle:@"搜索" forState:UIControlStateNormal];
    [righttBtn setBackgroundImage:[UIImage imageNamed:normal_img_name] forState:UIControlStateNormal];
    [righttBtn setBackgroundImage:[UIImage imageNamed:highlighted_img_name] forState:UIControlStateHighlighted];
    return righttBtn;
}
-(UIButton *)filterButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name
{
    UIButton *filterBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    filterBtn.frame=CGRectMake(0, 0, 44, 44);
    [filterBtn setTitle:@"筛选" forState:UIControlStateNormal];
    [filterBtn setBackgroundImage:[UIImage imageNamed:normal_img_name] forState:UIControlStateNormal];
    [filterBtn setBackgroundImage:[UIImage imageNamed:highlighted_img_name] forState:UIControlStateHighlighted];
    return filterBtn;
}
-(UIButton *)showCartButton:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name
{
    UIButton *showCartBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    showCartBtn.frame=CGRectMake(0, 0, 60, 44);
    [showCartBtn setTitle:@"购物车" forState:UIControlStateNormal];
    [showCartBtn setBackgroundImage:[UIImage imageNamed:normal_img_name] forState:UIControlStateNormal];
    [showCartBtn setBackgroundImage:[UIImage imageNamed:highlighted_img_name] forState:UIControlStateHighlighted];
    return showCartBtn;
}
-(UIBarButtonItem *)topBarButtonItem:(NSString *)normal_img_name andHighlightedName:(NSString *)highlighted_img_name
{
    UIButton *topBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    topBtn.frame=CGRectMake(0, 0, 44, 44);
    [topBtn setTitle:@"返回" forState:UIControlStateNormal];
    [topBtn setBackgroundImage:[UIImage imageNamed:normal_img_name] forState:UIControlStateNormal];
    [topBtn setBackgroundImage:[UIImage imageNamed:highlighted_img_name] forState:UIControlStateHighlighted];
    [topBtn addTarget:self action:@selector(baseBackMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *topItem=[[UIBarButtonItem alloc]initWithCustomView:topBtn];
    return topItem;
    
}
-(void)baseBackMethod
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
