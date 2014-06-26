package com.jason.golf.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.GScoreDetailsFragment;
import com.jason.golf.R;
import com.jason.golf.adapters.PopTeeListAdapter;
import com.jason.golf.classes.GScoreDetailBean;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class ScroseDetailEditDialog extends Dialog implements android.view.View.OnClickListener, TextWatcher, OnItemClickListener {
	
	private PopupWindow mPop;
	
	private TextView mTee, mHole;
	
	private EditText mStd, mLong, mPush ;
	
	private Button mCancel, mSubmit;
	
	private GScoreDetailBean _bean;
	
	private FragmentActivity _fat;
	
	private int _tee , _position;
	
	public ScroseDetailEditDialog(FragmentActivity context, GScoreDetailBean bean, int postion){
		super(context,R.style.OrderDialog);
		_bean = bean;
		_fat = context;
		_tee = Integer.parseInt(bean.getTee());
		_position = postion;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_score_details);
		
		mTee = (TextView) findViewById(R.id.dialog_score_tee);
		mTee.setText(GScoreDetailBean.GetTeeDesc(_bean.getTee()));
		mTee.setOnClickListener(this);
		
		mHole = (TextView) findViewById(R.id.dialog_score_hole);
		mHole.setText(String.format("球洞：%d ", _bean.getHoleNo()));
		
		mStd = (EditText) findViewById(R.id.dialog_score_std);
		mStd.setText(_bean.getStandardBar());
		mStd.addTextChangedListener(this);
		
		mLong = (EditText) findViewById(R.id.dialog_score_long);
		mLong.setText(_bean.getLangBar());
		mLong.addTextChangedListener(this);
		
		mPush = (EditText) findViewById(R.id.dialog_score_push);
		mPush.setText(_bean.getPushBar());
		mPush.addTextChangedListener(this);
		
		mCancel = (Button) findViewById(R.id.dialog_score_cancel);
		mCancel.setOnClickListener(this);
		mSubmit = (Button) findViewById(R.id.dialog_score_submit);
		mSubmit.setOnClickListener(this);
		
		checkButton();
	}

	private void checkButton() {
		// TODO Auto-generated method stub
		
		if(mStd.length() == 0 || mPush.length() == 0 || mLong.length() == 0){
			mSubmit.setEnabled(false);
		}else{
			mSubmit.setEnabled(true);
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		
		case R.id.dialog_score_tee:
			
			if(mPop == null){
				initPop();
			}
			System.out.println("弹出PopWindows！");
			mPop.showAsDropDown(mTee);  
			
			break;
		case R.id.dialog_score_submit:
			
			JSONObject params = new JSONObject();

			try {

				params.put("cmd", "score/dupdate");
				params.put("id", _bean.getId());
				params.put("score_id", _bean.getScoreId());
				params.put("hole_no", String.format("%d", _bean.getHoleNo()));
				params.put("tee", String.format("%d", _tee));
				params.put("standard_bar", mStd.getText());
				params.put("lang_bar", mLong.getText());
				params.put("push_bar", mPush.getText());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpRequest r = new HttpRequest(_fat, params, new HttpCallback() {

				@Override
				public void sucessData(String res) {
					// TODO Auto-generated method stub
					super.sucessData(res);
					
					Intent intent = new Intent(GScoreDetailsFragment.BROADCAST_REFRESH_ACTION);
					intent.putExtra("Position", _position);
					
					LocalBroadcastManager.getInstance(_fat).sendBroadcast(intent);
				}
				
			} );
			
			GThreadExecutor.execute(r);
			this.dismiss();
			break;
		case R.id.dialog_score_cancel:
			this.cancel();
			break;
		}
	}

	private void initPop(){
		
		ListView contentView = (ListView) LayoutInflater.from(this.getContext()).inflate(R.layout.pop_tees_layout, null);  
		contentView.setAdapter(new PopTeeListAdapter(getContext()));
		contentView.setOnItemClickListener(this);
		contentView.setSelection(_tee-1);

		mPop = new PopupWindow(contentView,  mTee.getWidth(), mTee.getHeight() * 5);
		mPop.setBackgroundDrawable(new BitmapDrawable());
		mPop.setOutsideTouchable(true);
		mPop.setFocusable(true);
				
	}
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		checkButton();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub
		mTee.setText((CharSequence) parent.getAdapter().getItem(position));
		_tee = position + 1 ;
		mPop.dismiss();
	}
	
}
