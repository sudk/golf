package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.AgentsAdapter;
import com.jason.golf.classes.GAgent;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

public class GCourtInfoBriefFragment extends Fragment {
	
	private static final String KEY_COURT_ID = "court_id";
	public static final String KEY_DATE = "key_date";
	public static final String KEY_TIME = "key_time";
	
	private ListView mAgentList;
	private AgentsAdapter mAgentsAdapter;
	private ScrollView mSroller;
	
	private ArrayList<GAgent> _agents;
	
	private String _courtId, _date, _time;
	
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
		
//		agents.add(new GAgent("青之鸟高球生活", "18洞果岭/僮/车/柜", "620"));
//		agents.add(new GAgent("大连智远高尔夫", "18洞果岭/僮/车/柜", "540"));
//		agents.add(new GAgent("启程", "18洞果岭/僮/车/柜", "430"));
//		agents.add(new GAgent("上海狼鹰高尔夫", "18洞果岭/僮/车/柜", "800"));
//		agents.add(new GAgent("黑梯高尔夫", "18洞果岭/僮/车/柜", "700"));
//		agents.add(new GAgent("上海铁马高尔夫", "18洞果岭/僮/车/柜", "650"));
//		agents.add(new GAgent("行天下", "18洞果岭/僮/车/柜", "720"));
		
		_courtId = getArguments().getString(KEY_COURT_ID);
		_date = getArguments().getString(KEY_DATE);
		_time = getArguments().getString(KEY_TIME);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.fragment_court_info_brief, null);
		mSroller = (ScrollView) v.findViewById(R.id.scroller_solution);
		mAgentList = (ListView) v.findViewById(R.id.court_agents);
		mAgentsAdapter = new AgentsAdapter(this.getActivity(), _agents);
		mAgentList.setAdapter(mAgentsAdapter);
		setListViewHeightBasedOnChildren(mAgentList);
		
		quertAgents(_courtId, _date, _time);
		
		return v;
	}
	
	private void quertAgents(String c, String d, String t){
		
		JSONObject params = new JSONObject();
		
		try {
			params.put("cmd", "court/price");
			params.put("court_id", c);
			params.put("type", "");
			params.put("date_time", d+t);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void sucess(String res) {
				// TODO Auto-generated method stub
				
				try {
					JSONObject resObj = new JSONObject(res);
					
					int status = resObj.getInt("status");
					
					if( status == 0){
					
						JSONArray data = resObj.getJSONArray("data");
						
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
//							agent.setBargainPriceDes(item.getString("special_desc"));
							
							agents.add(agent);
							
						}
						
						mAgentsAdapter.swapData(agents);
						setListViewHeightBasedOnChildren(mAgentList);
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.sucess(res);
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

}
