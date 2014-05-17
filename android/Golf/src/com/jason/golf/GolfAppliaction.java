package com.jason.golf;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.jason.golf.classes.GAccount;

import android.app.Application;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

public class GolfAppliaction extends Application{
	
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	
	private GAccount _account;
	
	public GAccount getAccount(){
		
		if(null == _account)
			_account = new GAccount();
		
		return _account;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this);
	}
	
}
