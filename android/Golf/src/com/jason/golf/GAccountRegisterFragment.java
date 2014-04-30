package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class GAccountRegisterFragment extends Fragment implements OnClickListener, TextWatcher {
	
	public static GAccountRegisterFragment Instance(){
		GAccountRegisterFragment fragment = new GAccountRegisterFragment();
		
		return fragment;
	}
	
	private EditText mPhone, mPassword, mUsername, mEmail, mVipCard;
	private Button mRegisterSubmit;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.fragment_account_register, null);
		
		mPhone = (EditText) v.findViewById(R.id.loginid_edit);
		mPhone.addTextChangedListener(this);
		mPassword = (EditText) v.findViewById(R.id.password_edit);
		mPassword.addTextChangedListener(this);
		mUsername = (EditText) v.findViewById(R.id.username_edit);
		mUsername.addTextChangedListener(this);
		mEmail = (EditText) v.findViewById(R.id.email_edit);
		mEmail.addTextChangedListener(this);
		mVipCard = (EditText) v.findViewById(R.id.vip_edit);
		mVipCard.addTextChangedListener(this);
		
		mRegisterSubmit = (Button) v.findViewById(R.id.register_submit);
		mRegisterSubmit.setOnClickListener(this);
		
//		ActionBarActivity activity = (ActionBarActivity) getActivity();
//		ActionBar bar = activity.getSupportActionBar();
//		bar.setTitle(R.string.register_title);
//		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
//	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.register_submit:

			String id = mPhone.getText().toString();
			String pwd = mPassword.getText().toString();
			String name = mUsername.getText().toString();
			String email = mEmail.getText().toString();
			String vipCard = mVipCard.getText().toString();
			
			JSONObject p = new JSONObject();
			try {
				p.put("phone",  id);
				p.put("passwd", pwd);
				p.put("user_name", name);
				p.put("card_no", vipCard);
				p.put("email", email);
				p.put("sex", "");
				p.put("cmd",    "user/registe");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			HttpRequest r = new HttpRequest(getActivity(), p,
					new HttpCallback() {

						@Override
						public void sucessData(String res) {
							// TODO Auto-generated method stub
							
							// {"status":0,"desc":"\u6210\u529f","data":{"user_id":"6","user_name":"","phone":"18653157591","card_no":"","email":"","sex":null,"remark":null,"record_time":"2014-04-24 17:21:17","status":null,"balance":null,"point":null}}
							
							try {
								JSONObject data = new JSONObject(res);
								
								GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
								GAccount acc = app.getAccount();
								acc.initilization(data.getString("user_id"));
								acc.setPhone(data.getString("phone"));
								getActivity().finish();
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							super.sucessData(res);
							
//							try {
//								JSONObject resObj = new JSONObject(res);
//								int status = resObj.getInt("status");
//								if(status == 0){
//								
//									JSONObject data = resObj.getJSONObject("data");
//									
//									GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
//									GAccount acc = app.getAccount();
//									acc.initilization(data.getString("user_id"));
//									acc.setPhone(data.getString("phone"));
//									
//									getActivity().finish();
//								}else{
//									
//									
//									
//									
//									
//								}
//								
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							
						}
						
					});

			GThreadExecutor.execute(r);
			
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if( mPhone.length() < 11 || mPassword.length() < 6 )
			mRegisterSubmit.setEnabled(false);
		else
			mRegisterSubmit.setEnabled(true);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	

}
