package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GOrder;
import com.jason.golf.dialog.WarnDialog;
import com.jsaon.golf.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GOrderDetailsFragment extends Fragment implements OnClickListener {

	public static final String KEY_ORDER_ID = "order_id";
	private static final String Pay_Balance = "1";
	private static final String Pay_Yinlian = "2";
	
	private String _orderId, _amount;
	
	private TextView mCourtName, mTeeTime, mOrderType, mCount, mAmount, mPaytype, mStatus, mContact, mPhone ;
	
	private Button mCancel, mPayBalance;
	
	private LinearLayout mPayButtons;

	public static GOrderDetailsFragment Instance() {
		return new GOrderDetailsFragment();
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
		
		Bundle params = getArguments();
		_orderId = params.getString(KEY_ORDER_ID,"");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_order_detail, null);
		
		mCourtName = (TextView) v.findViewById(R.id.order_detail_court_name);
		mTeeTime = (TextView) v.findViewById(R.id.order_detail_tee_time);
		mOrderType = (TextView) v.findViewById(R.id.order_detail_type);
		mCount = (TextView) v.findViewById(R.id.order_detail_count);
		mAmount = (TextView) v.findViewById(R.id.order_detail_amount);
		mPaytype = (TextView) v.findViewById(R.id.order_detail_paytype);
		mStatus = (TextView) v.findViewById(R.id.order_detail_status);
		mContact = (TextView) v.findViewById(R.id.order_detail_contact);
		mPhone = (TextView) v.findViewById(R.id.order_detail_phone);
		
		mCancel = (Button) v.findViewById(R.id.order_detail_cancel);
		mCancel.setOnClickListener(this);
		
		mPayButtons = (LinearLayout) v.findViewById(R.id.pay_button_layout);
		mPayButtons.setOnClickListener(this);
		
		mPayBalance = (Button) v.findViewById(R.id.order_detail_pay_balance);
		mPayBalance.setOnClickListener(this);
		
		queryOrder();
		return v;
	}

	
	/*
	 * 
	 * 查询订单详情
	 * 
	 * 
	 */
	private void queryOrder() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "order/info");
			params.put("order_id", _orderId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params,new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
//				{"order_id":"20140503151643308669","user_id":"6","type":"0","relation_id":"admin20140501170636","relation_name":"\u9752\u5c9b\u51ef\u601d\u4e50\u9ad8\u5c14\u592b\u4ff1\u4e50\u90e8","tee_time":"2014-05-04 09:30:00","count":"3","unitprice":"600","amount":"1800","had_pay":"0","pay_type":"1","status":"0","record_time":"2014-05-03 15:16:43","desc":null,"agent_id":"1","contact":"\u9646\u6bc5","phone":"18653157590"}}

				
					try {
						
						String payType, status;
						
						JSONObject data = new JSONObject(res);
						mCourtName.setText(data.getString("relation_name"));
						mTeeTime.setText(data.getString("tee_time"));
						mOrderType.setText(GOrder.GetOrderTypeDes(data.getString("type")));
						
						mCount.setText(data.getString("count"));
						_amount = data.getString("amount");
						float amount =  Float.parseFloat(_amount);
						mAmount.setText(String.format("￥%.2f",amount/100));
						
						//支付方式：0为现付，1为全额预付，2为押金
						payType = data.getString("pay_type");
						mPaytype.setText(GOrder.GetPayTypeDes(payType));
						
						//status : 0、待确认；1、待付款；2、完成预约；3、撤销 ，4、未到场  5、订单完成
						status = data.getString("status");
						mStatus.setText(GOrder.GetStatusDes(status));
						
						mContact.setText(data.getString("contact"));
						mPhone.setText(data.getString("phone"));
						
						
						// 判断是否显示"支付"按钮
						if("1".equals(payType) || "2".equals(payType)){
							if("1".equals(status)){
								mPayButtons.setVisibility(ViewGroup.VISIBLE);
							}else{
								mPayButtons.setVisibility(ViewGroup.GONE);
							}
						}else{
							mPayButtons.setVisibility(ViewGroup.GONE);
						}
						
						//判断是否显示"取消"按钮
						if("0".equals(status) || "1".equals(status)){
							mCancel.setVisibility(View.VISIBLE);
						}else{
							mCancel.setVisibility(View.GONE);
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
	
	
	/*
	 * 
	 * 撤销订单
	 * 
	 * 
	 */
	private void cancelTheOrder() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "order/cancel");
			params.put("order_id", _orderId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params,new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				queryOrder();
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				WarnDialog dialog = new WarnDialog(getActivity());
				dialog.setTitle(R.string.register_title).setMessage(res)
				.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				dialog.show(getFragmentManager(), "CancelOrderFaild");
			}
			
		});
		
		GThreadExecutor.execute(r);
	}
	
	/*
	 * 
	 * 支付订单
	 * 
	 * 
	 */
	private void payOrder(String payChannel) {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {
			params.put("cmd", "order/pay");
			params.put("order_id", _orderId);
			params.put("type", payChannel);
			params.put("amount", _amount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params,new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				WarnDialog dialog = new WarnDialog(getActivity());
				dialog.setTitle(R.string.order).setMessage(R.string.order_psy_successful)
				.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				dialog.show(getFragmentManager(), "PayOrderSuccess");
				GAccount acc = ((GolfAppliaction)getActivity().getApplication()).getAccount();
				acc.setBalance(String.format("%d", acc.getBalance() - Integer.parseInt(_amount)));
				queryOrder();
				
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				WarnDialog dialog = new WarnDialog(getActivity());
				dialog.setTitle(R.string.order).setMessage(res)
				.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				dialog.show(getFragmentManager(), "PayOrderFaild");
			}
			
		});
		
		GThreadExecutor.execute(r);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.order_detail_cancel:
			cancelTheOrder();
			break;
		case R.id.order_detail_pay_balance:
			payOrder(Pay_Balance);
			break;
		case R.id.order_detail_pay_yinlian:
			payOrder(Pay_Yinlian);
			break;
		}
		
	}

}
