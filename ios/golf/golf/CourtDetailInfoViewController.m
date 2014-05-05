//
//  CourtDetailInfoViewController.m
//  golf
//
//  Created by mahh on 14-4-12.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "CourtDetailInfoViewController.h"
#import "UIButton+WebCache.h"
#import "UIImageView+WebCache.h"

@interface CourtDetailInfoViewController ()

@end

@implementation CourtDetailInfoViewController

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
    self.title=@"球场信息";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _httpUtils=[[HttpUtils alloc]init];
    self.nearbyInfoArray=[NSArray array];
    
    _courtInfoScroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64)];
    _courtInfoScroll.contentSize=CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT*1.8);
    [self.view addSubview:_courtInfoScroll];
    _courtInfoTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 270) style:UITableViewStylePlain];
    _courtInfoTable.dataSource=self;
    _courtInfoTable.delegate=self;
    _courtInfoTable.backgroundColor=[UIColor clearColor];
    _courtInfoTable.rowHeight=30;
    [_courtInfoScroll addSubview:_courtInfoTable];
    self.courtInfoTitleArray=@[@"球场模式",@"建立时间",@"球场面积",@"果岭草种",@"球场数据",@"设计师",@"球道长度",@"球道草种"];
    
//    [self courtDetailInfo];
    [self courtFacilitiesInfo];
    UIView *phnView=[[UIView alloc]initWithFrame:CGRectMake(0, _courtInfoTable.frame.origin.y+_courtInfoTable.frame.size.height+10, SCREEN_WIDTH, 40)];
    phnView.backgroundColor=[UIColor whiteColor];
    UILabel *phnLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 40)];
    phnLabel.backgroundColor=[UIColor clearColor];
    phnLabel.text=@"球场电话";
    
    self.phnBtn=[[UIButton alloc]initWithFrame:CGRectMake(phnLabel.frame.origin.x+phnLabel.frame.size.width, 0, 180, 40)];
    [_phnBtn setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
    [_phnBtn setTitle:_phnStr forState:UIControlStateNormal];
    [_phnBtn addTarget:self action:@selector(callMethod:) forControlEvents:UIControlEventTouchUpInside];
    [phnView addSubview:phnLabel];
    [phnView addSubview:_phnBtn];
    [_courtInfoScroll addSubview:phnView];
    
    UIView *courtWayView;
    if ([_fairwayImgArray count]>0) {
    courtWayView=[[UIView alloc]initWithFrame:CGRectMake(0, phnView.frame.origin.y+phnView.frame.size.height+10, SCREEN_WIDTH, 70)];
    courtWayView.backgroundColor=[UIColor whiteColor];
    UILabel *courtWaylabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 15, 80, 40)];
    courtWaylabel.text=@"球道详情";
    courtWaylabel.backgroundColor=[UIColor clearColor];
    [courtWayView addSubview:courtWaylabel];
    [_courtInfoScroll addSubview:courtWayView];
        self.fcourtWayBtn=[[UIButton alloc]initWithFrame:CGRectMake(95, 5, 60, 60)];
        [_fcourtWayBtn setImageWithURL:[NSURL URLWithString:[NSStringSmall smallSpliceStr:[self.fairwayImgArray objectAtIndex:0]]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"defaultImg"]];
        [_fcourtWayBtn addTarget:self action:@selector(showCourtWayImgs) forControlEvents:UIControlEventTouchUpInside];
        [courtWayView addSubview:_fcourtWayBtn];
    }
    if ([_fairwayImgArray count]>1) {
        self.scourtWayBtn=[[UIButton alloc]initWithFrame:CGRectMake(160, 5, 60, 60)];
        [_scourtWayBtn setImageWithURL:[NSURL URLWithString:[NSStringSmall smallSpliceStr:[self.fairwayImgArray objectAtIndex:1]]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"defaultImg"]];
        [_scourtWayBtn addTarget:self action:@selector(showCourtWayImgs) forControlEvents:UIControlEventTouchUpInside];
        [courtWayView addSubview:_scourtWayBtn];
    }
    if ([_fairwayImgArray count]>2) {
        self.tcourtWayBtn=[[UIButton alloc]initWithFrame:CGRectMake(225, 5, 60, 60)];
        [_tcourtWayBtn setImageWithURL:[NSURL URLWithString:[NSStringSmall smallSpliceStr:[self.fairwayImgArray objectAtIndex:2]]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"defaultImg"]];
        [_tcourtWayBtn addTarget:self action:@selector(showCourtWayImgs) forControlEvents:UIControlEventTouchUpInside];
        [courtWayView addSubview:_tcourtWayBtn];
    }
    [_courtInfoScroll addSubview:courtWayView];
    UILabel *desclabel;
    if ([_fairwayImgArray count]==0) {
        desclabel=[[UILabel alloc]initWithFrame:CGRectMake(10, phnView.frame.origin.y+phnView.frame.size.height+10, 100, 40)];
    }
    else
    {
        desclabel=[[UILabel alloc]initWithFrame:CGRectMake(10, courtWayView.frame.origin.y+courtWayView.frame.size.height+10, 100, 40)];
    }
    desclabel.text=@"球场简介";
    desclabel.backgroundColor=[UIColor clearColor];
    [_courtInfoScroll addSubview:desclabel];
    
    UIView *descView=[[UIView alloc]initWithFrame:CGRectMake(0, desclabel.frame.origin.y+desclabel.frame.size.height, SCREEN_WIDTH, 100)];
    descView.backgroundColor=[UIColor whiteColor];
    self.descInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, descView.frame.size.width, descView.frame.size.height)];
    _descInfo.backgroundColor=[UIColor clearColor];
    _descInfo.text=_descInfoStr;
    _descInfo.numberOfLines=7;
    _descInfo.lineBreakMode=NSLineBreakByWordWrapping;
    _descInfo.font=[UIFont systemFontOfSize:14];
    [descView addSubview:_descInfo];
    [_courtInfoScroll addSubview:descView];
    
    UILabel *facilitylabel=[[UILabel alloc]initWithFrame:CGRectMake(10, descView.frame.origin.y+descView.frame.size.height+10, 100, 40)];
    facilitylabel.text=@"球场设施";
    facilitylabel.backgroundColor=[UIColor clearColor];
    [_courtInfoScroll addSubview:facilitylabel];
    
    self.facilityView=[[UIView alloc]initWithFrame:CGRectMake(0, facilitylabel.frame.origin.y+facilitylabel.frame.size.height, SCREEN_WIDTH, 40)];
    _facilityView.backgroundColor=[UIColor whiteColor];
    self.facilityInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, _facilityView.frame.size.width, _facilityView.frame.size.height)];
    _facilityInfo.backgroundColor=[UIColor clearColor];
    _facilityInfo.text=_facilityInfoStr;
    _facilityInfo.numberOfLines=2;
    _facilityInfo.lineBreakMode=NSLineBreakByWordWrapping;
    _facilityInfo.font=[UIFont systemFontOfSize:14];
    [_facilityView addSubview:_facilityInfo];
    [_courtInfoScroll addSubview:_facilityView];
    
//    UILabel *evaluationlabel=[[UILabel alloc]initWithFrame:CGRectMake(10, facilityView.frame.origin.y+facilityView.frame.size.height+10, 100, 40)];
//    evaluationlabel.text=@"球场评价";
//    evaluationlabel.backgroundColor=[UIColor clearColor];
//    [_courtInfoScroll addSubview:evaluationlabel];
    
//    UIView *evaluationView=[[UIView alloc]initWithFrame:CGRectMake(0, evaluationlabel.frame.origin.y+evaluationlabel.frame.size.height, SCREEN_WIDTH, 40)];
//    evaluationView.backgroundColor=[UIColor whiteColor];
//    UILabel *evaluationInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, evaluationView.frame.size.width, evaluationView.frame.size.height)];
//    evaluationInfo.backgroundColor=[UIColor clearColor];
//    evaluationInfo.text=@"环境优雅，环境优雅，环境优雅，环境优雅，环境优雅，环境优雅";
//    evaluationInfo.numberOfLines=2;
//    evaluationInfo.lineBreakMode=NSLineBreakByWordWrapping;
//    evaluationInfo.font=[UIFont systemFontOfSize:14];
//    [evaluationView addSubview:evaluationInfo];
//    [_courtInfoScroll addSubview:evaluationView];
    
    UILabel *nearbylabel=[[UILabel alloc]initWithFrame:CGRectMake(10, _facilityView.frame.origin.y+_facilityView.frame.size.height+10, 100, 40)];
    nearbylabel.text=@"附近的店";
    nearbylabel.backgroundColor=[UIColor clearColor];
    [_courtInfoScroll addSubview:nearbylabel];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)customNearByBtn:(CGRect)frame andIndex:(int)index andnearbyView:(UIView *)nearbyView
{
    UIButton *nearbyBtn=[[UIButton alloc]initWithFrame:frame];
    NSLog(@"=====%@",[NSStringSmall smallSpliceStr:[[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"imgs"] objectAtIndex:0]]);
    [nearbyBtn setImageWithURL:[NSURL URLWithString:[NSStringSmall smallSpliceStr:[[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"imgs"] objectAtIndex:0]]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"defaultImg"]];
    nearbyBtn.tag=index;
    [nearbyBtn addTarget:self action:@selector(showNearbyImgs:) forControlEvents:UIControlEventTouchUpInside];
    [nearbyView addSubview:nearbyBtn];
}
-(void)customNearByLabel:(int)index andNameFrame:(CGRect)nameFrame andInfoFrame:(CGRect)infoFrame andnearbyView:(UIView *)nearbyView
{
    NSString *facilitie_nameStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"facilitie_name"];//巴山夜雨
    if (facilitie_nameStr==nil) {
        facilitie_nameStr=@"";
    }
    NSString *typeStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"type"];//吃
    if (typeStr==nil) {
        typeStr=@"";
    }
    NSString *featureStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"feature"];//好吃的菜
    if (featureStr==nil) {
        featureStr=@"";
    }
    featureStr=[featureStr stringByAppendingString:@"\n"];
    NSString *consumptionStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"consumption"];//200元
    if (consumptionStr==nil) {
        consumptionStr=@"";
    }
    consumptionStr=[consumptionStr stringByAppendingString:@"   "];
    NSString *favourableStr=@"";
    if ([[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"favourable"] intValue]==1) {
        favourableStr=@"绿卡优惠";
    }
    favourableStr=[favourableStr stringByAppendingString:@"  "];
    NSString *phoneStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"phone"];
    if (phoneStr==nil) {
        phoneStr=@"";
    }
    phoneStr=[phoneStr stringByAppendingString:@"\n"];
    NSString *addrStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"addr"];
    if (addrStr==nil) {
        addrStr=@"";
    }
    addrStr=[addrStr stringByAppendingString:@"\n"];
    NSString *distanceStr=[[_nearbyInfoArray objectAtIndex:index] objectForKey:@"distance"];
    if (distanceStr==nil) {
        distanceStr=@"";
    }
    distanceStr=[@"距离球场" stringByAppendingString:distanceStr];
    NSString *infoStr=[[[[[featureStr stringByAppendingString:consumptionStr] stringByAppendingString:favourableStr] stringByAppendingString:phoneStr] stringByAppendingString:addrStr] stringByAppendingString:distanceStr];
    UILabel *nearbyNameInfo=[[UILabel alloc]initWithFrame:nameFrame];
    nearbyNameInfo.backgroundColor=[UIColor clearColor];
    nearbyNameInfo.text=facilitie_nameStr;
    nearbyNameInfo.font=[UIFont systemFontOfSize:14];
    [nearbyView addSubview:nearbyNameInfo];
    
    UILabel *nearbyInfo=[[UILabel alloc]initWithFrame:infoFrame];
    nearbyInfo.backgroundColor=[UIColor clearColor];
    nearbyInfo.font=[UIFont systemFontOfSize:12];
    nearbyInfo.text=infoStr;
    nearbyInfo.numberOfLines=7;
    nearbyInfo.lineBreakMode=NSLineBreakByWordWrapping;
    [nearbyView addSubview:nearbyInfo];
}
-(void)addNearByView
{
    UIView *nearbyView=[[UIView alloc]initWithFrame:CGRectMake(0, _facilityView.frame.origin.y+_facilityView.frame.size.height+50, SCREEN_WIDTH, 300)];
    nearbyView.backgroundColor=[UIColor whiteColor];
    if ([_nearbyInfoArray count]>0) {
        [self customNearByBtn:CGRectMake(10, 2, 90, 90) andIndex:0 andnearbyView:nearbyView];
        [self customNearByLabel:0 andNameFrame:CGRectMake(105, 2, 215, 20) andInfoFrame:CGRectMake(105, 22, 215, 76) andnearbyView:nearbyView];
    }
    if ([_nearbyInfoArray count]>1) {
        [self customNearByBtn:CGRectMake(10, 96, 90, 90) andIndex:1 andnearbyView:nearbyView];
        [self customNearByLabel:1 andNameFrame:CGRectMake(105, 96, 215, 20) andInfoFrame:CGRectMake(105, 116, 215, 76) andnearbyView:nearbyView];
    }
    if ([_nearbyInfoArray count]>2) {
        [self customNearByBtn:CGRectMake(10, 190, 90, 90) andIndex:2 andnearbyView:nearbyView];
        [self customNearByLabel:2 andNameFrame:CGRectMake(105, 190, 215, 20) andInfoFrame:CGRectMake(105, 210, 215, 76) andnearbyView:nearbyView];
    }
    [_courtInfoScroll addSubview:nearbyView];
}
//附近的店
-(void)courtFacilitiesInfo
{
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"court/facilities" forKey:@"cmd"];
    [dic setObject:_courtId forKey:@"court_id"];
    [dic setObject:@"" forKey:@"id"];
    [dic setObject:@"" forKey:@"facilitie_name"];
    [dic setObject:@"" forKey:@"type"];
    [dic setObject:@"0" forKey:@"_pg_"];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(courtFacilitiesInfoMethod:) name:@"com.golf.courtFacilitiesInfoMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"court/facilities" andNotificationName:@"com.golf.courtFacilitiesInfoMethod" andViewControler:nil];
}
-(void)courtFacilitiesInfoMethod:(NSNotification *)notification
{
    NSDictionary *faciDic=[notification object];
    NSLog(@"球场信息界面附近的店回调====%@",faciDic);
    NSNumber *statusNum=[faciDic objectForKey:@"status"];
    if ([statusNum intValue]==0) {
        _nearbyInfoArray=[faciDic objectForKey:@"data"];
        [self addNearByView];
    }
    else
    {
        NSString *descStr=[faciDic objectForKey:@"desc"];
        if (descStr==nil) {
            descStr=networkAbnormalInfo;
        }
        self.notificationText=descStr;
        [self displayNotification];
    }
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.courtFacilitiesInfoMethod" object:nil];
}
-(void)courtDetailInfo
{
    NSMutableDictionary *dic=[NSMutableDictionary dictionary];
    [dic setObject:@"court/info" forKey:@"cmd"];
    [dic setObject:_courtId forKey:@"court_id"];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(courtDetailInfoMethod:) name:@"com.golf.ahCourtDetailInfoMethod" object:nil];
    [_httpUtils startRequest:dic andUrl:baseUrlStr andRequestField:@"court/info" andNotificationName:@"com.golf.ahCourtDetailInfoMethod" andViewControler:nil];
}
-(void)courtDetailInfoMethod:(NSNotification *)notification
{
    NSLog(@"球场信息界面详情====%@",[notification object]);
    NSDictionary *dataArray=[[notification object]objectForKey:@"data"];
    NSString *modelStr=[dataArray  objectForKey:@"model"];
    if (modelStr==nil) {
        modelStr=@"";
    }
    NSString *create_yearStr=[dataArray  objectForKey:@"create_year"];
    if (create_yearStr==nil) {
        create_yearStr=@"";
    }
    NSString *areaStr=[dataArray  objectForKey:@"area"];
    if (areaStr==nil) {
        areaStr=@"";
    }
    NSString *green_grassStr=[dataArray  objectForKey:@"green_grass"];
    if (green_grassStr==nil) {
        green_grassStr=@"";
    }
    NSString *court_dataStr=[dataArray objectForKey:@"court_data"];
    if (court_dataStr==nil) {
        court_dataStr=@"";
    }
    NSString *designerStr=[dataArray objectForKey:@"designer"];
    if (designerStr==nil) {
        designerStr=@"";
    }
    NSString *fairway_lengthStr=[dataArray  objectForKey:@"fairway_length"];
    if (fairway_lengthStr==nil) {
        fairway_lengthStr=@"";
    }
    NSString *fairway_grassStr=[dataArray  objectForKey:@"fairway_grass"];
    if (fairway_grassStr==nil) {
        fairway_grassStr=@"";
    }
    if ([_courtInfoArray count]!=0) {
        [_courtInfoArray removeAllObjects];
    }
    self.courtInfoArray=[NSMutableArray array];
    [_courtInfoArray addObject:modelStr];
    [_courtInfoArray addObject:create_yearStr];
    [_courtInfoArray addObject:areaStr];
    [_courtInfoArray addObject:green_grassStr];
    [_courtInfoArray addObject:court_dataStr];
    [_courtInfoArray addObject:designerStr];
    [_courtInfoArray addObject:fairway_lengthStr];
    [_courtInfoArray addObject:fairway_grassStr];
    [_courtInfoTable reloadData];
    [_phnBtn setTitle:[dataArray  objectForKey:@"phone"] forState:UIControlStateNormal];
    _descInfo.text=[dataArray  objectForKey:@"remark"];
    _facilityInfo.text=[dataArray  objectForKey:@"facilities"];
    _fairwayImgArray=[dataArray  objectForKey:@"fairway_imgs"];
    //http://115.28.77.119/images/picture/20140410/230742147.png
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"com.golf.ahCourtDetailInfoMethod" object:nil];
}
#pragma mark -TableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_courtInfoTitleArray count];
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell=[[UITableViewCell alloc]init];
    UILabel *titleLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 5, 80, 20)];
    titleLabel.text=[_courtInfoTitleArray objectAtIndex:[indexPath row]];
    [cell.contentView addSubview:titleLabel];
    
    UILabel *contentLabel=[[UILabel alloc]initWithFrame:CGRectMake(95, 5, 205, 20)];
    contentLabel.text=[_courtInfoArray objectAtIndex:[indexPath row]];
    [cell.contentView addSubview:contentLabel];
    tableView.separatorColor=[UIColor clearColor];
    return cell;
}
#pragma mark -method
-(IBAction)callMethod:(id)sender
{
    UIButton *btn=(UIButton*)sender;
    NSString *phn=[[btn titleLabel] text];
    UIWebView*callWebview =[[UIWebView alloc] init];
    
    NSString *telStr=[@"tel:" stringByAppendingString:phn];
    NSURL *telURL =[NSURL URLWithString:telStr];// 貌似tel:// 或者 tel: 都行
    
    [callWebview loadRequest:[NSURLRequest requestWithURL:telURL]];
    
    //记得添加到view上
    
    [self.view addSubview:callWebview];
}
-(void)showCourtWayImgs
{
    AHPreviewController *preview=[[AHPreviewController alloc]init];
    preview.previewImgArray=_fairwayImgArray;
    preview.previewTitle=@"球道详情";
    [self.navigationController pushViewController:preview animated:YES];
}
-(IBAction)showNearbyImgs:(id)sender
{
    int tag=(int)[sender tag];
    AHPreviewController *preview=[[AHPreviewController alloc]init];
    preview.previewImgArray=[[_nearbyInfoArray objectAtIndex:tag] objectForKey:@"imgs"];
    preview.previewTitle=@"店铺图片";
    [self.navigationController pushViewController:preview animated:YES];
}
- (void)displayNotification {
    if (self.notify.isAnimating) return;
    [self.view addSubview:self.notify];
    //    [[[UIApplication sharedApplication]keyWindow] addSubview:self.notify];
    [self.notify presentWithDuration:1.5f speed:1.0f inView:self.view completion:^{
        [self.notify removeFromSuperview];
    }];
}

- (BDKNotifyHUD *)notify {
    if (_notify != nil) return _notify;
    _notify = [BDKNotifyHUD notifyHUDWithImage:[UIImage imageNamed:@""] text:_notificationText];
    _notify.center = CGPointMake(73, self.view.center.y - 20);
    return _notify;
}

@end
