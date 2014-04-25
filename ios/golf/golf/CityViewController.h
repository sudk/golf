//
//  CityViewController.h
//  China36Plans
//
//  Created by apple on 10-10-21.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SQLUtilsObject.h"


@interface CityViewController : UITableViewController {
	
	NSArray      *titles;
	UITableView  *detailView;
	NSDictionary *dic;
	BOOL *flag;
	BOOL doFresh;
	
}

@property(nonatomic, retain) NSArray *titles;
@property(nonatomic, retain) UITableView  *detailView;
@property(nonatomic, retain) NSDictionary *dic;
@property(nonatomic,strong)SQLUtilsObject *sqlUtils;
@property(nonatomic,strong)NSDictionary *changeDic;

- (int)numberOfRowsInSection:(NSInteger)section;

@end
