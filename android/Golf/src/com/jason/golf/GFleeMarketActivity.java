package com.jason.golf;

import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GFleeMarketActivity extends ActionBarActivity {

	public static final String FRAGMENT_MARK = "fragment_mark";
	public static final int FRAGMENT_MARK_FLEE_LIST = 0x1001;
	public static final int FRAGMENT_MARK_FLEE_NEW = 0x1002;
	public static final int FRAGMENT_MARK_FLEE_INFO = 0x1003;
	public static final int FRAGMENT_MARK_FLEE_MYLIST = 0x1004;
	
	private FragmentManager fm;

	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flee_market);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.sale);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();

		Bundle params = getIntent().getExtras();
		int mark = params.getInt(FRAGMENT_MARK, FRAGMENT_MARK_FLEE_LIST);

		switch (mark) {
		case FRAGMENT_MARK_FLEE_LIST:

			Fragment fleeListFragment = GFleeMarketListFragment.Instance();
			fleeListFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, fleeListFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case FRAGMENT_MARK_FLEE_NEW:

			Fragment newFragment = GFleeMarketNewFragment.Instance();
			newFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case FRAGMENT_MARK_FLEE_INFO:
			
			Fragment infoFragment = GFleeMarketInfoFragment.Instance();
			infoFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, infoFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			break;
		case FRAGMENT_MARK_FLEE_MYLIST:
			Fragment myListFragment = GFleeMarketMyGoodsFragment.Instance();
			myListFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, myListFragment);
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

		if (fm.getBackStackEntryCount() == 1)
			finish();
		else
			super.onBackPressed();

	}

}
