//
//  NMTextField.m
//  NeiMengPay
//
//  Created by zkjc on 11-11-12.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import "NMTextField.h"
#import "Style.h"
#import "FSSysConfig.h"


@implementation NMTextField
@synthesize textField=_textField;
@synthesize textLabel=_textLabel;
- (id)initWithFrame:(CGRect)frame WithText:(NSString *)labeltext WithRightView :(UIView *)rightView withFlg:(NSString *)upOrDownFlg
{
    self = [super initWithFrame:frame];
    if (self) {
//        CGSize textSize=[labeltext sizeWithFont:Style_LabelTextFont constrainedToSize:frame.size lineBreakMode:1];
        CGSize textSize=[labeltext sizeWithFont:Style_LabelTextFont constrainedToSize:frame.size lineBreakMode:NSLineBreakByCharWrapping];
        _textLabel=[[UILabel alloc] initWithFrame:CGRectMake(10, (frame.size.height-30)/2, textSize.width, 30)];
        _textField=[[UITextField alloc] initWithFrame:CGRectMake(20+textSize.width, (frame.size.height-30)/2, frame.size.width-textSize.width-20-rightView.frame.size.width, 30)];
        _textField.clearButtonMode=UITextFieldViewModeWhileEditing;
        _textField.font=Style_ContentTextFont;
        _textField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
        _textLabel.text=labeltext;
//        _textLabel.lineBreakMode=UILineBreakModeMiddleTruncation;
        _textLabel.lineBreakMode=NSLineBreakByTruncatingMiddle;
        _textLabel.backgroundColor=[UIColor clearColor];
        
        _textLabel.font=Style_LabelTextFont;
        [self addSubview:_textLabel];
        [self addSubview:_textField];
        if (rightView) {
            rightView.frame=CGRectMake(20+textSize.width+_textField.frame.size.width, rightView.frame.origin.y, rightView.frame.size.width, rightView.frame.size.height);
            [self addSubview:rightView];
        }
//        self.backgroundColor=[UIColor colorWithRed:0.88 green:0.88 blue:0.88 alpha:1];
        if ([upOrDownFlg isEqualToString:@"lone1"]) {
            self.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"login_input1_normal.png"]];
        }
        else if([upOrDownFlg isEqualToString:@"lone2"])
        {
//            self.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"login_input2_normal.png"]];
            self.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"login_input1_normal.png"]];
        }
        else
        {
            self.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"button_alone_normal.png"]];
        }
        
//        [self.layer setCornerRadius:8];
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)dealloc
{
    [_textField release];
    [_textLabel release];
    [super dealloc];
}

@end
