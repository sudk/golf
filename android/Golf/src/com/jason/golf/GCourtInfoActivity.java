package com.jason.golf;

import com.jsaon.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

public class GCourtInfoActivity extends ActionBarActivity {
	
	public static final String KEY_COURT_ID = "Key_Court_Id"; 
	public static final String KEY_DATE = "Key_Date";
	public static final String KEY_TIME = "Key_Time";
	
	FrameLayout mComtainer;

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
		
		Fragment newFragment = GCourtInfoBriefFragment.Instance(courtId, date, time);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
		
	}
	
}
