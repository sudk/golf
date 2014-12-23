package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.AppInfoContent;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GAccountFragment extends Fragment implements OnClickListener {

	private Button mLogout;

	private LinearLayout mButtons, mUserInfo;

	private TextView mBalance, mPhone, mFlea;
	
	private ImageView mVip;

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
		View v = inflater.inflate(R.layout.fragment_account, container, false);
		v.findViewById(R.id.account_login).setOnClickListener(this);
		v.findViewById(R.id.account_register).setOnClickListener(this);
		v.findViewById(R.id.account_change_pwd).setOnClickListener(this);
		v.findViewById(R.id.account_recharge).setOnClickListener(this);
		v.findViewById(R.id.account_my_flea).setOnClickListener(this);
		v.findViewById(R.id.account_trans_details).setOnClickListener(this);
		v.findViewById(R.id.account_my_score).setOnClickListener(this);

		mButtons = (LinearLayout) v.findViewById(R.id.account_buttons);
		mUserInfo = (LinearLayout) v.findViewById(R.id.account_info);

		mBalance = (TextView) v.findViewById(R.id.account_balance);
		mBalance.setOnClickListener(this);
		
		mPhone = (TextView) v.findViewById(R.id.account_phone);
		mPhone.setOnClickListener(this);

		mLogout = (Button) v.findViewById(R.id.account_logout);
		mLogout.setOnClickListener(this);
		
		mVip = (ImageView) v.findViewById(R.id.account_vip);
		mVip.setOnClickListener(this);

		v.findViewById(R.id.account_order).setOnClickListener(this);

		ActionBarActivity activity = (ActionBarActivity) getActivity();
		ActionBar bar = activity.getSupportActionBar();
		bar.setTitle(R.string.account_manager);

		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		GAccount _account = app.getAccount();

		System.out.println(_account.toString());

		if (_account.isLogin()) {
			mLogout.setEnabled(true);
			mButtons.setVisibility(ViewGroup.GONE);
			mUserInfo.setVisibility(ViewGroup.VISIBLE);
			
			initializeAccountInfo(_account);
			// mPhone.setText(_account.getPhone());
			// mBalance.setText(String.format("￥ %.2f", (float)
			// _account.getBalance() / 100));
			//
			// if(_account.isVip()){
			// mVip.setImageResource(R.drawable.ic_vip_gold);
			// }else{
			// mVip.setImageResource(R.drawable.ic_vip_gray);
			// }

		} else {
			mLogout.setEnabled(false);
			mButtons.setVisibility(ViewGroup.VISIBLE);
			mUserInfo.setVisibility(ViewGroup.GONE);

		}
		
		_account = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		MenuItemCompat.setShowAsAction(menu.add("Menu 1a"), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
//		MenuItemCompat.setShowAsAction(menu.add("Menu 1b"), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		GAccount _account = app.getAccount();

		try{
		
		switch (v.getId()) {
			case R.id.account_login:
				startLoginActivity();
				break;
			case R.id.account_register:
				Intent itRegister = new Intent(getActivity(), GAccountActivity.class);
				itRegister.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.REGISTER);
				startActivity(itRegister);
				break;
			case R.id.account_logout:
				_account.clear();
				onResume();
				break;
	
			case R.id.account_change_pwd:
				if (_account.isLogin()) {
					Intent changePwd = new Intent(getActivity(), GChangePwdActivity.class);
					changePwd.putExtra("title", "修改密码");
					startActivity(changePwd);
				} else {
					startLoginActivity();
				}
				
				break;
			case R.id.account_recharge:
				if (_account.isLogin()) {
					startActivity(new Intent(getActivity(), GRechargeActivity.class));
				} else {
					startLoginActivity();
				}
				break;
			case R.id.account_order:
	
				if (_account.isLogin()) {
	
					Intent itOrder = new Intent(getActivity(), GOrderActivity.class);
					Bundle params = new Bundle();
					params.putInt(GOrderActivity.FRAGMENT_MARK,
							GOrderActivity.FRAGMENT_MARK_LIST_ORDER);
					itOrder.putExtras(params);
					startActivity(itOrder);
	
				} else {
					startLoginActivity();
				}
	
				break;
			case R.id.account_my_flea:
				if (_account.isLogin()) {
					Intent fleaIntent = new Intent(getActivity(), GFleeMarketActivity.class);
					Bundle params = new Bundle();
					params.putInt(GFleeMarketActivity.FRAGMENT_MARK,	GFleeMarketActivity.FRAGMENT_MARK_FLEE_MYLIST);
					fleaIntent.putExtras(params);
					startActivity(fleaIntent);
				} else {
					startLoginActivity();
				}
				break;
			case R.id.account_trans_details:
				if (_account.isLogin()) {
					Intent transIntent = new Intent(getActivity(), GTransrecordActivity.class);
					Bundle params = new Bundle();
					params.putInt(GTransrecordActivity.FRAGMENT, GTransrecordActivity.FRAGMENT_TRANSRECORD_LIST);
					transIntent.putExtras(params);
					startActivity(transIntent);
				} else {
					startLoginActivity();
				}
				break;
			case R.id.account_my_score:
				
				if (_account.isLogin()) {
					Intent scroIntent = new Intent(getActivity(), GScoreActivity.class);
					Bundle params = new Bundle();
					params.putInt(GScoreActivity.FRAGMENT_MARK, GScoreActivity.FRAGMENT_MARK_LIST);
					scroIntent.putExtras(params);
					startActivity(scroIntent);
				} else {
					startLoginActivity();
				}
				break;
			case R.id.account_vip:
			case R.id.account_phone:
				
				if (_account.isLogin()) {

					startActivity(new Intent(getActivity(), GVipActivity.class));

				} else {
					startLoginActivity();
				}

				break;
			case R.id.account_balance:
				queryUserInfo();
				break;
			}
		} finally{
			_account = null;
		}

	}
	
	private void queryUserInfo(){
		
		JSONObject p = new JSONObject();
		try {
			p.put("cmd",    "user/info");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		Runnable r = new HttpRequest(getActivity(), p, new HttpCallback() {
			
			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				
			}

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				JSONObject data;
				try {
					data = new JSONObject(res);
					GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
					GAccount acc = app.getAccount();
					
					acc.setName(data.getString("user_name"));
					acc.setPhone(data.getString("phone"));
					acc.setVipCardNo(data.getString("card_no"));
					acc.setEamil(data.getString("email"));
					acc.setSex(data.getString("sex"));
					acc.setBalance(data.getString("balance"));
					acc.setPoint(data.getString("point"));
					
					// Vip信息
					acc.setVipExpireDate(data.getString("vip_expire_date"));
					acc.setVipStatus(data.getInt("vip_status"));
					
					initializeAccountInfo(acc);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void finalWork() {
				// TODO Auto-generated method stub
			}

			
		});
		
		GThreadExecutor.execute(r);
		
	}
	
	private void initializeAccountInfo(GAccount acc){
		
		mPhone.setText(acc.getPhone());
		mBalance.setText(String.format("￥ %.2f", (float) acc.getBalance() / 100));
		
		if(acc.isVip()){
			mVip.setImageResource(R.drawable.ic_vip_gold);
		}else{
			mVip.setImageResource(R.drawable.ic_vip_gray);
		}
		
		
	}

	private void startLoginActivity() {
		Intent itLogin = new Intent(getActivity(), GAccountActivity.class);
		itLogin.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.LOGIN);
		startActivity(itLogin);
	}

}
