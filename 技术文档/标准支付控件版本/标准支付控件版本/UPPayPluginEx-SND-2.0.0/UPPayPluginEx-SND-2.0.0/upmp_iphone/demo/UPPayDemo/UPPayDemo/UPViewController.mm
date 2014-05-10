//
//  UPViewController.m
//  UPPayDemo
//
//  Created by liwang on 12-11-12.
//  Copyright (c) 2012年 liwang. All rights reserved.
//
#include <sys/socket.h> // Per msqr
#include <sys/sysctl.h>
#include <net/if.h>
#include <net/if_dl.h>
#import "UPViewController.h"
#import "UPPayPlugin.h"

#define KBtn_width        200
#define KBtn_height       80
#define KXOffSet          (self.view.frame.size.width - KBtn_width) / 2
#define KYOffSet          80

#define kVCTitle          @"TN测试"
#define kBtnFirstTitle    @"新控件测试"
#define kBtnSecondTitle   @"老控件测试"
#define kWaiting          @"正在获取TN,请稍后..."
#define kNote             @"提示"         
#define kConfirm          @"确定"
#define kErrorNet         @"网络错误"
#define kResult           @"支付结果：%@"

#define kConfigTnUrl      @"http://218.80.192.213:1725/clsun/getLaa?id=2"
#define kNormalTnUrl      @"http://222.66.233.198:8080/sim/gettn"
//120.204.69.167:10306
//222.66.233.198:8080


@interface UPViewController ()

@end

@implementation UPViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    self.title = kVCTitle;
	// Do any additional setup after loading the view, typically from a nib.
    
    // Add the normalTn button
    CGFloat y = KYOffSet;
    UIButton* btnStartPay = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnStartPay setTitle:kBtnFirstTitle forState:UIControlStateNormal];
    [btnStartPay addTarget:self action:@selector(normalPayAction:) forControlEvents:UIControlEventTouchUpInside];
    [btnStartPay setFrame:CGRectMake(KXOffSet, y, KBtn_width, KBtn_height)];
    
    [self.view addSubview:btnStartPay];
    y += KBtn_height + KYOffSet;
    
    // Add the configTn button
    /*
    UIButton* btnConfig = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnConfig setTitle:kBtnSecondTitle forState:UIControlStateNormal];
    [btnConfig addTarget:self action:@selector(userPayAction:) forControlEvents:UIControlEventTouchUpInside];
    [btnConfig setFrame:CGRectMake(KXOffSet, y, KBtn_width, KBtn_height)];
    [self.view addSubview:btnConfig];
    */
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Alert

- (void)showAlertWait
{
    mAlert = [[UIAlertView alloc] initWithTitle:kWaiting message:nil delegate:self cancelButtonTitle:nil otherButtonTitles: nil];
    [mAlert show];
    UIActivityIndicatorView* aiv = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
    aiv.center = CGPointMake(mAlert.frame.size.width / 2.0f - 15, mAlert.frame.size.height / 2.0f + 10 );
    [aiv startAnimating];
    [mAlert addSubview:aiv];
    [aiv release];
    [mAlert release];
}

- (void)showAlertMessage:(NSString*)msg
{
    mAlert = [[UIAlertView alloc] initWithTitle:kNote message:msg delegate:nil cancelButtonTitle:kConfirm otherButtonTitles:nil, nil];
    [mAlert show];
    [mAlert release];
}
- (void)hideAlert
{
    if (mAlert != nil)
    {
        [mAlert dismissWithClickedButtonIndex:0 animated:YES];
        mAlert = nil;
    }
}

#pragma mark - UPPayPlugin Test


- (void)userPayAction:(id)sender
{
     NSString* urlString = [NSString stringWithFormat:kConfigTnUrl];
     NSURL* url = [NSURL URLWithString:urlString];
     
     NSMutableURLRequest * urlRequest=[NSMutableURLRequest requestWithURL:url];
     NSURLConnection* urlConn = [[NSURLConnection alloc] initWithRequest:urlRequest delegate:self];
     [urlConn start];
     [self showAlertWait];
}


- (void)normalPayAction:(id)sender
{
    NSURL* url = [NSURL URLWithString:kNormalTnUrl];
	NSMutableURLRequest * urlRequest=[NSMutableURLRequest requestWithURL:url];
    NSURLConnection* urlConn = [[NSURLConnection alloc] initWithRequest:urlRequest delegate:self];
    [urlConn start];
    [self showAlertWait];
}


- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse*)response
{
    NSHTTPURLResponse* rsp = (NSHTTPURLResponse*)response;
    int code = [rsp statusCode];
    if (code != 200)
    {
        [self hideAlert];
        [self showAlertMessage:kErrorNet];
        [connection cancel];
        [connection release];
        connection = nil;
    }
    else
    {
        if (mData != nil)
        {
            [mData release];
            mData = nil;
        }
        mData = [[NSMutableData alloc] init];
    }
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [mData appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    [self hideAlert];
    NSString* tn = [[NSMutableString alloc] initWithData:mData encoding:NSUTF8StringEncoding];
    if (tn != nil && tn.length > 0)
    {
        NSLog(@"tn=%@",tn);
        [UPPayPlugin startPay:tn sysProvide:@"12345678" spId:@"1234" mode:@"01" viewController:self delegate:self];
    }
    [tn release];
    [connection release];
    connection = nil;
    
}

-(void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    [self hideAlert];
    [self showAlertMessage:kErrorNet];
    [connection release];
    connection = nil;
}

- (void)UPPayPluginResult:(NSString *)result
{
    NSString* msg = [NSString stringWithFormat:kResult, result];
    [self showAlertMessage:msg];
}

//get mac

- (NSString*)currentUID
{
    int                 mib[6];
    size_t              len;
    char                *buf;
    unsigned char       *ptr;
    struct if_msghdr    *ifm;
    struct sockaddr_dl  *sdl;
    
    mib[0] = CTL_NET;
    mib[1] = AF_ROUTE;
    mib[2] = 0;
    mib[3] = AF_LINK;
    mib[4] = NET_RT_IFLIST;
    
    NSString* noUID = @"";
    
    if ((mib[5] = if_nametoindex("en0")) == 0) {
        return noUID;
    }
    
    if (sysctl(mib, 6, NULL, &len, NULL, 0) < 0) {
        return noUID;
    }
    
    if ((buf = (char*)malloc(len)) == NULL) {
        return noUID;
    }
    
    if (sysctl(mib, 6, buf, &len, NULL, 0) < 0) {
        free(buf);
        return noUID;
    }
    
    ifm = (struct if_msghdr *)buf;
    sdl = (struct sockaddr_dl *)(ifm + 1);
    ptr = (unsigned char *)LLADDR(sdl);
    NSString *outstring = [NSString stringWithFormat:@"%02x%02x%02x%02x%02x%02x",
                           *ptr, *(ptr+1), *(ptr+2), *(ptr+3), *(ptr+4), *(ptr+5)];
    free(buf);
    
    return outstring;
}

@end
