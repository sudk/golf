//
//  ContactPeople.m
//  SearchCoreTest
//
//  Created by Apple on 28/01/13.
//  Copyright (c) 2013 kewenya. All rights reserved.
//

#import "ContactPeople.h"

@implementation ContactPeople
@synthesize localID = _localID;
@synthesize name = _name;
@synthesize phoneArray = _phoneArray;
- (void)dealloc
{
    [_localID release];
    [_name release];
    [_phoneArray release];
    [super dealloc];
}



+(BOOL)searchResult:(NSString *)contactName searchText:(NSString *)searchT{
	NSComparisonResult result = [contactName compare:searchT options:NSCaseInsensitiveSearch
											   range:NSMakeRange(0, searchT.length)];
	if (result == NSOrderedSame)
		return YES;
	else
		return NO;
}
@end
