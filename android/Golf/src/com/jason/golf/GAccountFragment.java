package com.jason.golf;

import com.jason.golf.classes.GAccount;
import com.jsaon.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GAccountFragment extends Fragment implements OnClickListener {

	GAccount _account;
	Button mLogout;

	LinearLayout mButtons, mUserInfo;

	private TextView mBalance, mPoints, mPhone;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_account, container, false);
		v.findViewById(R.id.account_login).setOnClickListener(this);
		v.findViewById(R.id.account_register).setOnClickListener(this);
		v.findViewById(R.id.account_change_pwd).setOnClickListener(this);

		mButtons = (LinearLayout) v.findViewById(R.id.account_buttons);
		mUserInfo = (LinearLayout) v.findViewById(R.id.account_info);

		mBalance = (TextView) v.findViewById(R.id.account_balance);
		mPoints = (TextView) v.findViewById(R.id.account_points);
		mPhone = (TextView) v.findViewById(R.id.account_phone);

		mLogout = (Button) v.findViewById(R.id.account_logout);
		mLogout.setOnClickListener(this);

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
		_account = app.getAccount();

		System.out.println(_account.toString());

		if (_account.isLogin()) {
			mLogout.setEnabled(true);
			mButtons.setVisibility(ViewGroup.GONE);
			mUserInfo.setVisibility(ViewGroup.VISIBLE);

			mPhone.setText(_account.getPhone());
			mBalance.setText(String.format("金额：%d", _account.getBalance()));
			mPoints.setText(String.format("积分：%d", _account.getPoint()));

		} else {
			mLogout.setEnabled(false);
			mButtons.setVisibility(ViewGroup.VISIBLE);
			mUserInfo.setVisibility(ViewGroup.GONE);

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItemCompat.setShowAsAction(menu.add("Menu 1a"),
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		MenuItemCompat.setShowAsAction(menu.add("Menu 1b"),
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		_account = app.getAccount();

		switch (v.getId()) {
		case R.id.account_login:
			startLoginActivity();
			break;
		case R.id.account_register:
			Intent itRegister = new Intent(getActivity(),
					GAccountActivity.class);
			itRegister.putExtra(GAccountActivity.FRAGMENT,
					GAccountActivity.REGISTER);
			startActivity(itRegister);
			break;
		case R.id.account_logout:
			_account.clear();
			onResume();
			break;

		case R.id.account_change_pwd:
			startActivity(new Intent(getActivity(), GChangePwdActivity.class));
			break;
		case R.id.account_order:

			if (_account.isLogin()) {
				
				Intent itOrder = new Intent(getActivity(), GOrderActivity.class);
				Bundle params = new Bundle();
				params.putInt(GOrderActivity.FRAGMENT_MARK, GOrderActivity.FRAGMENT_MARK_LIST_ORDER);
				itOrder.putExtras(params);
				startActivity(itOrder);

			} else {
				startLoginActivity();
			}

			break;
		}

	}

	private void startLoginActivity() {
		Intent itLogin = new Intent(getActivity(), GAccountActivity.class);
		itLogin.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.LOGIN);
		startActivity(itLogin);
	}

}
