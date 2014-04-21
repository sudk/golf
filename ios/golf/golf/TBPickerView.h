//
//  NMPickerView.h
//  NeiMengPay
//
//  Created by qi zuowei on 11/17/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol TBPickerViewDelegate;

@interface TBPickerView : UIView<UIPickerViewDelegate,UIPickerViewDataSource>{
    id <TBPickerViewDelegate> delegate;
    NSInteger _selectRow;
    
    NSArray *_array;
}
@property (nonatomic,assign)id <TBPickerViewDelegate> delegate;
- (void)selectRow:(NSInteger)row animated:(BOOL)animated;
-(void)setDataArray:(NSArray *)array;
- (id)initWithFrame:(CGRect)frame AndDataArray:(NSArray *)aArray;
@end


@protocol TBPickerViewDelegate <NSObject>

- (void)pickerView:(TBPickerView *)pickerView didSelectRow:(NSInteger)row;
- (void)confirmpickerView:(TBPickerView *)pickerView didSelectRow:(NSInteger)row;
@end