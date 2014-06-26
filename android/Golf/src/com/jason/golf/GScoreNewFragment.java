package com.jason.golf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.AddAndSubView;
import com.jason.golf.dialog.WarnDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GScoreNewFragment extends Fragment implements OnClickListener {

	private static final int RequestPickCourt = 1001;

	private TextView mCourtName;

	private CheckBox mIsSecret;

	private EditText mMembers;

	private Button mSave;

	private String _CourtId;
	
	private AddAndSubView mAddAndSubView;

	public static GScoreNewFragment Instance() {
		return new GScoreNewFragment();
	}

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
		getActivity().getActionBar().setTitle(R.string.new_score);

		View v = inflater.inflate(R.layout.fragment_score_new, null);

		mCourtName = (TextView) v.findViewById(R.id.court_name);
		mCourtName.setOnClickListener(this);

		mSave = (Button) v.findViewById(R.id.submitNewScore);
		mSave.setOnClickListener(this);

		mIsSecret = (CheckBox) v.findViewById(R.id.is_secret);

		mMembers = (EditText) v.findViewById(R.id.team_memeber);
		mMembers.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				checkButtonEnable();
			}
		});
		
		LinearLayout linearLayout1 = (LinearLayout) v.findViewById(R.id.holes);
		mAddAndSubView = new AddAndSubView(getActivity(), 1);
		mAddAndSubView.setMaxNumber(18);
		mAddAndSubView.setButtonBgResource(R.drawable.button_background, R.drawable.button_background);
		linearLayout1.addView(mAddAndSubView);
		
		
		checkButtonEnable();

		return v;
	}
	
	private void checkButtonEnable(){
		if(mCourtName.length() == 0){
			mSave.setEnabled(false);
		}else{
			mSave.setEnabled(true);
		}
	}

	private void newScore() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		
		Date date = new Date();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String dateStr = sdf.format(date);
		
		String members = mMembers.getText().toString();
		
		try {

			params.put("cmd", "score/create");

			params.put("court_id", _CourtId);
			params.put("holes", "18");
			params.put("fee_time", dateStr);
			params.put("is_show", mIsSecret.isChecked() ? "1" : "0");

			if (TextUtils.isEmpty(members)) {
				params.put("team_menbers", "");
			} else {
				params.put("team_menbers", members);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params,
				new HttpCallback() {

					@Override
					public void finalWork() {
						// TODO Auto-generated method stub
						super.finalWork();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						super.sucessData(res);

						((ActionBarActivity)getActivity()).onSupportNavigateUp();
						
						
					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						super.faildData(code, res);
					}

				});

		GThreadExecutor.execute(r);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.court_name:

			Intent pickCourt = new Intent(getActivity(), SearchCourtNameActivity.class);
			startActivityForResult(pickCourt, RequestPickCourt);

			break;
		case R.id.submitNewScore:
			newScore();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RequestPickCourt:

			if (resultCode == Activity.RESULT_OK) {
				String court_name = data.getStringExtra("CourtName");
				mCourtName.setText(court_name);
				_CourtId = data.getStringExtra("CourtId");
				checkButtonEnable();

			}
			break;
			
		}

	}

}
