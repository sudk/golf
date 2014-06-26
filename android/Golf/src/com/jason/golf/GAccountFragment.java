package com.jason.golf;

import com.jason.golf.classes.GAccount;
import com.jason.golf.R;

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

	private TextView mBalance, mPoints, mPhone, mFlea;
	
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
		mPoints = (TextView) v.findViewById(R.id.account_points);
		mPhone = (TextView) v.findViewById(R.id.account_phone);

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
			mPhone.setText(_account.getPhone());
			mBalance.setText(String.format("余额：%.2f", (float) _account.getBalance() / 100));
			mPoints.setText(String.format("积分：%d", _account.getPoint()));
			
			if(_account.isVip()){
				mVip.setImageResource(R.drawable.ic_vip_red);
			}else{
				mVip.setImageResource(R.drawable.ic_vip_gray);
			}

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
					startActivity(new Intent(getActivity(), GChangePwdActivity.class));
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
				
				if (_account.isLogin()) {
					
					if(!_account.isVip()){
						startActivity(new Intent(getActivity(), GVipActivity.class));
					}
					
				} else {
					startLoginActivity();
				}
				
				break;
			}
		} finally{
			_account = null;
		}

	}

	private void startLoginActivity() {
		Intent itLogin = new Intent(getActivity(), GAccountActivity.class);
		itLogin.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.LOGIN);
		startActivity(itLogin);
	}

}
