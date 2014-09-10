package com.jason.golf;

import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GTripsActivity extends ActionBarActivity {
	
	public static final String FRAGMENT_MARK = "fragment_mark";
	public static final int FRAGMENT_MARK_TRIP_LIST = 0x1001;
	public static final int FRAGMENT_MARK_TRIP_INFO = 0x1002;
	

	private FragmentManager fm;

	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trips);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.trip);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();

		Bundle params = getIntent().getExtras();
		int mark = params.getInt(FRAGMENT_MARK, FRAGMENT_MARK_TRIP_LIST);
		
		switch(mark){
		case FRAGMENT_MARK_TRIP_LIST:
			
			Fragment tripListFragment = GTripListFragment.Instance();
			tripListFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, tripListFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			break;
		case FRAGMENT_MARK_TRIP_INFO:
			
			Fragment tripInfoFragment = GTripDetailsFragment.Instance();
			tripInfoFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, tripInfoFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			break;
		}

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
	

}
