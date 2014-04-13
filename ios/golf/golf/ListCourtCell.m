//
//  ListCourtCell.m
//  golf
//
//  Created by mahh on 14-4-10.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "ListCourtCell.h"

@implementation ListCourtCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        _courtImgv=[[UIImageView alloc]initWithFrame:CGRectMake(8, 2, 60, 60)];
        [self.contentView addSubview:_courtImgv];
        _courtNameLabel=[[UILabel alloc]initWithFrame:CGRectMake(72, 2, 180, 30)];
        _courtNameLabel.backgroundColor=[UIColor clearColor];
        [self.contentView addSubview:_courtNameLabel];
        _courtDistanceLabel=[[UILabel alloc]initWithFrame:CGRectMake(72, 33, 180, 30)];
        _courtDistanceLabel.backgroundColor=[UIColor clearColor];
        [self.contentView addSubview:_courtDistanceLabel];
        _courtPriceLabel=[[UILabel alloc]initWithFrame:CGRectMake(252, 17, 60, 30)];
        _courtPriceLabel.backgroundColor=[UIColor clearColor];
        [self.contentView addSubview:_courtPriceLabel];
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
