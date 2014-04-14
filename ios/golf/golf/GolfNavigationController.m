//
//  NeiMengNavigationController.m
//  NeiMengPay
//
//  Created by zkjc on 11-10-25.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import "GolfNavigationController.h"
#import "GolfNavgationBarAdd.h"

@implementation GolfNavigationController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}



- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}
/*
-(void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated{

    [super pushViewController:viewController animated:animated];
}
- (UIViewController *)popViewControllerAnimated:(BOOL)animated{
    UIViewController * controller=[super popViewControllerAnimated:animated];
    return controller;
}
*/
#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
//    [self.navigationController.navigationBar setBarStyle:UIBarStyleBlack];
//    [self.navigationController.navigationBar setTranslucent:NO];
    [self.navigationBar needMyDisplay];
    // Do any additional setup after loading the view from its nib.
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
