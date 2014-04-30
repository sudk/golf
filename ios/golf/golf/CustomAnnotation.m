//
//  LocationObject.m
//  MapDemo
//
//  Created by mahh on 14-3-11.
//
//

#import "CustomAnnotation.h"

@implementation CustomAnnotation
@synthesize coordinate,_latitude,_longitude,_titleString,_subTitleString;

- (id) initWithTitle:(NSString *)atitle latitue:(float)alatitude longitude:(float)alongitude
{
    if(self=[super init])
    {
        self._titleString = atitle;
        self._latitude = alatitude;
        self._longitude = alongitude;
    }
    return self;
}

- (CLLocationCoordinate2D)coordinate;
{
    CLLocationCoordinate2D currentCoordinate;
    currentCoordinate.latitude = self._latitude ;
    currentCoordinate.longitude = self._longitude;
    return currentCoordinate;
}

// required if you set the MKPinAnnotationView's "canShowCallout" property to YES
- (NSString *)title
{
    return self._titleString;
}
// optional
- (NSString *)subtitle
{
    return _subTitleString;
}


@end
