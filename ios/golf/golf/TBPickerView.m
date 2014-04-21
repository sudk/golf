//
//  NMPickerView.m
//  NeiMengPay
//
//  Created by qi zuowei on 11/17/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "TBPickerView.h"

@implementation TBPickerView
@synthesize delegate;
- (id)initWithFrame:(CGRect)frame AndDataArray:(NSMutableArray *)aArray
{
    self = [super initWithFrame:frame];
    if (self) {
        _array=[aArray retain];
        self.frame=CGRectMake(self.frame.origin.x, self.frame.origin.y, 320, 216+44);
        UIPickerView * pickerView=[[UIPickerView alloc] initWithFrame:CGRectMake(0, 44, 320, 216)];
        pickerView.delegate=self;
        pickerView.dataSource=self;
        pickerView.showsSelectionIndicator=YES;
        pickerView.tag=2;
        [self addSubview:pickerView];
        [pickerView release];
        
        
        UIToolbar *toolBar=[[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 320, 44)];
        toolBar.barStyle=UIBarStyleBlack;
        
        UIBarButtonItem *buttonbar=[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
        UIBarButtonItem *buttonbar2=[[UIBarButtonItem alloc]initWithTitle:@"确定" style:UIBarButtonItemStyleDone target:self action:@selector(confirm)];
        NSArray * array=[[NSArray alloc] initWithObjects:buttonbar,buttonbar2, nil];
        toolBar.items=array;
        _selectRow=0;
        [array release];
        [buttonbar release];
        [buttonbar2 release];
        [self addSubview:toolBar];
        [toolBar release];
        // Initialization code
    }
    return self;
}
-(void)confirm{
    [delegate confirmpickerView:self didSelectRow:_selectRow];
}
- (void)selectRow:(NSInteger)row animated:(BOOL)animated{
    UIPickerView *pickerView=(UIPickerView *)[self viewWithTag:2];
    [pickerView selectRow:row inComponent:0 animated:animated];
}
-(void)setDataArray:(NSArray *)array{
    if(_array){
        [_array release];
    }
    _array=[array retain];
    [(UIPickerView *)[self viewWithTag:2] reloadAllComponents];
}
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return [_array count];
}
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [_array objectAtIndex:row];
}
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    _selectRow=row;
    [delegate pickerView:self didSelectRow:row];
}
- (void)dealloc {
    [_array release];
    [super dealloc];
}
@end
