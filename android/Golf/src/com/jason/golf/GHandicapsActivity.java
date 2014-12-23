package com.jason.golf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GHandicapsActivity extends ActionBarActivity {
	
	
	public static final String FRAGMENT_MARK = "FRAGMENT";
	public static final int PUBLIC = 0;
	public static final int PERSON = 1;
	
	FragmentManager fm ;
	
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.ranking);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
		Bundle prams = getIntent().getExtras();
		int mark = prams.getInt(FRAGMENT_MARK, PUBLIC);
		
		switch (mark) {
		case PUBLIC:

			Fragment listFragment = GHandicapsPublicFragment.Instance();
			listFragment.setArguments(prams);
			
			transaction.replace(R.id.container, listFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case PERSON:
			
			Fragment personalFragment = GHandicapsPersonalFragment.Instance();
			personalFragment.setArguments(prams);

			transaction.replace(R.id.container, personalFragment);
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
