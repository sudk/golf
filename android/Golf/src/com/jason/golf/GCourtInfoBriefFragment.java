package com.jason.golf;

import java.util.ArrayList;
import java.util.Calendar;

import mirko.android.datetimepicker.date.DatePickerDialog;
import mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import mirko.android.datetimepicker.time.RadialPickerLayout;
import mirko.android.datetimepicker.time.TimePickerDialog;
import mirko.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.AdvertisementAdapter;
import com.jason.golf.classes.AgentsAdapter;
import com.jason.golf.classes.CourtImgsAdapter;
import com.jason.golf.classes.GAdver;
import com.jason.golf.classes.GAgent;
import com.jason.golf.classes.GCourt;
import com.jason.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class GCourtInfoBriefFragment extends Fragment implements OnClickListener {
	
	private static final String KEY_COURT_ID = "court_id";
	public static final String KEY_DATE = "key_date";
	public static final String KEY_TIME = "key_time";
	
	private ViewPager mCourtImgs;
	private CourtImgsAdapter mAdapter;
	private Handler mHandler;
	private int mViewPagerCurrentPosition;
	private ArrayList<String> _imgs;
	
	private TextView mAgentDate, mAgentTime;
	private TextView mCourtAddr, mCourtBrief;
	
	private ListView mAgentList;
	private AgentsAdapter mAgentsAdapter;
	private ScrollView mSroller;
	
	private ArrayList<GAgent> _agents;
	
	private String _courtId;
	
	private int _year, _month, _day, _hour, _minute;
	
	private GCourt _court; 
	
	
	public static GCourtInfoBriefFragment Instance(String courtId, String date, String time){
		GCourtInfoBriefFragment fragment = new GCourtInfoBriefFragment();
		Bundle params = new Bundle();
		params.putString(KEY_COURT_ID, courtId);
		params.putString(KEY_DATE, date);
		params.putString(KEY_TIME, time);
		fragment.setArguments(params);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		_imgs = new ArrayList<String>();
		_agents = new ArrayList<GAgent>();
		
		_courtId = getArguments().getString(KEY_COURT_ID);
		String date = getArguments().getString(KEY_DATE);
		String time = getArguments().getString(KEY_TIME);
		
		if(!TextUtils.isEmpty(date)){
			String[] d = date.split("-");
			_year = Integer.parseInt(d[0]);
			_month = Integer.parseInt(d[1]);
			_day = Integer.parseInt(d[2]);
		}else{
			Calendar c = Calendar.getInstance();
			_year = c.get(Calendar.YEAR);
			_month = c.get(Calendar.MONTH);
			_day = c.get(Calendar.DAY_OF_MONTH);
		}
		
		if(!TextUtils.isEmpty(time)){
			String[] t = time.split(":");
			_hour = Integer.parseInt(t[0]);
			_minute = Integer.parseInt(t[1]);
		}else{
			_hour = 0;
			_minute = 0;
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.fragment_court_info_brief, null);
		
		mCourtImgs = (ViewPager) v.findViewById(R.id.court_imgs);
		
		mAdapter = new CourtImgsAdapter(getActivity(),_imgs);
		mCourtImgs.setAdapter(mAdapter);
		mCourtImgs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mViewPagerCurrentPosition = position;
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
		mHandler = new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					int count = mAdapter.getCount();
					if (count > 1){
						mCourtImgs.setCurrentItem(( ++ mViewPagerCurrentPosition ) % count, true);
					}
				}
				mHandler.removeMessages(1);
				mHandler.sendEmptyMessageDelayed(1,	5000);
				return false;
			}
		});
		mHandler.sendEmptyMessageDelayed(1, 5000);
		
		
		
		
		
		
		
		
		mAgentDate = (TextView) v.findViewById(R.id.agent_date);
		mAgentDate.setOnClickListener(this);
		mAgentDate.setText(String.format("%d月%d日", _month, _day));
		mAgentTime = (TextView) v.findViewById(R.id.agent_time);
		mAgentTime.setOnClickListener(this);
		mAgentTime.setText(String.format("%02d:%02d", _hour, _minute));
		
		mCourtAddr = (TextView) v.findViewById(R.id.court_addr);
		mCourtAddr.setOnClickListener(this);
		mCourtBrief = (TextView) v.findViewById(R.id.court_brief);
		mCourtBrief.setOnClickListener(this);
		
		v.findViewById(R.id.court_addr_triangle).setOnClickListener(this);;
		v.findViewById(R.id.court_brief_triangle).setOnClickListener(this);;
		
		mSroller = (ScrollView) v.findViewById(R.id.scroller_solution);
		mAgentList = (ListView) v.findViewById(R.id.court_agents);
		mAgentsAdapter = new AgentsAdapter(this.getActivity(), _agents);
		mAgentList.setAdapter(mAgentsAdapter);
		setListViewHeightBasedOnChildren(mAgentList);
		
		queryCourtInfo(_courtId);
		quertAgents(_courtId);
		
		return v;
	}
	
	
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	private void quertAgents(final String c){
		
		JSONObject params = new JSONObject();
		
		try {
			params.put("cmd", "court/price");
			params.put("court_id", c);
			params.put("type", "");
			params.put("date_time", String.format("%d-%02d-%02d %02d:%02d", _year, _month, _day, _hour, _minute));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				
				try {
					JSONArray data = new JSONArray(res);
					
					ArrayList<GAgent> agents = new ArrayList<GAgent>();
					
					for(int i=0, length=data.length(); i<length; i++){
						
						JSONObject item = data.getJSONObject(i);
						
						GAgent agent = new GAgent();
						agent.setId(item.getString("agent_id"));
						agent.setName(item.getString("agent_name"));
						agent.setCourtid(item.getString("court_id"));
						agent.setCourtname(item.getString("court_name"));
						agent.setPriceremark(item.getString("remark"));
						agent.setGreen(item.getString("is_green"));
						agent.setCaddie(item.getString("is_caddie"));
						agent.setCar(item.getString("is_car"));
						agent.setWardrobe(item.getString("is_wardrobe"));
						agent.setMeal(item.getString("is_meal"));
						agent.setInsurance(item.getString("is_insurance"));
						agent.setTips(item.getString("is_tip"));
						agent.setPayType(item.getString("pay_type"));
						agent.setPrice(item.getString("price"));
						agent.setCancelRemark(item.getString("cancel_remark"));
//						agent.setBargainPriceDes(item.getString("special_desc"));
						
						agent.setTeeTime(String.format("%d-%02d-%02d %02d:%02d", _year, _month, _day, _hour, _minute));
						agents.add(agent);
						
					}
					
					mAgentsAdapter.swapData(agents);
					setListViewHeightBasedOnChildren(mAgentList);
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.sucessData(res);
				
			}
			
		});
		
		GThreadExecutor.execute(r);
		
	}
	
	private void queryCourtInfo(String cid){
		

		JSONObject params = new JSONObject();
		
		try {
			params.put("cmd", "court/info");
			params.put("court_id", cid);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				
				try {
					JSONObject data = new JSONObject(res);
					
					_court = new GCourt();
					_court.setId(data.getString("court_id"));
					_court.setName(data.getString("name"));
					_court.setAddress(data.getString("addr"));
					_court.setModel(data.getString("model"));
					_court.setCreateYear(data.getString("create_year"));
					_court.setArea(data.getString("area"));
					_court.setGreenGrass(data.getString("green_grass"));
					_court.setCourtData(data.getString("court_data"));
					_court.setDesigner(data.getString("designer"));
					_court.setFairwayLength(data.getString("fairway_length"));
					_court.setFairwayGrass(data.getString("fairway_grass"));
					_court.setPhone(data.getString("phone"));
					_court.setRemark(data.getString("remark"));
					_court.setFacilities(data.getString("facilities"));

					JSONArray fi = data.getJSONArray("fairway_imgs");
					
					for(int fi_i=0, fi_length=fi.length(); fi_i<fi_length; fi_i++){
						
						_court.addFairwayImg((String) fi.get(fi_i));
					}
					
					
					JSONArray ci = data.getJSONArray("court_imgs");
					for(int ci_i=0, ci_length=ci.length(); ci_i<ci_length; ci_i++){
						
						_court.addCourtImg((String) ci.get(ci_i));
					}
					
					mAdapter.swapData(_court.getCourtImgs());
					
					GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
					a.setCourt(_court);
					
					System.out.println(_court.toString());
					mCourtAddr.setText(_court.getAddress());
					mCourtBrief.setText(_court.getRemark());
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.sucessData(res);
				
//				try {
//					JSONObject resObj = new JSONObject(res);
//					
//					int status = resObj.getInt("status");
//					
//							if (status == 0) {
//
//								JSONObject data = resObj.getJSONObject("data");
//								GCourt court = new GCourt();
//								court.setId(data.getString("court_id"));
//								court.setName(data.getString("name"));
//								court.setAddress(data.getString("addr"));
//								court.setModel(data.getString("model"));
//								court.setCreateYear(data.getString("create_year"));
//								court.setArea(data.getString("area"));
//								court.setGreenGrass(data.getString("green_grass"));
//								court.setCourtData(data.getString("court_data"));
//								court.setDesigner(data.getString("designer"));
//								court.setFairwayLength(data.getString("fairway_length"));
//								court.setFairwayGrass(data.getString("fairway_grass"));
//								court.setPhone(data.getString("phone"));
//								court.setRemark(data.getString("remark"));
//								court.setFacilities(data.getString("facilities"));
//
//								JSONArray fi = data.getJSONArray("fairway_imgs");
//								
//								for(int fi_i=0, fi_length=fi.length(); fi_i<fi_length; fi_i++){
//									
//									court.addFairwayImg((String) fi.get(fi_i));
//								}
//								
//								
//								JSONArray ci = data.getJSONArray("court_imgs");
//								for(int ci_i=0, ci_length=ci.length(); ci_i<ci_length; ci_i++){
//									
//									court.addCourtImg((String) ci.get(ci_i));
//								}
//								
//								GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
//								a.setCourt(court);
//								
//								System.out.println(court.toString());
//								mCourtAddr.setText(court.getAddress());
//								mCourtBrief.setText(court.getRemark());
//							}
//					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
				
			}
			
		});
		
		GThreadExecutor.execute(r);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void setListViewHeightBasedOnChildren(ListView listView) {   
        // 获取ListView对应的Adapter   
		AgentsAdapter listAdapter = (AgentsAdapter) listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);  
        mSroller.smoothScrollTo(0, 0);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.agent_date:
			
			final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
					new OnDateSetListener() {

						public void onDateSet(DatePickerDialog datePickerDialog,
								int year, int month, int day) {

//							mSelectDate.setText(new StringBuilder().append(pad(day))
//									.append(" ").append(pad(month + 1)).append(" ")
//									.append(pad(year)));
							
							_year = year;
							_month = month + 1;
							_day = day;
							
							mAgentDate.setText(String.format("%d月%d日", _month, _day));
							quertAgents(_courtId);
						}

					}, _year, _month-1, _day );
			
			datePickerDialog.show(getFragmentManager(), "SelsectDate");
			
			break;
		case R.id.agent_time:
			
			TimePickerDialog timePickerDialog12h = TimePickerDialog.newInstance(new OnTimeSetListener() {

				@Override
				public void onTimeSet(RadialPickerLayout view, int hourOfDay,
						int minute) {

					_hour = hourOfDay;
					_minute = minute;
					
					mAgentTime.setText(String.format("%02d:%02d", _hour, _minute));
					
					quertAgents(_courtId);
				}
			}, _hour, _minute, false);
			
			timePickerDialog12h.show(getFragmentManager(), "SelectTime");
			
			break;
		case R.id.court_addr:
		case R.id.court_addr_triangle:
			
			//打开地图
			Intent mapIntent = new Intent(getActivity(), MapActivity.class);
			Bundle params = new Bundle();
			params.putString(MapActivity.KEY_COURT_ADDR, _court.getAddress());
			mapIntent.putExtras(params);
			startActivity(mapIntent);
			
			break;
			
		case R.id.court_brief:
		case R.id.court_brief_triangle:
			Fragment newFragment = GCourtInfoDetailsFragment.Instance();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();

			transaction.replace(R.id.container, newFragment);
			transaction.addToBackStack(null);

			transaction.commit();
			
			break;
		}
	} 

}
