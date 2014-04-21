//
//  NMTextField.h
//  NeiMengPay
//
//  Created by zkjc on 11-11-12.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface NMTextField : UIView {
    UILabel * _textLabel;
    UITextField * _textField;
}
@property(nonatomic,retain)UILabel * textLabel;
@property(nonatomic,retain)UITextField * textField;
- (id)initWithFrame:(CGRect)frame WithText:(NSString *)labeltext WithRightView :(UIView *)rightView withFlg:(NSString *)upOrDownFlg;

@end
