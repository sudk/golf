package com.jason.golf.dialog;

import com.jason.golf.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RefundOrderDialog extends Dialog implements android.view.View.OnClickListener {
	
	private Button mOK, mCancel;
	private EditText mRefundRemark;
	private TextView mMessage;
	private IRefundListener mListener;
	
	private String _amout;

//	public OrderDialog(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
	
	public RefundOrderDialog(Context context, String amout){
		super(context,R.style.OrderDialog);
		_amout = amout;
	}
	
	public void setOnRefundListener(IRefundListener l){
		mListener = l;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_refund);
		
		mMessage = (TextView) findViewById(R.id.dialog_refund_message);
		mMessage.setText(String.format("是否退款，%s元", _amout));
		
		mRefundRemark = (EditText) findViewById(R.id.dialog_refund_remark);
		mRefundRemark.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(mRefundRemark.length() == 0){
					mOK.setEnabled(false);
				}else{
					mOK.setEnabled(true);
				}
			}
		});
		
		mOK = (Button) findViewById(R.id.dialog_refund_ok);
		mOK.setEnabled(false);
		mOK.setOnClickListener(this);
		
		mCancel = (Button) findViewById(R.id.dialog_refund_cancel);
		mCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.dialog_refund_ok:
			
			if(mListener!=null) mListener.onRefund(mRefundRemark.getText().toString());
			
			this.dismiss();
			
			break;
		case R.id.dialog_refund_cancel:
			
			dismiss();
			break;
		}
	}
	
	public interface IRefundListener {
		public void onRefund(String desc);
	}
	
}
