//
//  AccountCell.m
//  golf
//
//  Created by mahh on 14-4-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "AccountCell.h"

@implementation AccountCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        _titleLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, 200, 36)];
        _titleLabel.backgroundColor=[UIColor clearColor];
        self.accessoryType=UITableViewCellAccessoryDisclosureIndicator;
        [self addSubview:_titleLabel];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
