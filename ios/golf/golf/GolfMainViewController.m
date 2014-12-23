//
//  GolfMainViewController.m
//  iaptest
//
//  Created by mahh on 13-7-18.
//  Copyright (c) 2013年 mahh. All rights reserved.
//

#import "GolfMainViewController.h"
#import "GolfNavigationController.h"


@interface GolfMainViewController ()

@end

@implementation GolfMainViewController


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
    // Do any additional setup after loading the view from its nib.
    self.navigationController.navigationBarHidden=YES;
    NSLog(@"self.navigationController=====%@",self.navigationController);

    _searchCourtHomeViewController=[[SearchCourtHomeViewController alloc]init];
    _findHomeViewController=[[FindHomeViewController alloc]init];
//    _recommendHomeViewController=[[RecommendHomeViewController alloc]init];
    _myAccountHomeViewController=[[MyAccountHomeViewController alloc]init];

    GolfNavigationController *searchCourtNavc=[[GolfNavigationController alloc]initWithRootViewController:_searchCourtHomeViewController];
    GolfNavigationController *findNavc=[[GolfNavigationController alloc]initWithRootViewController:_findHomeViewController];
//    GolfNavigationController *recommendNavc=[[GolfNavigationController alloc]initWithRootViewController:_recommendHomeViewController];
    GolfNavigationController *myAccountNavc=[[GolfNavigationController alloc]initWithRootViewController:_myAccountHomeViewController];
    _golfTabBarController=[[GolfTabBarController alloc]init];
    
    _golfTabBarController.viewControllers=[NSArray arrayWithObjects:searchCourtNavc,findNavc,myAccountNavc,nil];
    [self.navigationController pushViewController:_golfTabBarController animated:YES];

}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
