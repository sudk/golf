//
//  LMapViewController.m
//  golf
//
//  Created by mahh on 14-4-26.
//  Copyright (c) 2014年 mahh. All rights reserved.
//

#import "LMapViewController.h"
#import "CustomAnnotation.h"


@interface LMapViewController ()

@end

@implementation LMapViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    self.title=@"导航";
    self.navigationItem.leftBarButtonItem=[self topBarButtonItem:@"" andHighlightedName:@""];
    [self addMapView];
    
    if(!_forwardGeocoder){
        MJGeocoder *tmpforwardGeocoder = [[MJGeocoder alloc] init];
        self.forwardGeocoder=tmpforwardGeocoder;
        [tmpforwardGeocoder release];
        self.forwardGeocoder.delegate = self;
    }
    [_forwardGeocoder findLocationsWithAddress:self.courtAddress title:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)addMapView
{
    MKMapView *tmpnavcMap = [[MKMapView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-64)];
    self.navcMap=tmpnavcMap;
    [tmpnavcMap release];
	self.navcMap.showsUserLocation = YES;
	self.navcMap.mapType = MKMapTypeStandard;// MKMapTypeSatellite
    self.navcMap.delegate = self;
    [self.view addSubview:self.navcMap];
	_zoomLevel = 0.02;
}
-(void)updateMapView
{
    NSString *subtitleStr=nil;
    BOOL isShowAnnotation=NO;
    CLLocationCoordinate2D coords=CLLocationCoordinate2DMake(_lat, _lon);
    CLLocation *loc=[[CLLocation alloc]initWithLatitude:_lat longitude:_lon];
    subtitleStr=@"监控对象起点位置";
    isShowAnnotation=YES;
    
    [self geocodeMethod:loc andSubTitle: subtitleStr andIsShowAnnotation:isShowAnnotation];
    MKCoordinateRegion region=MKCoordinateRegionMake(coords, MKCoordinateSpanMake(_zoomLevel, _zoomLevel));
    [_navcMap setRegion:region];
    [loc release];
}
-(void)geocodeMethod:(CLLocation *)loc andSubTitle:(NSString *)subtitle andIsShowAnnotation:(BOOL)isShowAnnotation
{
    CLLocationCoordinate2D coords=loc.coordinate;
    //地址解析
    if ([[[UIDevice currentDevice] systemVersion]floatValue] >= 5.0) {
        CLGeocoder *clGeoCoder = [[CLGeocoder alloc] init];
        CLGeocodeCompletionHandler handle = ^(NSArray *placemarks,NSError *error)
        {
            for (CLPlacemark * placeMark in placemarks)
            {
                NSLog(@"place====%@",placeMark);
                NSString *subLocalityStr=placeMark.subLocality;
                if (subLocalityStr==nil) {
                    subLocalityStr=@"";
                }
                NSString *thoroughfareStr=placeMark.thoroughfare;
                if (thoroughfareStr==nil) {
                    thoroughfareStr=@"";
                }
                NSString *subThoroughfareStr=placeMark.subThoroughfare;
                if (subThoroughfareStr==nil) {
                    subThoroughfareStr=@"";
                }
                
                NSString *title=[[[placeMark.locality stringByAppendingString:subLocalityStr] stringByAppendingString:thoroughfareStr] stringByAppendingString:subThoroughfareStr];
                [self addAnnotation:title andLatitue:coords.latitude andLongitude:coords.longitude andSubTitle:subtitle andIsShowAnnotation:isShowAnnotation];
            }
        };
        [clGeoCoder reverseGeocodeLocation:loc completionHandler:handle];
    }
}
//创建注解：
-(void)addAnnotation:(NSString *)title andLatitue:(float)latitue andLongitude:(float)longitude andSubTitle:(NSString *)subtitle andIsShowAnnotation:(BOOL)isShowAnnotation
{
    CustomAnnotation *aLocationObject = [[CustomAnnotation alloc] initWithTitle:title latitue:latitue longitude:longitude];
//    aLocationObject._subTitleString = subtitle;
    [_navcMap addAnnotation:aLocationObject];
    if (isShowAnnotation==YES) {
        [_navcMap selectAnnotation:aLocationObject animated:YES];
    }
    [aLocationObject release];
    
}

- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
    /*
    CLLocationCoordinate2D coords = userLocation.location.coordinate;
    _lat=coords.latitude;
    _lon=coords.longitude;
    
     CLLocation *loc=[[CLLocation alloc]initWithLatitude:coords.latitude longitude:coords.longitude];
     [self geocodeMethod:loc andSubTitle:@"我当前的位置" andIsShowAnnotation:NO];
     MKCoordinateRegion region=MKCoordinateRegionMake(coords, MKCoordinateSpanMake(_zoomLevel, _zoomLevel));
     _navcMap.showsUserLocation = NO;
     [_navcMap setRegion:region];
    */
}
- (void)mapView:(MKMapView *)mapView didAddAnnotationViews:(NSArray *)views
{
    // Initialize each view
    for (MKPinAnnotationView *mkaview in views)
    {
        if ([mkaview class]==[MKPinAnnotationView class]) {
            // 当前位置 的大头针设为紫色，并且没有右边的附属按钮
            if ([mkaview.annotation.subtitle isEqualToString:@"监控对象起点位置"])
            {
                mkaview.pinColor = MKPinAnnotationColorGreen;
                //                mkaview.rightCalloutAccessoryView = nil;
                continue;
            }
            else if ([mkaview.annotation.subtitle isEqualToString:@"监控对象终点位置"])
            {
                mkaview.pinColor = MKPinAnnotationColorPurple;
                continue;
            }
            // 其他位置的大头针设为红色，右边添加附属按钮
            mkaview.pinColor = MKPinAnnotationColorRed;
            mkaview.rightCalloutAccessoryView=nil;
        }
    }
}
#pragma mark -MJGeocoderDelegate
- (void)geocoder:(MJGeocoder *)geocoder didFindLocations:(NSArray *)locations
{
    //hide network indicator
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    self.displayedResults = locations;
    Address *address = [_displayedResults objectAtIndex:0];
    _lat=address.coordinate.latitude;
    _lon=address.coordinate.longitude;
    [self updateMapView];
    
}
- (void)geocoder:(MJGeocoder *)geocoder didFailWithError:(NSError *)error
{
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    
    NSLog(@"GEOCODE ERROR CODE: %d", [error code]);
    
    if([error code] == 1){
        NSLog(@"NO GEOCODE RESULTS");
        
        self.displayedResults = nil;
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"No results found. :(" message:nil delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alertView show];
    }
    
}
@end
