package com.jason.golf;

import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class GAccountRegisterFragment extends Fragment implements OnClickListener {
	
	public static GAccountRegisterFragment Instance(){
		GAccountRegisterFragment fragment = new GAccountRegisterFragment();
		
		return fragment;
	}
	
	private EditText mPhone, mPassword, mRecommandor;
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
		mPassword = (EditText) v.findViewById(R.id.password_edit);
		mRecommandor = (EditText) v.findViewById(R.id.recommander_edit);
		
		mRegisterSubmit = (Button) v.findViewById(R.id.register_submit);
		mRegisterSubmit.setOnClickListener(this);
		
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		ActionBar bar = activity.getSupportActionBar();
		bar.setTitle(R.string.register_title);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.register_submit:
			
			
			
			break;
		}
	}
	
	

}
