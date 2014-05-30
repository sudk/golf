//
//  FindHomeViewController.m
//  golf
//
//  Created by mahh on 14-4-2.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "FindHomeViewController.h"
#import "Utils.h"

@interface FindHomeViewController ()

@end

@implementation FindHomeViewController

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
    self.title=@"发现";
    [self addadvMethod];
    //特惠套餐寄卖排行榜
    UIButton *specialsBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, _adScrollView.frame.origin.y+_adScrollView.frame.size.height+10, 90, 80)];
    [specialsBtn setBackgroundColor:[Utils colorWithHexString:@"#118bdf"]];
    [specialsBtn setTitle:@"特惠" forState:UIControlStateNormal];
    [specialsBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [specialsBtn addTarget:self action:@selector(specialsMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:specialsBtn];
    
    UIButton *packageBtn=[[UIButton alloc]initWithFrame:CGRectMake(115, _adScrollView.frame.origin.y+_adScrollView.frame.size.height+10, 90, 80)];
    [packageBtn setBackgroundColor:[Utils colorWithHexString:@"#8586bb"]];
    [packageBtn setTitle:@"套餐" forState:UIControlStateNormal];
    [packageBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [packageBtn addTarget:self action:@selector(packageMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:packageBtn];
    
    UIButton *consignmentBtn=[[UIButton alloc]initWithFrame:CGRectMake(220, _adScrollView.frame.origin.y+_adScrollView.frame.size.height+10, 90, 80)];
    [consignmentBtn setBackgroundColor:[Utils colorWithHexString:@"#143e91"]];
    [consignmentBtn setTitle:@"寄卖" forState:UIControlStateNormal];
    [consignmentBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [consignmentBtn addTarget:self action:@selector(consignmentMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:consignmentBtn];
    
    UIButton *rankingBtn=[[UIButton alloc]initWithFrame:CGRectMake(10, _adScrollView.frame.origin.y+_adScrollView.frame.size.height+100, 90, 80)];
    [rankingBtn setBackgroundColor:[Utils colorWithHexString:@"#129289"]];
    [rankingBtn setTitle:@"排行榜" forState:UIControlStateNormal];
    [rankingBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [rankingBtn addTarget:self action:@selector(rankingMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:rankingBtn];
    
    UIButton *merchantsBtn=[[UIButton alloc]initWithFrame:CGRectMake(115, _adScrollView.frame.origin.y+_adScrollView.frame.size.height+100, 90, 80)];
    [merchantsBtn setBackgroundColor:[Utils colorWithHexString:@"#129289"]];
    [merchantsBtn setTitle:@"特约\n商户" forState:UIControlStateNormal];
    [merchantsBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [merchantsBtn addTarget:self action:@selector(merchantsMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:merchantsBtn];
    
    UIButton *newsBtn=[[UIButton alloc]initWithFrame:CGRectMake(220, _adScrollView.frame.origin.y+_adScrollView.frame.size.height+100, 90, 80)];
    [newsBtn setBackgroundColor:[Utils colorWithHexString:@"#129289"]];
    [newsBtn setTitle:@"新闻" forState:UIControlStateNormal];
    [newsBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [newsBtn addTarget:self action:@selector(newsMethod) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:newsBtn];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)addadvMethod
{
    //广告位
    _adImgArray=@[[UIImage imageNamed:@"guide1.jpg"],[UIImage imageNamed:@"guide2.jpg"],[UIImage imageNamed:@"guide3.jpg"]];
    int pageControlHeight = 10;
    float _adScrollViewY=0;
    _adScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, _adScrollViewY, SCREEN_WIDTH, SCREEN_HEIGHT/3.0)];
    _adPageControl = [[StyledPageControl alloc] initWithFrame:CGRectMake(0, _adScrollView.bounds.size.height+_adScrollViewY-18-pageControlHeight, [UIScreen mainScreen].bounds.size.width, pageControlHeight)];
    [self.view addSubview:_adScrollView];
    [self.view addSubview:_adPageControl];
    
    
    // in the meantime, load the array with placeholders which will be replaced on demand
    NSMutableArray *views = [[NSMutableArray alloc] init];
    for (unsigned i = 0; i < [_adImgArray count]; i++) {
        [views addObject:[NSNull null]];
    }
    _adImgvArray = views;
    _adScrollView.pagingEnabled = YES;
    _adScrollView.contentSize = CGSizeMake(_adScrollView.frame.size.width * [_adImgArray count], _adScrollView.frame.size.height);
    _adScrollView.bounces=NO;
    _adScrollView.alwaysBounceVertical=NO;
    _adScrollView.showsHorizontalScrollIndicator = NO;
    _adScrollView.showsVerticalScrollIndicator = NO;
    _adScrollView.scrollsToTop = NO;
    _adScrollView.delegate = self;
    
    _adPageControl.numberOfPages = [_adImgArray count];
    _adPageControl.currentPage = 0;
    [_adPageControl setCoreSelectedColor:[Utils colorWithHexString:@"#ff7a00"]];
    [_adPageControl setCoreNormalColor:[Utils colorWithHexString:@"#bcbcbc"]];
    
    _timeCount = 0;
    [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(scrollTimer) userInfo:nil repeats:YES];
    
    // pages are created on demand
    // load the visible page
    // load the page on either side to avoid flashes when the user starts scrolling
    [self loadScrollViewWithPage];
}
/**
 广告位相关的方法
 */
- (void)scrollTimer
{
    _timeCount ++;
    if (_timeCount == [_adImgArray count]) {
        _timeCount = 0;
    }
    [_adScrollView scrollRectToVisible:CGRectMake(_timeCount * _adScrollView.frame.size.width, _adScrollView.frame.origin.y, _adScrollView.frame.size.width, SCREEN_HEIGHT/3.0) animated:YES];
}
- (void)loadScrollViewWithPage {
    // replace the placeholder if necessary
    for (int i=0; i<[_adImgArray count]; i++) {
        UIImageView *view = [_adImgvArray objectAtIndex:i];
        if ((NSNull *)view == [NSNull null]) {
            UIImage *image = [_adImgArray objectAtIndex:i];
            view = [[UIImageView alloc] initWithImage:image];
            [_adImgvArray replaceObjectAtIndex:i withObject:view];
        }
        
        // add the controller's view to the scroll view
        if (nil == view.superview) {
            CGRect frame = _adScrollView.frame;
            frame.origin.x = frame.size.width * i;
            frame.origin.y = 0;
            view.frame = frame;
            [_adScrollView addSubview:view];
        }
    }
    
}
#pragma mark -scroll view delegate
- (void)scrollViewDidScroll:(UIScrollView *)sender {
    // We don't want a "feedback loop" between the UIPageControl and the scroll delegate in
    // which a scroll event generated from the user hitting the page control triggers updates from
    // the delegate method. We use a boolean to disable the delegate logic when the page control is used.
    // Switch the indicator when more than 50% of the previous/next page is visible
    CGFloat pageWidth = _adScrollView.frame.size.width;
    int page = floor((_adScrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
    _adPageControl.currentPage = page;
	
    // load the visible page and the page on either side of it (to avoid flashes when the user starts scrolling)
    // A possible optimization would be to unload the views+controllers which are no longer visible
}

// At the begin of scroll dragging, reset the boolean used when scrolls originate from the UIPageControl
- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
}

// At the end of scroll animation, reset the boolean used when scrolls originate from the UIPageControl
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
}
-(void)specialsMethod
{
    
}
-(void)packageMethod
{
    
}
-(void)consignmentMethod
{
    
}
-(void)rankingMethod
{
    
}
-(void)merchantsMethod
{
    
}
-(void)newsMethod
{
    
}
@end
