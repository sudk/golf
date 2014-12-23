package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.AppInfoContent;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GChangePwdActivity extends ActionBarActivity implements
		OnClickListener, Callback, TextWatcher {

	private static final int MESSAGE_TIMING = 1001;
	
	private static final int TIME_COUNT = 60; // 倒计时60秒
	
	private boolean _timeCounting = false;

	private Button mGetSmsCode, mSubmitChgPwd;
	private Handler mHandler;

	private EditText mPhone, mPassword, mSmsCode;

	private int _timeCounter;// Sms 计时器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		_timeCounter = TIME_COUNT;
		
		String title = getIntent().getStringExtra("title");

		setContentView(R.layout.activity_change_pwd);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(title);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		mPhone = (EditText) findViewById(R.id.phone_edit);
		mPhone.addTextChangedListener(this);
		mPassword = (EditText) findViewById(R.id.new_pwd_edit);
		mPassword.addTextChangedListener(this);
		mSmsCode = (EditText) findViewById(R.id.code_edit);
		mSmsCode.addTextChangedListener(this);

		mGetSmsCode = (Button) findViewById(R.id.get_sms_code);
		mSubmitChgPwd = (Button) findViewById(R.id.submit_chgpwd);
		
		checkButtongsEnable();

		mGetSmsCode.setOnClickListener(this);
		mSubmitChgPwd.setOnClickListener(this);

		mHandler = new Handler(this);

	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.get_sms_code: {
			String phone = mPhone.getText().toString();
			JSONObject params = new JSONObject();

			try {
				params.put("phone", phone);
				params.put("cmd", "user/smstoken");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpRequest r = new HttpRequest(this, params, new HttpCallback() {

				@Override
				public void sucessData(String res) {
					// TODO Auto-generated method stub

					mGetSmsCode.setEnabled(false);
					mHandler.sendEmptyMessage(MESSAGE_TIMING);

					super.sucessData(res);
				}

			});

//			GThreadExecutor.execute(r);
			
			//为给老苏省短信钱，暂时用此代码测试 button 的enable效果，用完删掉或注册掉
			mGetSmsCode.setEnabled(false);
			mHandler.sendEmptyMessage(MESSAGE_TIMING);

			break;
		}
		case R.id.submit_chgpwd: {

			String phone = mPhone.getText().toString();
			String smsCode = mSmsCode.getText().toString();
			String pwd = mPassword.getText().toString();

			JSONObject params = new JSONObject();

			try {
				params.put("phone", phone);
				params.put("smstoken", smsCode);
				params.put("passwd", pwd);
				params.put("cmd", "user/changepwd");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpRequest r = new HttpRequest(this, params, new HttpCallback() {
				
				@Override
				public void finalWork() {
					// TODO Auto-generated method stub
					super.finalWork();
				}

				@Override
				public void faildData(int code, String res) {
					// TODO Auto-generated method stub
					
					WarnDialog dialog = new WarnDialog(GChangePwdActivity.this);
					dialog.setTitle(R.string.account_changepwd).setMessage(res)
					.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
					dialog.show(getSupportFragmentManager(), "ChPwdFaild");
					
					super.faildData(code, res);
				}

				@Override
				public void sucessData(String res) {
					// TODO Auto-generated method stub
					mGetSmsCode.setEnabled(false);
					mHandler.removeMessages(MESSAGE_TIMING);
					
					WarnDialog dialog = new WarnDialog(GChangePwdActivity.this);
					dialog.setTitle(R.string.account_changepwd).setMessage(R.string.account_has_chang_pwd)
					.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							AppInfoContent.saveAccount(GChangePwdActivity.this, mPhone.getText().toString(), "");
							finish();
						}
					});
					dialog.show(getSupportFragmentManager(), "ChPwdSuccess");
					
					super.sucessData(res);
					
				}

			});

			GThreadExecutor.execute(r);

			break;
		}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub

		switch (msg.what) {
		case MESSAGE_TIMING:

			_timeCounter--;
			_timeCounting = true;

			if (_timeCounter == 0) {
				_timeCounter = TIME_COUNT;
				mHandler.removeMessages(MESSAGE_TIMING);
				mGetSmsCode.setText(R.string.sms_code);
				_timeCounting = false;
				checkButtongsEnable();
//				mGetSmsCode.setEnabled(true);
			} else {
				mGetSmsCode.setText(String.format("(%d)", _timeCounter));
				mHandler.sendEmptyMessageDelayed(MESSAGE_TIMING, 1000);
			}

			break;
		}

		return false;
	}
	
	private void checkButtongsEnable(){
		
		if (!_timeCounting) {
			if (mPhone.length() < 11)
				mGetSmsCode.setEnabled(false);
			else
				mGetSmsCode.setEnabled(true);
		}
		
		if(mPhone.length() < 11 || mPassword.length() < 6 || mSmsCode.length() != 4){
			mSubmitChgPwd.setEnabled(false);
		}else{
			mSubmitChgPwd.setEnabled(true);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		checkButtongsEnable();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

}
