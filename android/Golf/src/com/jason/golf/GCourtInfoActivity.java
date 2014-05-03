package com.jason.golf;

import com.jason.golf.classes.GCourt;
import com.jsaon.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

public class GCourtInfoActivity extends ActionBarActivity {
	
	public static final String KEY_COURT_ID = "Key_Court_Id"; 
	public static final String KEY_DATE = "Key_Date";
	public static final String KEY_TIME = "Key_Time";
	
	FrameLayout mComtainer;

	FragmentManager fm ;
	
	private GCourt _court;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_court_info);
		mComtainer = (FrameLayout) findViewById(R.id.container);
		
		Intent it = getIntent();
		Bundle params = it.getExtras();
		String courtId = params.getString(KEY_COURT_ID);
		String date = params.getString(KEY_DATE);
		String time = params.getString(KEY_TIME);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.court_info);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		
		fm = getSupportFragmentManager();
		
		Fragment newFragment = GCourtInfoBriefFragment.Instance(courtId, date, time);
		FragmentTransaction transaction = fm.beginTransaction();

		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);

		transaction.commit();
		
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		onBackPressed();
		return super.onSupportNavigateUp();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if(fm.getBackStackEntryCount() == 1)
			finish();
		else
			super.onBackPressed();
		
	}

	public void setCourt(GCourt court) {
		// TODO Auto-generated method stub
		_court = court;
	}
	
	public GCourt getCourt(){
		return _court;
	}
	
}
