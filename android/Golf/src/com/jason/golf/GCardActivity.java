package com.jason.golf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GCardActivity extends ActionBarActivity {
	
	public static final String FRAGMENT_MARK = "fragment_mark";
	public static final int LIST = 0x1001;
	public static final int INFO = 0x1002;
	
	private FragmentManager fm;

	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.card_pack);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();

		Bundle params = getIntent().getExtras();
		int mark = params.getInt(FRAGMENT_MARK, LIST);

		switch (mark) {
		case LIST:

			Fragment cardListFragment = GCardListFragment.Instance();
			cardListFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, cardListFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		
		case INFO:
			
			Fragment infoFragment = GCardInfoFragment.Instance();
			infoFragment.setArguments(new Bundle(params));

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

		if (fm.getBackStackEntryCount() == 1)
			finish();
		else
			super.onBackPressed();

	}

}
