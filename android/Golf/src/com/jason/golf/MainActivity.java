package com.jason.golf;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.jason.golf.classes.GAccount;
import com.jason.golf.dialog.WarnDialog;
import com.jsaon.golf.R;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

	private FragmentTabHost mTabHost;
	private LocationClient mLocClient;
	private MyLocationListener mMyLocationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getLoaction();
		
		ActionBar bar = getSupportActionBar();
		bar.setIcon(R.drawable.actionbar_icon);
		
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_real_content);

        String indicator = getString(R.string.court);
        mTabHost.addTab(mTabHost.newTabSpec("Curse").setIndicator(indicator), GSearchCourtFragment.class, null);
        
//        mTabHost.addTab(mTabHost.newTabSpec("Preferential").setIndicator("Preferential"), GPreferentialFragment.class, null);
        indicator = getString(R.string.find);
        mTabHost.addTab(mTabHost.newTabSpec("Other").setIndicator(indicator),  GOtherFragment.class, null);
        
        indicator = getString(R.string.account);
        mTabHost.addTab(mTabHost.newTabSpec("Account").setIndicator(indicator), GAccountFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        TabWidget tabWidget = mTabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv=(TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setGravity(BIND_AUTO_CREATE);
//            tv.setPadding(10, 10,10, 10);
            tv.setTextSize(15);//设置字体的大小；
//            tv.setTextColor(Color.WHITE);//设置字体的颜色；
                //获取tabs图片；
//            ImageView iv=(ImageView)tabWidget.getChildAt(i).findViewById(android.R.id.icon);
        }
        
	}
	
	private void getLoaction() {
		// TODO Auto-generated method stub
		
		boolean mLocationInit = false;
		
		mLocClient = ((GolfAppliaction)getApplication()).mLocationClient;
		mMyLocationListener = new MyLocationListener();
		mLocClient.registerLocationListener(mMyLocationListener);
		
		try {
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);
			option.setCoorType("bd09ll");
			option.setScanSpan(10000);// 小于1000 为单次请求，需要主动发起请求
			option.setNeedDeviceDirect(false);
			option.setIsNeedAddress(true);
			mLocClient.setLocOption(option);
			mLocationInit = true;
		} catch (Exception e) {
			e.printStackTrace();
			mLocationInit = false;
		}
		
		//开始定位
		if (mLocationInit) {
			mLocClient.start();
		} else {
			Toast.makeText(this, "请设置定位相关的参数", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (mLocClient.isStarted()) {
			//单次请求定位
			mLocClient.requestLocation();
		} 
		
	}

	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		WarnDialog dialog = new WarnDialog(this);
		
		dialog.setTitle(R.string.exit).setMessage(R.string.is_exit)
		.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.super.onBackPressed();
			}
		})
		.setNegativeBtn(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				dialog.dismiss();
			}
		});
		
		dialog.show(getSupportFragmentManager(), "EXIT");
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		GolfAppliaction app = (GolfAppliaction) getApplication();
		
		if(app.mLocationClient.isStarted())
			app.mLocationClient.stop();
		
		app.getAccount().clear();
		super.onDestroy();
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			
			
			if (location == null)
				return;
			
			Log.i("GolfAppliaction", String.format("City code : %s, City : %s", location.getCityCode(), location.getCity()));
			StringBuffer sb = new StringBuffer(256);
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			Log.i("GolfAppliaction", sb.toString());
			
			GAccount acc = ((GolfAppliaction)getApplication()).getAccount();
			acc.setLatitude(location.getLatitude());
			acc.setLongitude(location.getLongitude());
			acc.setCity(location.getCity());
			
			if( ! (Math.abs(acc.getLatitude()-0) < 0.00000001) 
					&& !(Math.abs(acc.getLongitude()-0) < 0.00000001) 
					&& !TextUtils.isEmpty(acc.getCity()))
				
				mLocClient.stop();
			
//			System.out.println(acc.toString());
			
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
