package com.jason.golf;

import com.jason.golf.dialog.WarnDialog;
import com.jsaon.golf.R;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_real_content);

        String indicator = getString(R.string.court);
        mTabHost.addTab(mTabHost.newTabSpec("Curse").setIndicator(indicator), GSearchCourtFragment.class, null);
        
//        mTabHost.addTab(mTabHost.newTabSpec("Preferential").setIndicator("Preferential"), GPreferentialFragment.class, null);
        indicator = getString(R.string.find);
        mTabHost.addTab(mTabHost.newTabSpec("Other").setIndicator(indicator),  GOtherFragment.class, null);
        
        indicator = getString(R.string.account);
        mTabHost.addTab(mTabHost.newTabSpec("Account").setIndicator(indicator), GAccountFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		WarnDialog dialog = new WarnDialog(this);
		
		dialog.setTitle(R.string.exit).setMessage(R.string.is_exit)
		.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.super.onBackPressed();
			}
		})
		.setNegativeBtn(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				dialog.dismiss();
			}
		});
		
		dialog.show(getSupportFragmentManager(), "EXIT");
		
	}

}
