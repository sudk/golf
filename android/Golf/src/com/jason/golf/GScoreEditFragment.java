package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.AddAndSubView;
import com.jason.golf.classes.GScore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GScoreEditFragment extends Fragment implements OnClickListener {
	
	public static final String KEY_SCORE_ID = "key_score_id";

	private static final int RequestPickCourt = 1001;

	private TextView mCourtName;

	private CheckBox mIsSecret;

	private EditText mMembers;

	private Button mSave;
	
	private AddAndSubView mAddAndSubView;

	private GScore _Score;
	private String _ScoreId;

	public static GScoreEditFragment Instance() {
		return new GScoreEditFragment();
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
		_ScoreId = getArguments().getString(KEY_SCORE_ID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().getActionBar().setTitle(R.string.score_info);

		View v = inflater.inflate(R.layout.fragment_score_new, null);

		mCourtName = (TextView) v.findViewById(R.id.court_name);
		mCourtName.setOnClickListener(this);

		mSave = (Button) v.findViewById(R.id.submitNewScore);
		mSave.setOnClickListener(this);

		mIsSecret = (CheckBox) v.findViewById(R.id.is_secret);

		mMembers = (EditText) v.findViewById(R.id.team_memeber);
		
		LinearLayout linearLayout1 = (LinearLayout) v.findViewById(R.id.holes);
		mAddAndSubView = new AddAndSubView(getActivity(), 1);
		mAddAndSubView.setMaxNumber(18);
		mAddAndSubView.setButtonBgResource(R.drawable.button_background, R.drawable.button_background);
		linearLayout1.addView(mAddAndSubView);
		
		queryScoreInfo(_ScoreId);
		
		return v;
	}
	
	private void checkButtonEnable(){
		if(mCourtName.length() == 0){
			mSave.setEnabled(false);
		}else{
			mSave.setEnabled(true);
		}
	}

	private void queryScoreInfo(String sid) {
		// TODO Auto-generated method stub
		
		JSONObject params = new JSONObject();
		
		try {
			params.put("cmd", "score/info");
			params.put("id" , sid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params,	new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				try {
					JSONObject data = new JSONObject(res);
					
					_Score = new GScore();
					_Score.initialize(data);
					
					mIsSecret.setChecked("1".equals(_Score.getIsShow()));
					mMembers.setText(_Score.getTeamMembers());
					mCourtName.setText(_Score.getCourtName());
					int h = Integer.parseInt(_Score.getHoles());
					mAddAndSubView.setNum(h);
					
					checkButtonEnable();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					super.sucessData(res);
				}
			}
			
		});
		
		GThreadExecutor.execute(r);
		
	}

	private void updateScore() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		
		String members = mMembers.getText().toString();
		
		try {

			params.put("cmd", "score/update");
			params.put("id" , _ScoreId);
			params.put("court_id" , _Score.getCourtId());
			params.put("holes"    , "18");
			params.put("fee_time" , _Score.getFeeTime());
			params.put("is_show"  , mIsSecret.isChecked() ? "1" : "0");

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
			updateScore();
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
				_Score.setCourtId(data.getStringExtra("CourtId"));
				checkButtonEnable();
			}
			break;
			
		}

	}

}
