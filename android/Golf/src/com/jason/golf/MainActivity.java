package com.jason.golf;

import com.jason.golf.classes.GAccount;
import com.jason.golf.dialog.WarnDialog;
import com.jsaon.golf.R;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
        
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria=new Criteria(); 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    //精度要求：ACCURACY_FINE(高)ACCURACY_COARSE(低)
        criteria.setAltitudeRequired(false);             // 不要求海拔信息
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH); //方位信息的精度要求：ACCURACY_HIGH(高)ACCURACY_LOW(低)
        criteria.setBearingRequired(false);              // 不要求方位信息
        criteria.setCostAllowed(false);                  // 是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);// 对电量的要求  (HIGH、MEDIUM)
        
        String provider = locMan.getBestProvider(criteria,true);
        
        if(!TextUtils.isEmpty(provider) ){
        
        	System.out.println("Provider is "+provider);
            Location loc = locMan.getLastKnownLocation(provider);
            
            System.out.println(String.format("纬度:%f, 精度：%f", loc.getLatitude(), loc.getLongitude()));
            
            if(loc != null){
            	GolfAppliaction app = (GolfAppliaction) getApplication();
            	GAccount acc = app.getAccount();
            	acc.setLoc(loc);;
            }
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
		app.getAccount().clear();
		super.onDestroy();
	}
	
	

}
