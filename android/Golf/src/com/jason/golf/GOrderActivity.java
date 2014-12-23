package com.jason.golf;

import com.jason.golf.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GOrderActivity extends ActionBarActivity {
	
	public static final String FRAGMENT_MARK = "fragment_mark";
	
	public static final int FRAGMENT_MARK_GENERATE_ORDER = 0x1001;
	public static final int FRAGMENT_MARK_GENERATE_APPLY = 0x1002;
	public static final int FRAGMENT_MARK_GENERATE_TRIP = 0x1003;
	
	public static final int FRAGMENT_MARK_LIST_ORDER = 0x1004;
	public static final int FRAGMENT_MARK_VIEW_ORDER = 0x1005;
	
	private FragmentManager fm ;
	
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manager);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.order_manager);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
		Bundle params = getIntent().getExtras();
		int mark = params.getInt(FRAGMENT_MARK, FRAGMENT_MARK_LIST_ORDER);
		
		
		switch(mark){
		case FRAGMENT_MARK_GENERATE_ORDER:
			
			Fragment generateFragment = GOrderGenerateFragment.Instance();
			generateFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, generateFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			
			break;
		case FRAGMENT_MARK_LIST_ORDER:
			
			Fragment listFragment = GOrderListFragment.Instance();

			transaction.replace(R.id.container, listFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			
			break;
		case FRAGMENT_MARK_VIEW_ORDER:
			
			Fragment detailFragment = GOrderDetailsFragment.Instance();
			detailFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, detailFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			
			break;
			
		case FRAGMENT_MARK_GENERATE_APPLY:
			
			Fragment applyFragment = GOrderApplyFragment.Instance();
			applyFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, applyFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			break;
		case FRAGMENT_MARK_GENERATE_TRIP:
			
			Fragment tripFragment = GOrderBookingTripFragment.Instance();
			tripFragment.setArguments(new Bundle(params));

			transaction.replace(R.id.container, tripFragment);
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /************************************************* 
         * 
         *  步骤3：处理银联手机支付控件返回的支付结果 
         *  
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        GOrderDetailsFragment f = GOrderDetailsFragment.Instance();
        if(!f.isDetached())
        	f.queryOrder();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
	
}
