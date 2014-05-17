package com.jason.golf.dialog;

import com.jason.golf.GOrderActivity;
import com.jason.golf.GOrderGenerateFragment;
import com.jason.golf.classes.AgentsAdapter;
import com.jason.golf.classes.GAgent;
import com.jsaon.golf.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BookingOrderDialog extends Dialog implements android.view.View.OnClickListener {
	
	private GAgent _agent;
	private String _teeTime;
	private Button mOK;
	
	private TextView mService, mName, mTeetime, mCancelRemark, mAmount;

//	public OrderDialog(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
	
	public BookingOrderDialog(Context context, GAgent agent){
		super(context,R.style.OrderDialog);
		_agent = agent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_order);
		mService = (TextView) findViewById(R.id.dialog_order_service);
		mService.setText(_agent.getService());
		mName = (TextView) findViewById(R.id.dialog_order_agent_name);
		mName.setText(_agent.getName());
		mTeetime = (TextView) findViewById(R.id.dialog_order_tee_time);
		mTeetime.setText(_agent.getTeeTime());
		
		mCancelRemark = (TextView) findViewById(R.id.dialog_order_cancel_remark);
		mCancelRemark.setText(_agent.getCancelRemark());
		
		mAmount = (TextView) findViewById(R.id.dialog_order_amount);
		mAmount.setText(String.format("￥%.2f %s", (float)_agent.getPrice()/100, GAgent.GetPayTypeDes(_agent.getPayType())));
		
		mOK = (Button) findViewById(R.id.dialog_order_ok);
		mOK.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.dialog_order_ok:
			
			Intent it = new Intent(getContext(), GOrderActivity.class);
			Bundle params = new Bundle();
			params.putInt(GOrderActivity.FRAGMENT_MARK, GOrderActivity.FRAGMENT_MARK_GENERATE_ORDER);
			params.putString(GOrderGenerateFragment.key_agent_id, _agent.getId());
			params.putString(GOrderGenerateFragment.key_pay_type, _agent.getPayType());
			params.putString(GOrderGenerateFragment.key_relation_id, _agent.getCourtid());
			params.putString(GOrderGenerateFragment.key_relation_name, _agent.getCourtname());
			params.putString(GOrderGenerateFragment.key_tee_time, _agent.getTeeTime());
			params.putString(GOrderGenerateFragment.key_type, GOrderGenerateFragment.TYPE);
			params.putInt(GOrderGenerateFragment.key_unitprice, _agent.getPrice());
			
			it.putExtras(params);
			getContext().startActivity(it);
			
			this.dismiss();
			
			break;
		}
	}
	
}
