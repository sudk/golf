package com.jason.golf;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GTrip;
import com.jason.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GTripDetailsFragment extends Fragment implements OnClickListener {

	public static final String KEY_TRIP_ID = "trip_id";

	private String _tripId, _amount;

	private TextView mTripName, mTripAgent, mTripStartDate, mTripEndDate,
			mTripCourt, mTtripDesc, mPriceNormal, mPriceHoliday, mTripDesc;

	private LinearLayout mTripImages;

	private ImageView mTriange;

	private Button mBookingTrip;

	private LinearLayout mPayButtons;

	private FinalBitmap _fb;
	
	private GTrip _trip;

	public static GTripDetailsFragment Instance() {
		return new GTripDetailsFragment();
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

		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);

		Bundle params = getArguments();
		_tripId = params.getString(KEY_TRIP_ID, "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_trip_detail, null);

		mTripName = (TextView) v.findViewById(R.id.trip_name);
		mTripAgent = (TextView) v.findViewById(R.id.trip_agent);
		mTripStartDate = (TextView) v.findViewById(R.id.trip_start_date);
		mTripEndDate = (TextView) v.findViewById(R.id.trip_end_date);
		mTripCourt = (TextView) v.findViewById(R.id.trip_court);
		mTtripDesc = (TextView) v.findViewById(R.id.trip_desc);
		mPriceNormal = (TextView) v.findViewById(R.id.trip_price_normal);
		mPriceHoliday = (TextView) v.findViewById(R.id.trip_price_holiday);
		mTripDesc = (TextView) v.findViewById(R.id.trip_desc);
		mTriange = (ImageView) v.findViewById(R.id.trip_triange);
		mTripImages = (LinearLayout) v.findViewById(R.id.trip_images);
		
		mBookingTrip = (Button) v.findViewById(R.id.trip_booking);
		mBookingTrip.setOnClickListener(this);
		
		queryTripInfo();

		return v;
	}

	/*
	 * 
	 * 查询订单详情
	 */
	private void queryTripInfo() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "trip/info");
			params.put("id", _tripId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params,
				new HttpCallback() {

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						super.sucessData(res);

						// {"order_id":"20140503151643308669","user_id":"6","type":"0","relation_id":"admin20140501170636","relation_name":"\u9752\u5c9b\u51ef\u601d\u4e50\u9ad8\u5c14\u592b\u4ff1\u4e50\u90e8","tee_time":"2014-05-04 09:30:00","count":"3","unitprice":"600","amount":"1800","had_pay":"0","pay_type":"1","status":"0","record_time":"2014-05-03 15:16:43","desc":null,"agent_id":"1","contact":"\u9646\u6bc5","phone":"18653157590"}}

						try {

							JSONObject data = new JSONObject(res);
							_trip = new GTrip();
							_trip.initialize(data);

							mTripName.setText(_trip.getTripName());
							mTripAgent.setText(_trip.getAgentName());
							mTripCourt.setText(_trip.getCourtName());
							mTripStartDate.setText(_trip.getStartDate());
							mTripEndDate.setText(_trip.getEndDate());

							mPriceNormal.setText(String.format("￥%.2f",
									(float) _trip.getNormalPrice() / 100));
							mPriceHoliday.setText(String.format("￥%.2f",
									(float) _trip.getHolidayPrice() / 100));
							mTripDesc.setText(_trip.getDesc());

							// mTripImages

							DisplayMetrics m = getActivity().getResources()
									.getDisplayMetrics();
							int widthPixels = m.widthPixels;

							mTriange.measure(0, 0);

							View v = getView();

							int width = (widthPixels - v.getPaddingLeft()
									- v.getPaddingRight() - mTriange
									.getMeasuredWidth()) * 3 / 4;
							int imgMargin = 2;

							int imgWidth = width / 3 - imgMargin * 2;
							ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(
									imgWidth, imgWidth);

							ArrayList<String> imgUrl = _trip.getImgs();

							int length = imgUrl.size() <= 3 ? imgUrl.size() : 3;

							for (int i = 0; i < length; i++) {
								ImageView img = new ImageView(getActivity());
								img.setLayoutParams(param);
								img.setPadding(2, 2, 2, 2);
								mTripImages.addView(img);
								_fb.display(img, imgUrl.get(i));
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
		switch (v.getId()) {
		case R.id.trip_booking:
			
			GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
			if(app.getAccount().isLogin()){

//				CompetitionApplyingDialog dialog = new CompetitionApplyingDialog(getActivity(), _competition);
//				dialog.show();
				
				Intent it = new Intent(getActivity(), GOrderActivity.class);
				Bundle params = new Bundle();
				params.putInt(GOrderActivity.FRAGMENT_MARK, GOrderActivity.FRAGMENT_MARK_GENERATE_TRIP);
				params.putString(GOrderApplyFragment.key_agent_id, _trip.getAgentId());
				params.putString(GOrderApplyFragment.key_pay_type, _trip.getPayType());
				params.putString(GOrderApplyFragment.key_relation_id, _trip.getId());
				params.putString(GOrderApplyFragment.key_relation_name, _trip.getTripName());
				params.putString(GOrderApplyFragment.key_tee_time, _trip.getStartDate());
				params.putString(GOrderApplyFragment.key_agent_name, _trip.getAgentName());
				params.putString(GOrderApplyFragment.key_type, GOrderBookingTripFragment.TYPE);
				
				
				
				/*
				 * 
				 * 
				 *   使用什么价格？  普通价格 还是 假日价格 ？
				 * 
				 * 
				 */
				
				
				params.putInt(GOrderApplyFragment.key_unitprice, _trip.getHolidayPrice());
				
				
				
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
