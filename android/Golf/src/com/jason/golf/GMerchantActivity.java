package com.jason.golf;

import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GMerchantActivity extends ActionBarActivity {

	public static final String FRAGMENT_MARK = "FRAGMENT";
	public static final int MERCHANT_LIST = 0;
	public static final int MERCHANT_INFO = 1;
	
	FragmentManager fm ;
	
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.merchant);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
		Bundle prams = getIntent().getExtras();
		int mark = prams.getInt(FRAGMENT_MARK, MERCHANT_LIST);
		
		switch (mark) {
		case MERCHANT_LIST:

			Fragment listFragment = GMerchantListFragment.Instance();
			listFragment.setArguments(prams);
			
			transaction.replace(R.id.container, listFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case MERCHANT_INFO:
			
			Fragment infoFragment = GMerchantInfoFragment.Instance();
			infoFragment.setArguments(prams);

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
