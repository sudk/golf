//
//  EvaluateCell.m
//  golf
//
//  Created by mahh on 14-4-15.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "EvaluateCell.h"

@implementation EvaluateCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        _titleLabel=[[UILabel alloc]initWithFrame:CGRectMake(15, 2, SCREEN_WIDTH-30, 25)];
        _titleLabel.backgroundColor=[UIColor clearColor];
        [self.contentView addSubview:_titleLabel];
        
        _timeLabel=[[UILabel alloc]initWithFrame:CGRectMake(SCREEN_WIDTH-150, _titleLabel.frame.origin.y+_titleLabel.frame.size.height, 150, 25)];
        _timeLabel.backgroundColor=[UIColor clearColor];
        [self.contentView addSubview:_timeLabel];
        
        _descLabel=[[UILabel alloc]initWithFrame:CGRectMake(10, _timeLabel.frame.origin.y+_timeLabel.frame.size.height, SCREEN_WIDTH, 35)];
        _descLabel.backgroundColor=[UIColor clearColor];
        [self.contentView addSubview:_descLabel];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
