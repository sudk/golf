package com.jason.golf;

import java.util.Calendar;

import mirko.android.datetimepicker.date.DatePickerDialog;
import mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.AddAndSubView;
import com.jason.golf.classes.GCompetition;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GOrderBookingTripFragment extends Fragment implements
		OnClickListener {

	public static final String TYPE = "1";

	public static final String key_type = "order_type";
	public static final String key_relation_id = "order_relation_id";
	public static final String key_relation_name = "order_relation_name";
	public static final String key_tee_time = "order_tee_time";
	public static final String key_unitprice = "order_unitprice";
	public static final String key_pay_type = "order_pay_type";
	public static final String key_agent_id = "agent_id";
	public static final String key_agent_name = "agent_name";

	private final Calendar mCalendar = Calendar.getInstance();

	private AddAndSubView mAddAndSubView;

	private TextView mTripName, mAgentName, mTeeTime, mAmount, mPrice,
			mPaytype, mTripType;

	private String _mark;
	
	private EditText mPlayerName, mPhone;

	private Button mSubmit;

	private int _price;

	private int[] _priceArray;

	private DatePickerDialog mDatePickerDialog;

	private String _agentId, _relatioinId, _relationName, _teeTime, _payType,
			_orderType, _agentName;

	public static GOrderBookingTripFragment Instance() {
		return new GOrderBookingTripFragment();
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
		// _teeTime = params.getString(key_tee_time);
		_priceArray = params.getIntArray(key_unitprice);

//		if (_priceArray != null) {
//			_price = _priceArray[0];
//		}

		_price = 0;
		
		_payType = params.getString(key_pay_type);
		_agentName = params.getString(key_agent_name);
		// _orderType = params.getString(key_type);
		_orderType = TYPE;

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ActionBarActivity a = (ActionBarActivity) activity;
		ActionBar bar = a.getSupportActionBar();
		bar.setTitle(R.string.BookTrip);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_booking_trips, null);

		mTripName = (TextView) v.findViewById(R.id.booking_trip_name);
		mAgentName = (TextView) v.findViewById(R.id.booking_trip_agent_name);
		mTeeTime = (TextView) v.findViewById(R.id.booking_trip_tee_time);

		mTripType = (TextView) v.findViewById(R.id.booking_trip_type);
		mTripType.setOnClickListener(this);

		LinearLayout linearLayout1 = (LinearLayout) v
				.findViewById(R.id.booking_trip_number);
		mAddAndSubView = new AddAndSubView(getActivity(), 1);
		mAddAndSubView.setButtonBgResource(R.drawable.button_background,
				R.drawable.button_background);
		linearLayout1.addView(mAddAndSubView);
		mAddAndSubView
				.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {

					@Override
					public void onNumChange(View view, int num) {

						calculatePrice();

					}
				});

		mPlayerName = (EditText) v.findViewById(R.id.booking_trip_player_name);
		mPhone = (EditText) v.findViewById(R.id.booking_trip_phone);

		mPrice = (TextView) v.findViewById(R.id.booking_trip_price);
		mAmount = (TextView) v.findViewById(R.id.booking_trip_amount);
		mPaytype = (TextView) v.findViewById(R.id.booking_trip_paytype);

		mSubmit = (Button) v.findViewById(R.id.booking_submit);
		mSubmit.setOnClickListener(this);

		mTeeTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// mDatePickerDialog.show(manager, tag);

				mDatePickerDialog.show(getFragmentManager(), "SelsectDate");

			}
		});

		mDatePickerDialog = DatePickerDialog.newInstance(
				new OnDateSetListener() {

					public void onDateSet(DatePickerDialog datePickerDialog,
							int year, int month, int day) {

						// mSelectDate.setText(new
						// StringBuilder().append(pad(day))
						// .append(" ").append(pad(month + 1)).append(" ")
						// .append(pad(year)));

						mTeeTime.setText(String.format("%d月%d日", month + 1, day));
						_teeTime = String.format("%d-%02d-%02d", year, month + 1, day);
					}

				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH));

		mTripName.setText(_relationName);
		mAgentName.setText(_agentName);
		mPrice.setText(String.format("￥%.2f", (float) _price / 100));
		mAmount.setText(String.format("￥%.2f", (float) _price / 100));
		mPaytype.setText(GCompetition.GetFeeTypeDes(_payType));

		return v;
	}

	private void calculatePrice(){
		int num = mAddAndSubView.getNum();
		
		mPrice.setText(String.format("￥%.2f", (float) _price * num / 100));
		mAmount.setText(String.format("￥%.2f", (float) _price * num / 100));
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.booking_trip_type:

			final String[] des;

			if (_priceArray.length == 2) {
				des = new String[] { String.format("假日价格 %d元",  _priceArray[0] / 100),
						String.format("平日价格 %d元", _priceArray[1] / 100) };
			} else {
				des = new String[] { String.format("假日价格 %d元", _priceArray[0]/100) };
			}

			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setItems(des, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					mTripType.setText(des[which]);
					_price = _priceArray[which];
					_mark = des[which];
					calculatePrice();
				}
			});

			builder.create().show();

			break;

		case R.id.booking_submit:
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
				params.put("amount",String.format("%d", _price * mAddAndSubView.getNum()));
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
														// Intent it = new
														// Intent(
														// getActivity(),
														// GOrderActivity.class);
														Bundle params = new Bundle();
														//
														// params.putInt(GOrderActivity.FRAGMENT_MARK,
														// GOrderActivity.FRAGMENT_MARK_VIEW_ORDER);
														params.putString(
																GOrderDetailsFragment.KEY_ORDER_ID,
																orderid);
														//
														// it.putExtras(params);
														// startActivity(it);

														Fragment detailFragment = GOrderDetailsFragment
																.Instance();
														detailFragment
																.setArguments(new Bundle(
																		params));

														FragmentTransaction transaction = getFragmentManager()
																.beginTransaction();
														transaction.replace(
																R.id.container,
																detailFragment);
														// Commit the
														// transaction
														transaction.commit();
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
