package com.jason.golf;

import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GTransrecordActivity extends ActionBarActivity {
	
	public static final String FRAGMENT = "FRAGMENT";

	public static final int FRAGMENT_TRANSRECORD_LIST = 1001;
	public static final int FRAGMENT_TRANSRECORD_INFO = 1002;


	FragmentManager fm ;
	
	FragmentTransaction transaction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_transrecord);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.consumption_details);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
		int mark = getIntent().getIntExtra(FRAGMENT, FRAGMENT_TRANSRECORD_LIST);
		switch (mark) {
		case FRAGMENT_TRANSRECORD_LIST:

			Fragment listFragment = GTransrecordListFragment.Instance();
			
			transaction.replace(R.id.container, listFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case FRAGMENT_TRANSRECORD_INFO:
			
			Fragment infoFragment = GTransrecordInfoFragment.Instance();

			transaction.replace(R.id.container, infoFragment);
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