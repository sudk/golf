//
//  KeyWordViewController.m
//  golf
//
//  Created by mahh on 14-4-7.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "KeyWordViewController.h"
#import "CourtInfo.h"
#import "SearchCoreManager.h"
#import "Utils.h"
#import "SearchCourtHomeViewController.h"

@interface KeyWordViewController ()

@end

@implementation KeyWordViewController

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
    UIButton *keywordBackBtn=[self leftButton:@"" andHighlightedName:@""];
    [keywordBackBtn addTarget:self action:@selector(keywordBackMethod) forControlEvents:UIControlEventTouchUpInside];
//    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    _searchByName=[NSMutableArray array];
    _searchByPhone=[NSMutableArray array];
    self.resultDic=[NSMutableDictionary dictionary];
    self.courtNameArray=@[@"济南云虎国际高尔夫",@"沈阳高尔夫",@"海南高尔夫",@"济南"];
    for (int i=0; i<[_courtNameArray count]; i++) {
        CourtInfo *courtInfo = [[CourtInfo alloc] init];
        courtInfo.localID = [NSNumber numberWithInt:i];
        courtInfo.courtName =[_courtNameArray  objectAtIndex:i];//球场名称
        
        NSMutableArray *phoneArray = [[NSMutableArray alloc] init];
        [phoneArray addObject:[NSString stringWithFormat:@"%lld",99999999999]];
        courtInfo.phoneArray = phoneArray;
        
        [_resultDic setObject:courtInfo forKey:courtInfo.localID];
        //添加到搜索库
        [[SearchCoreManager share] AddContact:courtInfo.localID name:courtInfo.courtName phone:courtInfo.phoneArray];
    }
    self.searchBar=[[UISearchBar alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 44)];
    _searchBar.delegate=self;
    _searchBar.placeholder=@"球会名、地址等关键字";
    [_searchBar becomeFirstResponder];
    self.navigationItem.titleView=_searchBar;
    _courtNameTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64-44) style:UITableViewStylePlain];
    _courtNameTable.delegate=self;
    _courtNameTable.dataSource=self;
    _courtNameTable.rowHeight=40;
    _courtNameTable.backgroundColor=[UIColor clearColor];
    [self.view addSubview:_courtNameTable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (NSInteger)tableView:(UITableView *)_tableView numberOfRowsInSection:(NSInteger)section
{
    NSLog(@"_searchByName====%@",_searchByName);
    if ([self.searchBar.text length] <= 0) {
        return 0;
    } else {
        return [_searchByName count] + [self.searchByPhone count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *indentifier = @"Cell";
    UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:indentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:indentifier];
		cell.selectionStyle=UITableViewCellSelectionStyleBlue;
	}
    
    if ([self.searchBar.text length] <= 0) {
        return cell;
    }
    NSNumber *localID = nil;
    NSMutableString *matchString = [NSMutableString string];
    NSMutableArray *matchPos = [NSMutableArray array];
    if (indexPath.row < [_searchByName count]) {
        localID = [_searchByName objectAtIndex:indexPath.row];
        
        //姓名匹配 获取对应匹配的拼音串 及高亮位置
        if ([self.searchBar.text length]) {
            [[SearchCoreManager share] GetPinYin:localID pinYin:matchString matchPos:matchPos];
        }
    } else {
        localID = [self.searchByPhone objectAtIndex:indexPath.row-[_searchByName count]];
        NSMutableArray *matchPhones = [NSMutableArray array];
        
        //号码匹配 获取对应匹配的号码串 及高亮位置
        if ([self.searchBar.text length]) {
            [[SearchCoreManager share] GetPhoneNum:localID phone:matchPhones matchPos:matchPos];
            [matchString appendString:[matchPhones objectAtIndex:0]];
        }
    }
    CourtInfo *courtInfo = [_resultDic objectForKey:localID];
    NSLog(@"courtInfo==cellfor==%@",courtInfo);
    cell.textLabel.text = courtInfo.courtName;
    tableView.separatorStyle=UITableViewCellSeparatorStyleSingleLine;
    return cell;
}
- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath row]%2!=0) {
//        cell.backgroundColor=[Utils  colorWithHexString:@"#f0f1f3"];
        cell.backgroundColor=[UIColor whiteColor];
    }
}
- (void)tableView:(UITableView *)_tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.searchBar resignFirstResponder];
    NSNumber *localID = nil;
    NSMutableString *matchString = [NSMutableString string];
    NSMutableArray *matchPos = [NSMutableArray array];
    if (indexPath.row < [_searchByName count]) {
        localID = [_searchByName objectAtIndex:indexPath.row];
        
        //姓名匹配 获取对应匹配的拼音串 及高亮位置
        if ([self.searchBar.text length]) {
            [[SearchCoreManager share] GetPinYin:localID pinYin:matchString matchPos:matchPos];
        }
    } else {
        localID = [self.searchByPhone objectAtIndex:indexPath.row-[_searchByName count]];
        NSMutableArray *matchPhones = [NSMutableArray array];
        
        //号码匹配 获取对应匹配的号码串 及高亮位置
        if ([self.searchBar.text length]) {
            [[SearchCoreManager share] GetPhoneNum:localID phone:matchPhones matchPos:matchPos];
            [matchString appendString:[matchPhones objectAtIndex:0]];
        }
    }
    CourtInfo *courtInfo = [_resultDic objectForKey:localID];
    NSLog(@"courtInfo==didselect==%@",courtInfo);
    NSString *courtName=courtInfo.courtName;
    NSDictionary *tmpDic=[NSDictionary dictionaryWithObject:courtName forKey:[[_changeDic allKeys] objectAtIndex:0]];
    NSArray *viewcontrols=[self.navigationController viewControllers];
    SearchCourtHomeViewController *courtVc=(SearchCourtHomeViewController*)[viewcontrols objectAtIndex:[viewcontrols count]-2];
    courtVc.isFromUpdate=YES;
    courtVc.changeDic=tmpDic;
    courtVc.hidesBottomBarWhenPushed=NO;
    [self.navigationController popToViewController:courtVc animated:YES];
    NSLog(@"courtName=====%@",courtName);
    }
- (void)searchBar:(UISearchBar *)_searchBar textDidChange:(NSString *)searchText
{
    [[SearchCoreManager share] Search:searchText searchArray:nil nameMatch:self.searchByName phoneMatch:self.searchByPhone];
    [_courtNameTable reloadData];
}

-(void)keywordBackMethod
{
    NSArray *viewControls=[self.navigationController viewControllers];
    SearchCourtHomeViewController *searchVc=(SearchCourtHomeViewController *)[viewControls objectAtIndex:[viewControls count]-2];
    searchVc.isFromUpdate=YES;
    searchVc.hidesBottomBarWhenPushed=NO;
    [self.navigationController popToViewController:searchVc animated:YES];
}

@end
