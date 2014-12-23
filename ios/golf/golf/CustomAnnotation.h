//
//  LocationObject.h
//  MapDemo
//
//  Created by mahh on 14-3-11.
//
//

#import <Foundation/Foundation.h>
#import <MapKit/MKAnnotation.h>

@interface CustomAnnotation : NSObject<MKAnnotation>
{
    CLLocationCoordinate2D coordinate;
    NSString *_titleString; //title值
    NSString *_subTitleString;
    float _latitude; // 经度值
    float _longitude; //纬度值
}

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property float _latitude; // 经度值
@property float _longitude; //纬度值
@property (nonatomic, copy) NSString *_titleString; //title值
@property (nonatomic, copy) NSString *_subTitleString;

- (id) initWithTitle:(NSString *)atitle latitue:(float)alatitude longitude:(float)alongitude;
@end
