package com.jason.golf;

import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GCompetitionActivity extends ActionBarActivity {

	public static final String FRAGMENT_MARK = "FRAGMENT_MARK";
	public static final int FRAGMENT_MARK_VIEW_COMPETITION = 0x1001;
	public static final int FRAGMENT_MARK_LIST_COMPETITION = 0x1002;

	private FragmentManager fm;

	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_competition);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.comptition);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();

		Bundle params = getIntent().getExtras();
		int mark = params.getInt(FRAGMENT_MARK, FRAGMENT_MARK_LIST_COMPETITION);

		switch (mark) {

		case FRAGMENT_MARK_LIST_COMPETITION:

			Fragment listFragment = GCompetitionListFragment.Instance();

			transaction.replace(R.id.container, listFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

			break;
		case FRAGMENT_MARK_VIEW_COMPETITION:

//			Fragment detailFragment = GOrderDetailsFragment.Instance();
//			detailFragment.setArguments(new Bundle(params));
//
//			transaction.replace(R.id.container, detailFragment);
//			transaction.addToBackStack(null);
//
//			// Commit the transaction
//			transaction.commit();

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
