package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GOrder;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class GOrderDetailsFragment extends Fragment implements OnClickListener {

	public static final String KEY_ORDER_ID = "order_id";
	
	private String _orderId;
	
	private TextView mCourtName, mTeeTime, mOrderType, mCount, mAmount, mPaytype, mStatus, mContact, mPhone ;

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
		
		queryOrder();
		return v;
	}

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
						JSONObject data = new JSONObject(res);
						mCourtName.setText(data.getString("relation_name"));
						mTeeTime.setText(data.getString("tee_time"));
						mOrderType.setText(GOrder.GetOrderTypeDes(data.getString("type")));
						
						mCount.setText(data.getString("count"));
						float amount =  Float.parseFloat(data.getString("amount"));
						mAmount.setText(String.format("￥%.2f",amount/100));
						mPaytype.setText(GOrder.GetPayTypeDes(data.getString("pay_type")));
						mStatus.setText(GOrder.GetStatusDes(data.getString("status")));
						mContact.setText(data.getString("contact"));
						mPhone.setText(data.getString("phone"));
						
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
		
		
	}
}
