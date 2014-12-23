//
//  AHPreviewViewController.m
//  ZYQAssetPickerControllerDemo
//
//  Created by mahh on 14-3-28.
//  Copyright (c) 2014年 heroims. All rights reserved.
//

#import "AHPreviewController.h"
#import "UIImageView+WebCache.h"

@interface AHPreviewController ()

@end

@implementation AHPreviewController

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
    //[[UIApplication sharedApplication]setStatusBarHidden:YES];
    self.navigationController.navigationBarHidden=NO;
    self.title=_previewTitle;
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _selectBtnFrameY=0;
    currentPage=0;
    self.previewImgMutableArray=[NSMutableArray array];
    for (int i=0; i<[_previewImgArray count]; i++) {
        [_previewImgMutableArray addObject:[_previewImgArray objectAtIndex:i]];
    }
    if ([self respondsToSelector:@selector(setEdgesForExtendedLayout:)])
    {
        [self setEdgesForExtendedLayout:UIRectEdgeNone];
        _selectBtnFrameY=15;
    }
    //添加上下工具条
//    [self addToolBar];
    UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(showTopBar)];
    [self.view addGestureRecognizer:tap];
    
    _previewScroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    _previewScroll.contentSize=CGSizeMake(self.view.frame.size.width*[_previewImgArray count], self.view.frame.size.height);
    _previewScroll.pagingEnabled=YES;
    _previewScroll.bounces=NO;
    
    
    for (int i=0; i<[_previewImgArray count]; i++) {
        float imgvWidth=SCREEN_WIDTH;
        float imgvHeight=SCREEN_HEIGHT-64;
        self.zoomImgv=[[UIImageView alloc]initWithFrame:CGRectMake(i*_previewScroll.frame.size.width,0, imgvWidth, imgvHeight)];
        _zoomImgv.contentMode=UIViewContentModeScaleAspectFill;
        _zoomImgv.clipsToBounds=YES;
        [_zoomImgv setImageWithURL:[_previewImgArray objectAtIndex:i]];
        [_previewScroll addSubview:_zoomImgv];
    }
    _pageControl=[[UIPageControl alloc] initWithFrame:CGRectMake(0, SCREEN_HEIGHT-64-60, _previewScroll.frame.size.width, 20)];
    _previewScroll.delegate=self;
    //
    
    [self.view addSubview:_previewScroll];
    [self.view addSubview:_pageControl];
    _pageControl.numberOfPages=[_previewImgArray count];
    //添加上边的工具条
//    [self addTopToolBar];
//    [self.view addSubview:_bottomBar];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark action method

-(void)backMethod
{
    //[[UIApplication sharedApplication]setStatusBarHidden:NO];
    self.navigationController.navigationBarHidden=NO;
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)selectMethod
{
    NSLog(@"_previewScroll=====%@",_previewScroll);
    int _selectIndex=_previewScroll.contentOffset.x/_previewScroll.frame.size.width;
    NSLog(@"_selectIndex====%d",_selectIndex);
    NSMutableArray *tmpStatusArray=[NSMutableArray array];
    for (int i=0; i<[_previewImgArray count]; i++) {
        
        if (i==_selectIndex) {//说明是当前页面
            if ([[[_statusDic objectForKey:@"status"] objectAtIndex:i] intValue]==0) {//说明之前是隐藏状态
                //让其显示
                [tmpStatusArray addObject:@"1"];
                [_previewImgMutableArray addObject:[_previewImgArray objectAtIndex:_selectIndex]];
                [_topBar addSubview:[self addSelectBtn:1 andIsInit:NO]];
            }
            else
            {
                //让其隐藏
                [tmpStatusArray addObject:@"0"];
                [_previewImgMutableArray removeObject:[_previewImgArray objectAtIndex:_selectIndex]];
                [_topBar addSubview:[self addSelectBtn:0 andIsInit:NO]];
            }
        }
        else
        {
            [tmpStatusArray addObject:[[_statusDic objectForKey:@"status"] objectAtIndex:i]];
        }
    }
    [_statusDic setValue:tmpStatusArray forKey:@"status"];
    if ([_previewImgMutableArray count]==0) {
        [_numBtn setHidden:YES];
        return;
    }
    else
    {
        [_numBtn setHidden:NO];
    }
        [_numBtn setTitle:[NSString stringWithFormat:@"%d",[_previewImgMutableArray count]] forState:UIControlStateNormal];
}
-(void)addToolBar
{
    
    //下边的工具条
    _bottomBar=[[UIToolbar alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-49, SCREEN_WIDTH, 49)];
    UIImageView *bottomImgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, _bottomBar.frame.size.width, _bottomBar.frame.size.height)];
    bottomImgv.image=[UIImage imageNamed:@"TabbarBkg_ios7"];
    bottomImgv.clipsToBounds=YES;
    bottomImgv.contentMode=UIViewContentModeScaleAspectFill;
    [_bottomBar insertSubview:bottomImgv atIndex:1];
    NSMutableArray *bottomBarItems=[NSMutableArray array];
    UIBarButtonItem *spaceItem=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [bottomBarItems addObject:spaceItem];
    
    UIView *doneView=[[UIView alloc]initWithFrame:CGRectMake(200, 0, 60, 44)];
    UIButton *doneBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    doneBtn.frame=CGRectMake(0, 8, 60, 40);
    [doneBtn setTitle:@"完成" forState:UIControlStateNormal];
    [doneBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [doneBtn setBackgroundImage:[UIImage imageNamed:@"button_normal"] forState:UIControlStateNormal];
    [doneBtn addTarget:self action:@selector(finishPickingAssets:) forControlEvents:UIControlEventTouchUpInside];
    [doneView addSubview:doneBtn];
    _numBtn=[[UIButton alloc]initWithFrame:CGRectMake(38, 0, 27, 28)];
    [_numBtn setBackgroundImage:[UIImage imageNamed:@"FriendsSendsPicturesNumberIcon_ios7"] forState:UIControlStateNormal];
    [_numBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [_numBtn setTitle:[NSString stringWithFormat:@"%d",[_previewImgArray count]] forState:UIControlStateNormal];
    [doneView addSubview:_numBtn];
    UIBarButtonItem *doneItem=[[UIBarButtonItem alloc]initWithCustomView:doneView];
    [bottomBarItems addObject:doneItem];
    
    _bottomBar.hidden=NO;
    [_bottomBar setItems:bottomBarItems];
    
}
-(void)removeTopToolBar
{
    if (_topBar!=nil) {
        [_topBar removeFromSuperview];
    }
}
-(void)addTopToolBar
{
    float _topBarH=44;
    if (IOS_VERSION>=7.0) {
        _topBarH=64;
    }
    _topBar=[[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, _topBarH)];
    UIImageView *topImgv=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, _topBar.frame.size.width, _topBar.frame.size.height)];
    topImgv.image=[UIImage imageNamed:@"navigationbg_ios7"];
    topImgv.clipsToBounds=YES;
    topImgv.contentMode=UIViewContentModeScaleAspectFill;
    [_topBar insertSubview:topImgv atIndex:1];
    NSMutableArray *topItems=[NSMutableArray array];
    
    UIButton *backBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    backBtn.frame=CGRectMake(5, 0, 44, 44);
    [backBtn setImage:[UIImage imageNamed:@"title_back_normal"] forState:UIControlStateNormal];
    [backBtn setImage:[UIImage imageNamed:@"title_back_pressed"] forState:UIControlStateHighlighted];
    [backBtn addTarget:self action:@selector(backMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *backItem=[[UIBarButtonItem alloc]initWithCustomView:backBtn];
    [topItems addObject:backItem];
    
    UIBarButtonItem *spaceItem=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [topItems addObject:spaceItem];

    UIBarButtonItem *selectItem=[[UIBarButtonItem alloc]initWithCustomView:[self addSelectBtn:1 andIsInit:YES]];
    
    
    NSMutableArray *tmpPageArray=[NSMutableArray array];
    NSMutableArray *tmpStatusArray=[NSMutableArray array];
    for (int i=0; i<[_previewImgArray count]; i++) {
        [tmpPageArray addObject:[NSString stringWithFormat:@"%d",i]];
        [tmpStatusArray addObject:@"1"];
    }
    _statusDic=[NSMutableDictionary dictionaryWithObjects:@[tmpPageArray,tmpStatusArray] forKeys:@[@"currentPage",@"status"]];
    [topItems addObject:selectItem];
    
    [_topBar setItems:topItems];
    _topBar.hidden=NO;
    [self.view addSubview:_topBar];
}
-(UIButton*)addSelectBtn:(int)tag andIsInit:(BOOL)isInit
{
    if (_selectBtn!=nil) {
        [_selectBtn removeFromSuperview];
    }
    self.selectBtn=[UIButton buttonWithType:UIButtonTypeCustom];
    CGRect selectBtnFrame;
    if(isInit==YES)
    {
        selectBtnFrame=CGRectMake(200, 0, 42, 42);
    }
    else{
        selectBtnFrame=CGRectMake(263, _selectBtnFrameY, 42, 42);
    }
    _selectBtn.frame=selectBtnFrame;
    if (tag==0) {//隐藏
        [_selectBtn setImage:[UIImage imageNamed:@"FriendsSendsPicturesSelectBigNIcon_ios7"] forState:UIControlStateNormal];
    }
    else
    {
        //显示
        [_selectBtn setImage:[UIImage imageNamed:@"FriendsSendsPicturesSelectBigYIcon_ios7"] forState:UIControlStateNormal];
    }
    
    [_selectBtn addTarget:self action:@selector(selectMethod) forControlEvents:UIControlEventTouchUpInside];
    return _selectBtn;
}
-(void)showTopBar
{
    _bottomBar.hidden=!_bottomBar.hidden;
    _topBar.hidden=!_topBar.hidden;
}


#pragma mark - UIScrollView Delegate

-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    _pageControl.currentPage=floor(scrollView.contentOffset.x/scrollView.frame.size.width);
    currentPage=floor(scrollView.contentOffset.x/scrollView.frame.size.width);
}
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    
    if ([[[_statusDic objectForKey:@"status"] objectAtIndex:currentPage] intValue]==0) {//说明当前是隐藏状态
        [_topBar addSubview:[self addSelectBtn:0 andIsInit:NO]];
    }
    else
    {
        [_topBar addSubview:[self addSelectBtn:1 andIsInit:NO]];
    }
}


@end
