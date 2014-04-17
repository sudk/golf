//
//  NeiMengtabBarControllerController.m
//  NeiMengPay
//
//  Created by zkjc on 11-10-25.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//[Utils colorWithHexString:@"#f0f1f3"]

#import "GolfTabBarController.h"

@implementation GolfTabBarController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)dealloc
{
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    self.view.backgroundColor=[UIColor whiteColor];
    
    if ([self.tabBar viewWithTag:1]) {
        return;
    }
    
    for (int i=1; i<=4; i++) {
        
        UIButton *button=[UIButton buttonWithType:UIButtonTypeCustom];
        button.frame=CGRectMake((i-1)*320/4, 0, 320/4, 49);
        button.tag=i;
        [self.tabBar addSubview:button];
        [button addTarget:self action:@selector(buttonAction:) forControlEvents:UIControlEventTouchUpInside];
        switch (i) {
            case 1:
                [button setBackgroundImage:[UIImage imageNamed:@"tab_kx_pressed.png"] forState:UIControlStateNormal];
                break;
            case 2:
                [button setBackgroundImage:[UIImage imageNamed:@"tab_contact_normal.png"] forState:UIControlStateNormal];
                break;
            case 3:
                [button setBackgroundImage:[UIImage imageNamed:@"tab_setting_normal.png"] forState:UIControlStateNormal];
                break;
            case 4:
                [button setBackgroundImage:[UIImage imageNamed:@"tab_num_normal.png"] forState:UIControlStateNormal];
                break;
                
            default:
                break;
        }
    }
    // Do any additional setup after loading the view from its nib.
}

-(void)_butttonActionWithtag1{
    [(UIButton *)[self.tabBar viewWithTag:1] setBackgroundImage:[UIImage imageNamed:@"tab_kx_pressed.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:2] setBackgroundImage:[UIImage imageNamed:@"tab_contact_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:3] setBackgroundImage:[UIImage imageNamed:@"tab_setting_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:4] setBackgroundImage:[UIImage imageNamed:@"tab_num_normal.png"] forState:UIControlStateNormal];
    self.selectedIndex=0;
}
-(void)_butttonActionWithtag2{
   [(UIButton *)[self.tabBar viewWithTag:1] setBackgroundImage:[UIImage imageNamed:@"tab_kx_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:2] setBackgroundImage:[UIImage imageNamed:@"tab_contact_pressed.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:3] setBackgroundImage:[UIImage imageNamed:@"tab_setting_normal.png"] forState:UIControlStateNormal];
    
    [(UIButton *)[self.tabBar viewWithTag:4] setBackgroundImage:[UIImage imageNamed:@"tab_num_normal.png"] forState:UIControlStateNormal];
    self.selectedIndex=1;
}
-(void)_butttonActionWithtag3{
    
    [(UIButton *)[self.tabBar viewWithTag:1] setBackgroundImage:[UIImage imageNamed:@"tab_kx_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:2] setBackgroundImage:[UIImage imageNamed:@"tab_contact_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:3] setBackgroundImage:[UIImage imageNamed:@"tab_setting_pressed.png"] forState:UIControlStateNormal];
    
    [(UIButton *)[self.tabBar viewWithTag:4] setBackgroundImage:[UIImage imageNamed:@"tab_num_normal.png"] forState:UIControlStateNormal];
    self.selectedIndex=2;
}
-(void)_butttonActionWithtag4{
    
    [(UIButton *)[self.tabBar viewWithTag:1] setBackgroundImage:[UIImage imageNamed:@"tab_kx_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:2] setBackgroundImage:[UIImage imageNamed:@"tab_contact_normal.png"] forState:UIControlStateNormal];
    [(UIButton *)[self.tabBar viewWithTag:3] setBackgroundImage:[UIImage imageNamed:@"tab_setting_normal.png"] forState:UIControlStateNormal];
    
    [(UIButton *)[self.tabBar viewWithTag:4] setBackgroundImage:[UIImage imageNamed:@"tab_num_pressed.png"] forState:UIControlStateNormal];
    self.selectedIndex=3;
}
-(void)buttonAction:(UIButton *)button{
    if (button.tag==self.selectedIndex+1) {
        return;
    }
    switch (button.tag) {
        case 1:
            [self _butttonActionWithtag1];
            break;
        case 2:
            [self _butttonActionWithtag2];
            break;
        case 3:
            [self _butttonActionWithtag3];
            break;
        case 4:
            [self _butttonActionWithtag4];
            break;
            
        default:
            break;
    }
}
- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
