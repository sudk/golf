package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.AppInfoContent;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jason.golf.dialog.ProgressDialog;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.content.DialogInterface;
import android.content.Intent;
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
		
		mPhone.setText(AppInfoContent.getLoginid(getActivity()));
		mPassword.setText(AppInfoContent.getPassword(getActivity()));
		
		v.findViewById(R.id.getBackPwd).setOnClickListener(this);
		
		mLogin = (Button) v.findViewById(R.id.login_button);
		mLogin.setOnClickListener(this);
		mRegister = (Button) v.findViewById(R.id.register_button);
		mRegister.setOnClickListener(this);
		
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		ActionBar bar = activity.getSupportActionBar();
		bar.setTitle(R.string.login_title);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.login_button:
			
			String id = mPhone.getText().toString();
			String pwd = mPassword.getText().toString();
			
//			final ProgressDialog dialog = new ProgressDialog(getActivity());
//			dialog.show(getFragmentManager(), "Login");
			
			JSONObject p = new JSONObject();
			try {
				p.put("phone",  id);
				p.put("passwd", pwd);
				p.put("cmd",    "user/login");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			Runnable r = new HttpRequest(getActivity(), p, new HttpCallback() {
				
				@Override
				public void faildData(int code, String res) {
					// TODO Auto-generated method stub
					super.faildData(code, res);
					
					WarnDialog dialog = new WarnDialog(getActivity());
					dialog.setTitle(R.string.login_title).setMessage(res)
					.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
					dialog.show(getFragmentManager(), "LoginFaild");
				}

				@Override
				public void sucessData(String res) {
					// TODO Auto-generated method stub
//					 {"data":{"user_id":"6","user_name":"","phone":"18653157591",
//					"card_no":"","email":"","sex":null,"remark":null,"record_time":
//					"2014-04-24 17:21:17","status":"0","balance":null,"point":null},
//					"desc":"\u6210\u529f\uff01","status":0}

					super.sucessData(res);
					
					JSONObject data;
					try {
						data = new JSONObject(res);
						GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
						GAccount acc = app.getAccount();
						acc.initilization(data.getString("user_id"));
						acc.setName(data.getString("user_name"));
						acc.setPhone(data.getString("phone"));
						acc.setVipCardNo(data.getString("card_no"));
						acc.setEamil(data.getString("email"));
						acc.setSex(data.getString("sex"));
						acc.setRemark(data.getString("remark"));
						acc.setBalance(data.getString("balance"));
						acc.setPoint(data.getString("point"));
						
						// Vip信息
						acc.setVipExpireDate(data.getString("vip_expire_date"));
						acc.setVipStatus(data.getInt("vip_status"));
						
						getActivity().finish();
						
						AppInfoContent.saveAccount(getActivity(), mPhone.getText().toString(), mPassword.getText().toString());
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void finalWork() {
					// TODO Auto-generated method stub
//					dialog.dismiss();
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
			
		case R.id.getBackPwd:
			
			Intent changePwd = new Intent(getActivity(), GChangePwdActivity.class);
			changePwd.putExtra("title", "找回密码");
			startActivity(changePwd);
			
			break;
		}
	}

}
