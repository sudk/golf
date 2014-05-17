package com.jason.golf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.AddAndSubView;
import com.jason.golf.classes.GAgent;
import com.jason.golf.classes.GCompetition;
import com.jason.golf.dialog.WarnDialog;
import com.jsaon.golf.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GOrderApplyFragment extends Fragment implements OnClickListener {
	
	public static final String TYPE = "2";

	public static final String key_type = "order_type";
	public static final String key_relation_id = "order_relation_id";
	public static final String key_relation_name = "order_relation_name";
	public static final String key_tee_time = "order_tee_time";
	public static final String key_unitprice = "order_unitprice";
	public static final String key_pay_type = "order_pay_type";
	public static final String key_agent_id = "agent_id";
	public static final String key_agent_name = "agent_name";

	private AddAndSubView mAddAndSubView;

	private TextView mCompetitionName, mAgentName, mTeeTime, mAmount, mPrice, mPaytype;

	private EditText mPlayerName, mPhone, mNum;

	private Button mSubmit;

	private int _price;

	private String _agentId, _relatioinId, _relationName, _teeTime, _payType, _orderType, _agentName;

	public static GOrderApplyFragment Instance() {
		return new GOrderApplyFragment();
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
		_agentId = params.getString(key_agent_id);
		_relatioinId = params.getString(key_relation_id);
		_relationName = params.getString(key_relation_name);
		_teeTime = params.getString(key_tee_time);
		_price = params.getInt(key_unitprice, 0);
		_payType = params.getString(key_pay_type);
		_agentName = params.getString(key_agent_name);
		// _orderType = params.getString(key_type);
		_orderType = TYPE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_apply_generate, null);

		mCompetitionName = (TextView) v
				.findViewById(R.id.apply_competition_name);
		mAgentName = (TextView) v.findViewById(R.id.apply_agent_name);
		mTeeTime = (TextView) v.findViewById(R.id.apply_tee_time);

		LinearLayout linearLayout1 = (LinearLayout) v
				.findViewById(R.id.apply_pick_number);
		mAddAndSubView = new AddAndSubView(getActivity(), 1);
		mAddAndSubView.setButtonBgResource(R.drawable.button_background,
				R.drawable.button_background);
		linearLayout1.addView(mAddAndSubView);
		mAddAndSubView
				.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {

					@Override
					public void onNumChange(View view, int num) {

						mPrice.setText(String.format("￥%.2f", (float) _price
								* num / 100));
						mAmount.setText(String.format("￥%.2f", (float) _price
								* num / 100));

					}
				});

		mPlayerName = (EditText) v.findViewById(R.id.apply_players_name);
		mPhone = (EditText) v.findViewById(R.id.apply_phone);

		mPrice = (TextView) v.findViewById(R.id.apply_price);
		mAmount = (TextView) v.findViewById(R.id.apply_amount);
		mPaytype = (TextView) v.findViewById(R.id.apply_paytype_desc);

		mSubmit = (Button) v.findViewById(R.id.apply_submit);
		mSubmit.setOnClickListener(this);

		mCompetitionName.setText(_relationName);
		mAgentName.setText(_agentName);
		mTeeTime.setText(String.format("TeeTime：%s", _teeTime));
		mPrice.setText(String.format("￥%.2f", (float) _price / 100));
		mAmount.setText(String.format("￥%.2f", (float) _price / 100));
		mPaytype.setText(GCompetition.GetFeeTypeDes(_payType));

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.apply_submit:
			/*
			 * type 类型 String ordertype 订单类型，0、订场；1、行程；3、赛事报名； relation_id 关联ID
			 * String relation_name 关联商品名称 String tee_time 打球时间 String count 数量
			 * String unitprice 单价 String amount 订单金额 String pay_type 支付类型
			 * String contact 联系人 String phone 电话 String agent_id 代理商ID String
			 */

			JSONObject params = new JSONObject();

			String phone = mPhone.getText().toString();
			String contact = mPlayerName.getText().toString();
			try {
				params.put("cmd", "order/create");
				params.put("type", _orderType);
				params.put("relation_id", _relatioinId);
				params.put("relation_name", _relationName);
				params.put("tee_time", _teeTime);
				params.put("count",	String.format("%d", mAddAndSubView.getNum()));
				params.put("unitprice", String.format("%d", _price));
				params.put("amount", String.format("%d", _price * mAddAndSubView.getNum()));
				params.put("pay_type", _payType);
				params.put("contact", contact);
				params.put("phone", phone);
				params.put("agent_id", _agentId);

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
							// {"status":0,"desc":"\u6210\u529f","data":true}

							try {
								JSONArray array = new JSONArray(res);
								final String orderid = array.getJSONObject(0)
										.getString("order_id");

								WarnDialog dialog = new WarnDialog(
										getActivity());
								dialog.setTitle(R.string.order)
										.setMessage(R.string.order_successful)
										.setPositiveBtn(
												R.string.confirm,
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														Intent it = new Intent(	getActivity(), GOrderActivity.class);
														Bundle params = new Bundle();

														params.putInt(
																GOrderActivity.FRAGMENT_MARK,
																GOrderActivity.FRAGMENT_MARK_VIEW_ORDER);
														params.putString(
																GOrderDetailsFragment.KEY_ORDER_ID,
																orderid);

														it.putExtras(params);
														startActivity(it);
													}

												});
								dialog.show(getFragmentManager(),
										"CreateApplySuccessfull");

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						@Override
						public void faildData(int code, String res) {
							// TODO Auto-generated method stub
							super.faildData(code, res);

							WarnDialog dialog = new WarnDialog(getActivity());
							dialog.setTitle(R.string.register_title)
									.setMessage(res)
									.setPositiveBtn(
											R.string.confirm,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub

												}
											});
							dialog.show(getFragmentManager(),
									"CreateApplyFaild");
						}

					});

			GThreadExecutor.execute(r);

			break;
		}

	}
}
