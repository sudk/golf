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
    self.courtInfoArray=[NSMutableArray array];
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
    for (int i=0; i<8; i++) {
        [_courtInfoArray addObject:@""];
    }
    [self courtDetailInfo];
    UIView *phnView=[[UIView alloc]initWithFrame:CGRectMake(0, _courtInfoTable.frame.origin.y+_courtInfoTable.frame.size.height+10, SCREEN_WIDTH, 40)];
    phnView.backgroundColor=[UIColor whiteColor];
    UILabel *phnLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 40)];
    phnLabel.backgroundColor=[UIColor clearColor];
    phnLabel.text=@"球场电话";
    
    self.phnBtn=[[UIButton alloc]initWithFrame:CGRectMake(phnLabel.frame.origin.x+phnLabel.frame.size.width, 0, 180, 40)];
    [_phnBtn setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
    [_phnBtn setTitle:@"" forState:UIControlStateNormal];
    [_phnBtn addTarget:self action:@selector(callMethod:) forControlEvents:UIControlEventTouchUpInside];
    [phnView addSubview:phnLabel];
    [phnView addSubview:_phnBtn];
    [_courtInfoScroll addSubview:phnView];
    
    
    
    UIView *courtWayView=[[UIView alloc]initWithFrame:CGRectMake(0, phnView.frame.origin.y+phnView.frame.size.height+10, SCREEN_WIDTH, 70)];
    courtWayView.backgroundColor=[UIColor whiteColor];
    UILabel *courtWaylabel=[[UILabel alloc]initWithFrame:CGRectMake(10, 15, 80, 40)];
    courtWaylabel.text=@"球道详情";
    courtWaylabel.backgroundColor=[UIColor clearColor];
    [courtWayView addSubview:courtWaylabel];
    [_courtInfoScroll addSubview:courtWayView];
    
    UIButton *courtWayBtn1=[[UIButton alloc]initWithFrame:CGRectMake(95, 5, 60, 60)];
    [courtWayBtn1 setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] forState:UIControlStateNormal];
    [courtWayBtn1 addTarget:self action:@selector(showCourtWayImgs) forControlEvents:UIControlEventTouchUpInside];
    [courtWayView addSubview:courtWayBtn1];
    
    UIButton *courtWayBtn2=[[UIButton alloc]initWithFrame:CGRectMake(160, 5, 60, 60)];
    [courtWayBtn2 setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] forState:UIControlStateNormal];
    [courtWayBtn2 addTarget:self action:@selector(showCourtWayImgs) forControlEvents:UIControlEventTouchUpInside];
    [courtWayView addSubview:courtWayBtn2];
    
    UIButton *courtWayBtn3=[[UIButton alloc]initWithFrame:CGRectMake(225, 5, 60, 60)];
    [courtWayBtn3 setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] forState:UIControlStateNormal];
    [courtWayBtn3 addTarget:self action:@selector(showCourtWayImgs) forControlEvents:UIControlEventTouchUpInside];
    [courtWayView addSubview:courtWayBtn3];
    [_courtInfoScroll addSubview:courtWayView];
    
    UILabel *desclabel=[[UILabel alloc]initWithFrame:CGRectMake(10, courtWayView.frame.origin.y+courtWayView.frame.size.height+10, 100, 40)];
    desclabel.text=@"球场简介";
    desclabel.backgroundColor=[UIColor clearColor];
    [_courtInfoScroll addSubview:desclabel];
    
    UIView *descView=[[UIView alloc]initWithFrame:CGRectMake(0, desclabel.frame.origin.y+desclabel.frame.size.height, SCREEN_WIDTH, 100)];
    descView.backgroundColor=[UIColor whiteColor];
    self.descInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, descView.frame.size.width, descView.frame.size.height)];
    _descInfo.backgroundColor=[UIColor clearColor];
    _descInfo.text=@"澳门凯撒高尔夫也叫东方澳门高尔夫球场，事东方拉斯维加斯的球场，澳门凯撒高尔夫也叫东方澳门高尔夫球场，事东方拉斯维加斯的球场，澳门凯撒高尔夫也叫东方澳门高尔夫球场，事东方拉斯维加斯的球场，澳门凯撒高尔夫也叫东方澳门高尔夫球场，事东方拉斯维加斯的球场。";
    _descInfo.numberOfLines=7;
    _descInfo.lineBreakMode=NSLineBreakByWordWrapping;
    _descInfo.font=[UIFont systemFontOfSize:14];
    [descView addSubview:_descInfo];
    [_courtInfoScroll addSubview:descView];
    
    UILabel *facilitylabel=[[UILabel alloc]initWithFrame:CGRectMake(10, descView.frame.origin.y+descView.frame.size.height+10, 100, 40)];
    facilitylabel.text=@"球场设施";
    facilitylabel.backgroundColor=[UIColor clearColor];
    [_courtInfoScroll addSubview:facilitylabel];
    
    UIView *facilityView=[[UIView alloc]initWithFrame:CGRectMake(0, facilitylabel.frame.origin.y+facilitylabel.frame.size.height, SCREEN_WIDTH, 40)];
    facilityView.backgroundColor=[UIColor whiteColor];
    self.facilityInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, facilityView.frame.size.width, facilityView.frame.size.height)];
    _facilityInfo.backgroundColor=[UIColor clearColor];
    _facilityInfo.text=@"";
    _facilityInfo.numberOfLines=2;
    _facilityInfo.lineBreakMode=NSLineBreakByWordWrapping;
    _facilityInfo.font=[UIFont systemFontOfSize:14];
    [facilityView addSubview:_facilityInfo];
    [_courtInfoScroll addSubview:facilityView];
    
    UILabel *evaluationlabel=[[UILabel alloc]initWithFrame:CGRectMake(10, facilityView.frame.origin.y+facilityView.frame.size.height+10, 100, 40)];
    evaluationlabel.text=@"球场评价";
    evaluationlabel.backgroundColor=[UIColor clearColor];
//    [_courtInfoScroll addSubview:evaluationlabel];
    
    UIView *evaluationView=[[UIView alloc]initWithFrame:CGRectMake(0, evaluationlabel.frame.origin.y+evaluationlabel.frame.size.height, SCREEN_WIDTH, 40)];
    evaluationView.backgroundColor=[UIColor whiteColor];
    UILabel *evaluationInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, evaluationView.frame.size.width, evaluationView.frame.size.height)];
    evaluationInfo.backgroundColor=[UIColor clearColor];
    evaluationInfo.text=@"环境优雅，环境优雅，环境优雅，环境优雅，环境优雅，环境优雅";
    evaluationInfo.numberOfLines=2;
    evaluationInfo.lineBreakMode=NSLineBreakByWordWrapping;
    evaluationInfo.font=[UIFont systemFontOfSize:14];
    [evaluationView addSubview:evaluationInfo];
//    [_courtInfoScroll addSubview:evaluationView];
    
    UILabel *nearbylabel=[[UILabel alloc]initWithFrame:CGRectMake(10, facilityView.frame.origin.y+facilityView.frame.size.height+10, 100, 40)];
    nearbylabel.text=@"附近的店";
    nearbylabel.backgroundColor=[UIColor clearColor];
    [_courtInfoScroll addSubview:nearbylabel];
    
    UIView *nearbyView=[[UIView alloc]initWithFrame:CGRectMake(0, evaluationlabel.frame.origin.y+evaluationlabel.frame.size.height, SCREEN_WIDTH, 210)];
    nearbyView.backgroundColor=[UIColor whiteColor];
    
    UIImageView *nearbyImgv1=[[UIImageView alloc]initWithFrame:CGRectMake(10, 2, 60, 60)];
    [nearbyImgv1 setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] placeholderImage:[UIImage imageNamed:@"defaultImg"]];
    [nearbyView addSubview:nearbyImgv1];
    UILabel *nearbyInfo1=[[UILabel alloc]initWithFrame:CGRectMake(75, 2, 225, 30)];
    nearbyInfo1.backgroundColor=[UIColor clearColor];
    nearbyInfo1.text=@"ktv";
    nearbyInfo1.font=[UIFont systemFontOfSize:14];
    [nearbyView addSubview:nearbyInfo1];
    
    UIImageView *nearbyImgv2=[[UIImageView alloc]initWithFrame:CGRectMake(10, 64, 60, 60)];
    [nearbyImgv2 setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] placeholderImage:[UIImage imageNamed:@"defaultImg"]];
    [nearbyView addSubview:nearbyImgv2];
    UILabel *nearbyInfo2=[[UILabel alloc]initWithFrame:CGRectMake(75, 64, 225, 30)];
    nearbyInfo2.backgroundColor=[UIColor clearColor];
    nearbyInfo2.text=@"ktv";
    nearbyInfo2.font=[UIFont systemFontOfSize:14];
    [nearbyView addSubview:nearbyInfo2];
    
    UIImageView *nearbyImgv3=[[UIImageView alloc]initWithFrame:CGRectMake(10, 126, 60, 60)];
    [nearbyImgv3 setImageWithURL:[NSURL URLWithString:@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"] placeholderImage:[UIImage imageNamed:@"defaultImg"]];
    [nearbyView addSubview:nearbyImgv3];
    UILabel *nearbyInfo3=[[UILabel alloc]initWithFrame:CGRectMake(75, 126, 225, 30)];
    nearbyInfo3.backgroundColor=[UIColor clearColor];
    nearbyInfo3.text=@"ktv";
    nearbyInfo3.font=[UIFont systemFontOfSize:14];
    [nearbyView addSubview:nearbyInfo3];
    [_courtInfoScroll addSubview:nearbyView];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    NSLog(@"球场详情====%@",[notification object]);
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
    _preview.previewImgArray=[dataArray  objectForKey:@"fairway_imgs"];
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
    _preview=[[AHPreviewController alloc]init];
    _preview.previewImgArray=@[@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg",@"http://www.5aihuan.com/images/n_tjian/pp_11.jpg"];
    _preview.previewTitle=@"球道详情";
    [self.navigationController pushViewController:_preview animated:YES];
}
@end
