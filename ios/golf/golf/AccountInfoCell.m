//
//  AccountInfoCell.m
//  golf
//
//  Created by mahh on 14-4-20.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "AccountInfoCell.h"

@implementation AccountInfoCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        _titleLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, 90, 36)];
        _titleLabel.backgroundColor=[UIColor clearColor];
        [self addSubview:_titleLabel];
        
        _contentLabel=[[UILabel alloc]initWithFrame:CGRectMake(115, 2, 120, 36)];
        _contentLabel.backgroundColor=[UIColor clearColor];
        [self addSubview:_contentLabel];
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
