package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GCompetition;
import com.jason.golf.dialog.CompetitionApplyingDialog;
import com.jsaon.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GCompetitionDetailFragment extends Fragment implements OnClickListener {

	public static final String KEY_COMPETITION_ID = "key_competition_id";
	
	public static String _competitionId;
	
	private TextView mName, mStartDate, mEndDate, mAgent, mCourt, mPlan, mDesc, mFee, mInclude, mExclude;

	private Button mSignUp;
	
	private GCompetition _competition;
	
	public static GCompetitionDetailFragment Instance() {
		return new GCompetitionDetailFragment();
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
		_competitionId = getArguments().getString(KEY_COMPETITION_ID, "");
		_competition = new GCompetition();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_competition_detail, null);
		
		mName = (TextView) v.findViewById(R.id.competition_name);
		mAgent = (TextView) v.findViewById(R.id.competition_agent);
		mStartDate = (TextView) v.findViewById(R.id.competition_start_date);
		mEndDate = (TextView) v.findViewById(R.id.competition_end_date);
		mCourt = (TextView) v.findViewById(R.id.competition_court);
		mFee = (TextView) v.findViewById(R.id.competition_fee);
		mDesc = (TextView) v.findViewById(R.id.competition_desc);
		mPlan = (TextView) v.findViewById(R.id.competition_plan);
		mInclude = (TextView) v.findViewById(R.id.competition_include);
		mExclude = (TextView) v.findViewById(R.id.competition_not_include);
		
		mSignUp = (Button) v.findViewById(R.id.competition_sign_up);
		mSignUp.setOnClickListener(this);
		
		queryCompetition();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void queryCompetition() {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(_competitionId)) return;
		
		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "competition/info");
			params.put("id", _competitionId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params,new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
//				"id": "guest_one20140424154852", 
//		        "agent_id": "1", 
//		        "court_id": "admin20140409220741", 
//		        "name": "2014年5月21至23日邀请赛", 
//		        "desc": "比赛第一，友谊第二", 
//		        "fee": "100000", 
//		        "start_date": "2014-05-21", 
//		        "end_date": "2014-05-23", 
//		        "plan": "就是比赛，比赛，比赛，比赛，比赛，比赛，比赛，比赛，比赛，比赛", 
//		        "fee_include": "除了没包括的......", 
//		        "fee_not_include": "除了包括的", 
//		        "record_time": "2014-04-24 15:48:52", 
//		        "fee_type": "1", 
//		        "creatorid": "guest_one", 
//		        "court_name": "广州珠海金湾高尔夫", 
//		        "agent_name": "高尔夫代理商001", 
//		        "imgs": [
//		            "http://115.28.77.119/images/picture/20140424/1748077747.png", 
//		            "http://115.28.77.119/images/picture/20140424/1748071998.png"
//		        ]
				
					try {
						JSONObject data = new JSONObject(res);
						
						if(_competition.init(data)){
						
							mName.setText(_competition.getName());
							mAgent.setText(_competition.getAgentName());
							mCourt.setText(_competition.getCourtName());
							mStartDate.setText(_competition.getStartDate());
							mEndDate.setText(_competition.getEndDate());
							
							int fee =  _competition.getFee();
							
							mFee.setText(String.format("￥%.2f %s", (float)fee/100, GCompetition.GetFeeTypeDes(_competition.getFeeType())));
							mInclude.setText(_competition.getFeeInclude());
							mExclude.setText(_competition.getFeeNotInclude());
							mPlan.setText(_competition.getPlan());
							mDesc.setText(_competition.getDesc());
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		switch(v.getId()){
		case R.id.competition_sign_up:
			
			GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
			if(app.getAccount().isLogin()){

//				CompetitionApplyingDialog dialog = new CompetitionApplyingDialog(getActivity(), _competition);
//				dialog.show();
				
				Intent it = new Intent(getActivity(), GOrderActivity.class);
				Bundle params = new Bundle();
				params.putInt(GOrderActivity.FRAGMENT_MARK, GOrderActivity.FRAGMENT_MARK_GENERATE_APPLY);
				params.putString(GOrderApplyFragment.key_agent_id, _competition.getAgentId());
				params.putString(GOrderApplyFragment.key_pay_type, _competition.getFeeType());
				params.putString(GOrderApplyFragment.key_relation_id, _competition.getId());
				params.putString(GOrderApplyFragment.key_relation_name, _competition.getName());
				params.putString(GOrderApplyFragment.key_tee_time, _competition.getStartDate());
				params.putString(GOrderApplyFragment.key_agent_name, _competition.getAgentName());
				params.putString(GOrderApplyFragment.key_type, GOrderApplyFragment.TYPE);
				params.putInt(GOrderApplyFragment.key_unitprice, _competition.getFee());
				
				it.putExtras(params);
				startActivity(it);
				
			}else{
				
				Intent itLogin = new Intent(getActivity(), GAccountActivity.class);
				itLogin.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.LOGIN);
				getActivity().startActivity(itLogin);
				
			}
			
			break;
		}
	}

}
