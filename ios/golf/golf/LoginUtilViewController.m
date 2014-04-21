//
//  LoginUtilViewController.m
//  NeiMengPay
//
//  Created by qi zuowei on 12/19/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "LoginUtilViewController.h"
#import "AHLoginViewController.h"
//#import "NMRegistViewController.h"
@implementation LoginUtilViewController
@synthesize _target,_selector;

-(id)initWithRootViewController:(UIViewController *)rootViewController{
    AHLoginViewController * loginController=[[AHLoginViewController alloc] init];
    self=[super initWithRootViewController:loginController];
    if (self) {
        UIBarButtonItem * leftItem=[[UIBarButtonItem alloc] initWithTitle:@"返回" style:UIBarButtonItemStylePlain target:self action:@selector(goback)];
        loginController.navigationItem.leftBarButtonItem=leftItem;
        loginController.delegate=self;
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
}
-(void)goback{
    [self dismissViewControllerAnimated:YES completion:nil];
}
-(void)completeLogin{
    [self dismissViewControllerAnimated:YES completion:nil];
    if ([self._target respondsToSelector:_selector]) {
        [self._target performSelector:_selector withObject:nil afterDelay:0.1];
    }
}
-(void)setTarget:(id)target Selector:(SEL)selector{
    self._target=target;
    _selector=selector;
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
