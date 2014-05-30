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


public class PayBalanceDialog extends Dialog implements android.view.View.OnClickListener {
	
	private Button mOK, mCancel;
	private EditText mPassword;
	private IPayBalanceListener mListener;
	
//	public OrderDialog(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
	
	public PayBalanceDialog(Context context){
		super(context,R.style.OrderDialog);
	}
	
	public void setOnRefundListener(IPayBalanceListener l){
		mListener = l;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_paybalance);
		
		
		mPassword = (EditText) findViewById(R.id.dialog_password);
		mPassword.addTextChangedListener(new TextWatcher() {
			
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
				if(mPassword.length() == 0){
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
			
			if(mListener!=null) mListener.onPay(mPassword.getText().toString());
			
			this.dismiss();
			
			break;
		case R.id.dialog_refund_cancel:
			
			dismiss();
			break;
		}
	}
	
	public interface IPayBalanceListener {
		public void onPay(String password);
	}
	
}
