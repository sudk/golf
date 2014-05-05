package com.jason.golf;

import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GAccountActivity extends ActionBarActivity {

	public static final String FRAGMENT = "FRAGMENT";
	public static final int LOGIN = 0;
	public static final int REGISTER = 1;
	
	FragmentManager fm ;
	
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.court_info);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
		int mark = getIntent().getIntExtra(FRAGMENT, LOGIN);
		switch (mark) {
		case LOGIN:

			Fragment loginFragment = GAccountLoginFragment.Instance();
			
			transaction.replace(R.id.container, loginFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case REGISTER:
			
			Fragment registerFragment = GAccountRegisterFragment.Instance();

			transaction.replace(R.id.container, registerFragment);
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
