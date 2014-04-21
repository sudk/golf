package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.RunnabLogin;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class GAccountLoginFragment extends Fragment implements OnClickListener {
	
	public static GAccountLoginFragment Instance(){
		GAccountLoginFragment fragment = new GAccountLoginFragment();
		
		return fragment;
	}
	
	private EditText mPhone, mPassword;
	private Button mLogin, mRegister;

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
		
		View v = inflater.inflate(R.layout.fragment_account_login, null);
		
		mPhone = (EditText) v.findViewById(R.id.loginid_edit);
		mPassword = (EditText) v.findViewById(R.id.password_edit);
		
		mLogin = (Button) v.findViewById(R.id.login_button);
		mLogin.setOnClickListener(this);
		mRegister = (Button) v.findViewById(R.id.register_button);
		mRegister.setOnClickListener(this);
		
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		ActionBar bar = activity.getSupportActionBar();
		bar.setTitle(R.string.login_title);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.login_button:
			
			JSONObject p = new JSONObject();
			try {
				p.put("phone",  "13335130151");
				p.put("passwd", "123");
				p.put("cmd",    "user/login");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			Runnable r = new RunnabLogin(getActivity(), p, new HttpCallback() {

				@Override
				public void sucess(String res) {
					// TODO Auto-generated method stub
//					super.sucess(res);
					mPhone.setText(res);
				}

				@Override
				public void timeout(String res) {
					// TODO Auto-generated method stub
					mPhone.setText(res);
				}

				@Override
				public void malformedURL(String res) {
					// TODO Auto-generated method stub
					mPhone.setText(res);
				}

				@Override
				public void ioError(String res) {
					// TODO Auto-generated method stub
					mPhone.setText(res);
				}

				@Override
				public void other(String res) {
					// TODO Auto-generated method stub
					mPhone.setText(res);
				}
				
			});
			
			GThreadExecutor.execute(r);
			break;
		case R.id.register_button:
			
			Fragment registerFragment = GAccountRegisterFragment.Instance();
			FragmentManager fm =  getFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.replace(R.id.container, registerFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			break;
		}
	}

}
