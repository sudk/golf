package com.jason.golf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GAdver;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GRechargeActivity extends ActionBarActivity implements
		OnCheckedChangeListener, OnClickListener, TextWatcher {

	private RadioGroup mListAmount;

	private EditText mCustomAmount;
	
	private int _amount;
	
	private Button mRecharge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recharge);

		mListAmount = (RadioGroup) findViewById(R.id.list_amount);

		mListAmount.setOnCheckedChangeListener(this);
		
		mCustomAmount = (EditText) findViewById(R.id.customer_amount);
		
		mCustomAmount.addTextChangedListener(this);
		
		_amount = 0;
		
		mRecharge = (Button) findViewById(R.id.recharge);
		
		mRecharge.setOnClickListener(this);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.account_recharge);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

	}

	
	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		finish();
		return super.onSupportNavigateUp();
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int id = group.getCheckedRadioButtonId();
		System.out.println(String.format("checkedId is %d, getCheckedRadioButtonId()'s return is %d", checkedId, id));

		switch (checkedId) {
		case R.id.amount_1000:
			_amount = 1000;
			mCustomAmount.setVisibility(View.GONE);
			break;
		case R.id.amount_2000:
			_amount = 2000;
			mCustomAmount.setVisibility(View.GONE);
			break;
		case R.id.amount_5000:
			_amount = 5000;
			mCustomAmount.setVisibility(View.GONE);
			break;
		case R.id.amount_10000:
			_amount = 10000;
			mCustomAmount.setVisibility(View.GONE);
			break;
		case R.id.amount_custom:
			
			String str = mCustomAmount.getText().toString();
			
			if(!TextUtils.isEmpty(str)){
				_amount = Integer.parseInt(str);
			}
			
			mCustomAmount.setVisibility(View.VISIBLE);
			break;
		default:

			break;
		}

	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.recharge:
			recharge(_amount);			
			break;
		}
	}

	private void recharge(int amount) {
		// TODO Auto-generated method stub
		
		GolfAppliaction app = (GolfAppliaction) getApplication();
		GAccount acc = app.getAccount();
		
		JSONObject params = new JSONObject();
		Date date = new Date(); 
		String pat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pat);
		
		try {
			params.put("cmd"           , "order/create");
			params.put("type"          ,"3" );
			params.put("relation_id"   , acc.getId());
			params.put("relation_name" , acc.getId());
			params.put("tee_time"      , sdf.format(date));
			params.put("count"         , String.format("%d", 1));
			params.put("unitprice"     , String.format("%d", amount * 100));
			params.put("amount"        , String.format("%d", amount * 100));
			params.put("pay_type"      , "2");
			params.put("contact"       , acc.getId());
			params.put("phone"         , acc.getPhone());
			params.put("agent_id"      , "1");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Runnable r = new HttpRequest(this, params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				try {
					JSONArray data = new JSONArray(res);
					
					
					
					
					
					
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				
				WarnDialog dialogTimeout = new WarnDialog(GRechargeActivity.this);
				dialogTimeout
						.setTitle(R.string.account_recharge)
						.setMessage(res)
						.setPositiveBtn(R.string.confirm,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								});
				dialogTimeout.show(getSupportFragmentManager(), "CreateOrderFaild");
			}
			
		});
		
		GThreadExecutor.execute(r);
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String str = s.toString();
		
		if(TextUtils.isEmpty(str)){
			_amount = 0;
		}else{
			_amount = Integer.parseInt(str);
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

}
