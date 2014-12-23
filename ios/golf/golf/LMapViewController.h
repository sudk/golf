//
//  LMapViewController.h
//  golf
//
//  Created by mahh on 14-4-26.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "BaseViewController.h"
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import "MJGeocoder.h"

@interface LMapViewController : BaseViewController<MKMapViewDelegate,MJGeocoderDelegate>
{
    MKMapView *_navcMap;
}
@property(strong,nonatomic)MKMapView *navcMap;
@property(assign,nonatomic)float zoomLevel;
@property(nonatomic,strong)MJGeocoder *forwardGeocoder;
@property(nonatomic,strong)NSArray *displayedResults;
@property(nonatomic,assign)float lat;
@property(nonatomic,assign)float lon;
@property(nonatomic,strong)NSString *courtAddress;
@end
