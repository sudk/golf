package com.jason.golf;

import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
		
		checkToFinish();
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		checkToFinish();
	}
	
	private void checkToFinish(){
		/*
		 * 如果activity的fragment栈为空，结束activity。 
		 */
		if(fm.getBackStackEntryCount() == 1)
			finish();
		else
			fm.popBackStack();
		
	}
	
}
