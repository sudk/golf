package com.jason.golf.dialog;

import com.jason.golf.GOrderActivity;
import com.jason.golf.GOrderGenerateFragment;
import com.jason.golf.adapters.AgentsAdapter;
import com.jason.golf.classes.GCompetition;
import com.jason.golf.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CompetitionApplyingDialog extends Dialog implements android.view.View.OnClickListener {
	
	private GCompetition _competition;
	private String _teeTime;
	private Button mOK;
	
	private TextView mName, mAgent, mDate, mService, mCompetitionDesc, mFee;

//	public OrderDialog(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
	
	public CompetitionApplyingDialog(Context context, GCompetition competition){
		super(context,R.style.OrderDialog);
		_competition = competition;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_competition_applying);
		
		mName = (TextView) findViewById(R.id.dialog_competition_name);
		mName.setText(_competition.getName());
		mAgent = (TextView) findViewById(R.id.dialog_competition_agent);
		mAgent.setText(_competition.getAgentName());
		mDate = (TextView) findViewById(R.id.dialog_competition_date);
		mDate.setText(_competition.getStartDate());
		
		mService = (TextView) findViewById(R.id.dialog_competition_service);
		mService.setText(String.format("价格包含：%s \n不包含：%s", _competition.getFeeInclude(), _competition.getFeeNotInclude()));
		
		mFee = (TextView) findViewById(R.id.dialog_competition_unitprice);
		mFee.setText(String.format("￥%.2f %s", (float)_competition.getFee()/100, GCompetition.GetFeeTypeDes(_competition.getFeeType())));
		
		mCompetitionDesc = (TextView) findViewById(R.id.dialog_competition_desc);
		mCompetitionDesc.setText(_competition.getDesc());
		
		mOK = (Button) findViewById(R.id.dialog_competiton_ok);
		mOK.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.dialog_refund_ok:
			
			Intent it = new Intent(getContext(), GOrderActivity.class);
			Bundle params = new Bundle();
//			params.putInt(GOrderActivity.FRAGMENT_MARK, GOrderActivity.FRAGMENT_MARK_GENERATE_ORDER);
//			params.putString(GOrderGenerateFragment.key_agent_id, _competition.getId());
//			params.putString(GOrderGenerateFragment.key_pay_type, _competition.getPayType());
//			params.putString(GOrderGenerateFragment.key_relation_id, _competition.getCourtid());
//			params.putString(GOrderGenerateFragment.key_relation_name, _competition.getCourtname());
//			params.putString(GOrderGenerateFragment.key_tee_time, _competition.getTeeTime());
//			params.putString(GOrderGenerateFragment.key_type, );
//			params.putInt(GOrderGenerateFragment.key_unitprice, _competition.getPrice());
			
			it.putExtras(params);
			getContext().startActivity(it);
			
			this.dismiss();
			
			break;
		}
	}
	
}
