//
//  CityViewController.m
//  China36Plans
//
//  Created by apple on 10-10-21.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "CityViewController.h"
#import "SearchCourtHomeViewController.h"


@implementation CityViewController

@synthesize titles;
@synthesize detailView;
@synthesize dic;



#pragma mark -
#pragma mark View lifecycle


- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"城市";
    UIButton *cityBackBtn=[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 44, 44)];
    [cityBackBtn setTitle:@"返回" forState:UIControlStateNormal];
    [cityBackBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [cityBackBtn addTarget:self action:@selector(cityBackMethod) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *cityBackItem=[[UIBarButtonItem alloc]initWithCustomView:cityBackBtn];
    self.navigationItem.leftBarButtonItem=cityBackItem;
	UIColor *color = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:@"bgd.jpg"]];
	self.tableView.backgroundColor = color;
	[color release];
	
    _sqlUtils=[[SQLUtilsObject alloc]init];
    NSArray *tmparray=[_sqlUtils query_province_tab];
    NSMutableArray *tmpMutableArray=[NSMutableArray array];
    for (int i=0; i<[tmparray count]; i++) {
        [tmpMutableArray addObject:[[tmparray objectAtIndex:i] objectForKey:@"province"]];
    }
    NSArray *array=tmpMutableArray;
	self.titles = array;
    NSLog(@"self.titles====%@",self.titles);
	

    flag = (BOOL*)malloc([array count]*sizeof(BOOL*));
	memset(flag, NO, sizeof(flag)*[self.titles count]);
	
	NSString *path = [[NSBundle mainBundle] pathForResource:@"source" ofType:@"plist"];
	NSDictionary *dict = [[NSDictionary alloc] initWithContentsOfFile:path]; 
	self.dic = dict;
	[dict release];

}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:YES];

    
}

- (void)viewWillDisappear:(BOOL)animated {

}


#pragma mark -
#pragma mark Table view data source

// Customize the number of sections in the table view.
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return [self.titles count];
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self numberOfRowsInSection:section];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
	int section = [indexPath section];
	int row = [indexPath row];
	
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
	}
	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
//	if(row>1)
//	   cell.imageView.image = [UIImage imageNamed:@"unOnline.png"];
//	else 
//		cell.imageView.image = [UIImage imageNamed:@"online.png"];
	NSDictionary *source = [self.dic objectForKey:[NSString stringWithFormat:@"%i",section+1]];
//	NSArray *array = [source objectForKey:@"titles"];
//    NSArray *array = [_sqlUtils query_city_tab:[self.titles objectAtIndex:section]];
    NSArray *tmpprovinceIdArray=[_sqlUtils query_province_tab];
    NSMutableArray *provinceIdArray=[NSMutableArray array];
    for (int i=0; i<[tmpprovinceIdArray count]; i++) {
        [provinceIdArray addObject:[[tmpprovinceIdArray objectAtIndex:i] objectForKey:@"provinceId"]];
    }
    NSArray *array=[_sqlUtils query_city_tab:[provinceIdArray objectAtIndex:section]];
	
	UILabel *label = (UILabel *)[cell viewWithTag:7];
	if(label){
		label.text = [[array objectAtIndex:row]objectForKey:@"city"];
	}
	else {
		UILabel *secondLabel = [[UILabel alloc]initWithFrame:CGRectMake(20, 0,260, 30)];
		secondLabel.font = [UIFont systemFontOfSize:14];
		secondLabel.textColor = [UIColor colorWithRed:0.6 green:0.6 blue:0.3 alpha:1];
		secondLabel.text = [[array objectAtIndex:row] objectForKey:@"city"];
		secondLabel.tag = 7;
		[cell addSubview:secondLabel];
		[secondLabel release];
		
	}
	
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	return 40;
	
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
	
	return 50;
	
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
	UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
	button.tag = section;
	NSDictionary *source = [self.dic objectForKey:[NSString stringWithFormat:@"%i",section+1]];
	NSArray *array = [source objectForKey:@"titles"];
	[button setBackgroundImage:[UIImage imageNamed:@"bgd1.png"] forState:UIControlStateNormal];
	[button addTarget:self action:@selector(headerClicked:) forControlEvents:UIControlEventTouchUpInside];
	UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 20, 20)];
	
	if(flag[section])
		image.image = [UIImage imageNamed:@"normal.png"];
	else 
		image.image = [UIImage imageNamed:@"pressed.png"];
	[UIView beginAnimations:@"animatecomeout" context:NULL];
	[UIView setAnimationDuration:.25f];
	if(!flag[section])
		image.transform=CGAffineTransformMakeRotation(-1.58);
	else
		image.transform=CGAffineTransformMakeRotation(1.58);
	[UIView commitAnimations];
	[button addSubview:image];
	[image release];
	
	CGFloat size = 16;
    

	CGFloat width=50;
	UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(40, 5, width+200, 30)];
	label.backgroundColor = [UIColor clearColor];
	label.textColor = [UIColor whiteColor];
	label.font = [UIFont boldSystemFontOfSize:size];
    label.text = [NSString stringWithFormat:@"%@",[self.titles objectAtIndex:section]];
	[button addSubview:label];
	[label release];
	
	return button;
	
}

#pragma mark -
#pragma mark Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	
	NSArray *tmpprovinceIdArray=[_sqlUtils query_province_tab];
    NSMutableArray *provinceIdArray=[NSMutableArray array];
    for (int i=0; i<[tmpprovinceIdArray count]; i++) {
        [provinceIdArray addObject:[[tmpprovinceIdArray objectAtIndex:i] objectForKey:@"provinceId"]];
    }
    NSArray *array=[_sqlUtils query_city_tab:[provinceIdArray objectAtIndex:[indexPath section]]];
    NSString *city=[[array objectAtIndex:[indexPath row]]objectForKey:@"city"];
    NSLog(@"city====%@",city);
    NSDictionary *tmpDic=[NSDictionary dictionaryWithObject:city forKey:[[_changeDic allKeys]objectAtIndex:0]];
    NSArray *viewControls=[self.navigationController viewControllers];
    SearchCourtHomeViewController *searchVc=(SearchCourtHomeViewController *)[viewControls objectAtIndex:[viewControls count]-2];
    searchVc.isFromUpdate=YES;
    searchVc.changeDic=tmpDic;
    searchVc.hidesBottomBarWhenPushed=NO;
    [self.navigationController popToViewController:searchVc animated:YES];
}

-(void)headerClicked:(id)sender
{
	int sectionIndex = (int)[sender tag];
	flag[sectionIndex] = !flag[sectionIndex];
    [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:sectionIndex] withRowAnimation:UITableViewRowAnimationNone];
}
- (int)numberOfRowsInSection:(NSInteger)section
{
	
	if (flag[section]) {
        NSArray *tmpprovinceIdArray=[_sqlUtils query_province_tab];
        NSMutableArray *provinceIdArray=[NSMutableArray array];
        for (int i=0; i<[tmpprovinceIdArray count]; i++) {
            [provinceIdArray addObject:[[tmpprovinceIdArray objectAtIndex:i] objectForKey:@"provinceId"]];
        }
        NSArray *cityArray=[_sqlUtils query_city_tab:[provinceIdArray objectAtIndex:section]];
        NSLog(@"cityArray======%@",cityArray);
        return [cityArray count];
	}
	else {
		return 0;
	}
}

- (void)dealloc {
	[titles release];
	[detailView release];
	[dic release]; 
	free(flag);
    [super dealloc];
}
-(void)cityBackMethod
{
    NSArray *viewControls=[self.navigationController viewControllers];
    SearchCourtHomeViewController *searchVc=(SearchCourtHomeViewController *)[viewControls objectAtIndex:[viewControls count]-2];
    searchVc.isFromUpdate=NO;
    searchVc.hidesBottomBarWhenPushed=NO;
    [self.navigationController popToViewController:searchVc animated:YES];
}

@end

