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
import com.jason.golf.classes.AgentsAdapter;
import com.jason.golf.classes.GAgent;
import com.jason.golf.classes.GCourt;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class GCourtInfoBriefFragment extends Fragment implements OnClickListener {
	
	private static final String KEY_COURT_ID = "court_id";
	public static final String KEY_DATE = "key_date";
	public static final String KEY_TIME = "key_time";
	
	private TextView mAgentDate, mAgentTime;
	private TextView mCourtAddr, mCourtBrief;
	
	private ListView mAgentList;
	private AgentsAdapter mAgentsAdapter;
	private ScrollView mSroller;
	
	private ArrayList<GAgent> _agents;
	
	private String _courtId;
	
	private int _year, _month, _day, _hour, _minute;
	
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
		
		mAgentDate = (TextView) v.findViewById(R.id.agent_date);
		mAgentDate.setOnClickListener(this);
		mAgentDate.setText(String.format("%d月%d日", _month, _day));
		mAgentTime = (TextView) v.findViewById(R.id.agent_time);
		mAgentTime.setOnClickListener(this);
		mAgentTime.setText(String.format("%d:%d", _hour, _minute));
		
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

	private void quertAgents(String c){
		
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
						agent.setGreen("1".equals(item.getString("is_green")));
						agent.setCaddie("1".equals(item.getString("is_caddie")));
						agent.setCar("1".equals(item.getString("is_car")));
						agent.setWardrobe("1".equals(item.getString("is_wardrobe")));
						agent.setMeal("1".equals(item.getString("is_meal")));;
						agent.setInsurance("1".equals(item.getString("is_insurance")));
						agent.setTips("1".equals(item.getString("is_tip")));
						agent.setPayType(item.getInt("pay_type"));
						agent.setPrice(item.getString("price"));
//						agent.setBargainPriceDes(item.getString("special_desc"));
						
						agents.add(agent);
						
					}
					mAgentsAdapter.swapData(agents);
					setListViewHeightBasedOnChildren(mAgentList);
					
					
					
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
//					if( status == 0){
//					
//						JSONArray data = resObj.getJSONArray("data");
//						
//						ArrayList<GAgent> agents = new ArrayList<GAgent>();
//						
//						for(int i=0, length=data.length(); i<length; i++){
//							
//							JSONObject item = data.getJSONObject(i);
//							
//							GAgent agent = new GAgent();
//							agent.setId(item.getString("agent_id"));
//							agent.setName(item.getString("agent_name"));
//							agent.setCourtid(item.getString("court_id"));
//							agent.setCourtname(item.getString("court_name"));
//							agent.setPriceremark(item.getString("remark"));
//							agent.setGreen("1".equals(item.getString("is_green")));
//							agent.setCaddie("1".equals(item.getString("is_caddie")));
//							agent.setCar("1".equals(item.getString("is_car")));
//							agent.setWardrobe("1".equals(item.getString("is_wardrobe")));
//							agent.setMeal("1".equals(item.getString("is_meal")));;
//							agent.setInsurance("1".equals(item.getString("is_insurance")));
//							agent.setTips("1".equals(item.getString("is_tip")));
//							agent.setPayType(item.getInt("pay_type"));
//							agent.setPrice(item.getString("price"));
////							agent.setBargainPriceDes(item.getString("special_desc"));
//							
//							agents.add(agent);
//							
//						}
//						mAgentsAdapter.swapData(agents);
//						setListViewHeightBasedOnChildren(mAgentList);
//					}else if(status == 4){
//						ArrayList<GAgent> agents = new ArrayList<GAgent>();
//						mAgentsAdapter.swapData(agents);
//						setListViewHeightBasedOnChildren(mAgentList);
//					}
//					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
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
					
					GCourt court = new GCourt();
					court.setId(data.getString("court_id"));
					court.setName(data.getString("name"));
					court.setAddress(data.getString("addr"));
					court.setModel(data.getString("model"));
					court.setCreateYear(data.getString("create_year"));
					court.setArea(data.getString("area"));
					court.setGreenGrass(data.getString("green_grass"));
					court.setCourtData(data.getString("court_data"));
					court.setDesigner(data.getString("designer"));
					court.setFairwayLength(data.getString("fairway_length"));
					court.setFairwayGrass(data.getString("fairway_grass"));
					court.setPhone(data.getString("phone"));
					court.setRemark(data.getString("remark"));
					court.setFacilities(data.getString("facilities"));

					JSONArray fi = data.getJSONArray("fairway_imgs");
					
					for(int fi_i=0, fi_length=fi.length(); fi_i<fi_length; fi_i++){
						
						court.addFairwayImg((String) fi.get(fi_i));
					}
					
					
					JSONArray ci = data.getJSONArray("court_imgs");
					for(int ci_i=0, ci_length=ci.length(); ci_i<ci_length; ci_i++){
						
						court.addCourtImg((String) ci.get(ci_i));
					}
					
					GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
					a.setCourt(court);
					
					System.out.println(court.toString());
					mCourtAddr.setText(court.getAddress());
					mCourtBrief.setText(court.getRemark());
					
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
					
					mAgentTime.setText(String.format("%d点%d分", _hour, _minute));
					
					quertAgents(_courtId);
				}
			}, _hour, _minute, false);
			
			timePickerDialog12h.show(getFragmentManager(), "SelectTime");
			
			break;
		case R.id.court_addr:
		case R.id.court_addr_triangle:
			
			
			
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
