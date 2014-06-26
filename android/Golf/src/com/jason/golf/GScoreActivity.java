package com.jason.golf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GScoreActivity extends ActionBarActivity {

	public static final String FRAGMENT_MARK = "fragment_mark";
	public static final int FRAGMENT_MARK_LIST = 0x1001;

	private FragmentManager fm;

	private FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_score);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.sale);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();

		int mark = getIntent().getIntExtra(FRAGMENT_MARK, FRAGMENT_MARK_LIST);
		switch (mark) {
		case FRAGMENT_MARK_LIST:

			Fragment listFragment = GScoreListFragment.Instance();

			transaction.replace(R.id.container, listFragment);
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
