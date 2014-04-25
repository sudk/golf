package com.jason.golf;

import java.util.ArrayList;

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
	
	private ListView mAgentList;
	private AgentsAdapter mAgentsAdapter;
	private ScrollView mSroller;
	
	private ArrayList<GAgent> agents;
	
	public static GCourtInfoBriefFragment Instance(){
		GCourtInfoBriefFragment fragment = new GCourtInfoBriefFragment();
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		agents = new ArrayList<GAgent>();
		
		agents.add(new GAgent("青之鸟高球生活", "18洞果岭/僮/车/柜", "620"));
		agents.add(new GAgent("大连智远高尔夫", "18洞果岭/僮/车/柜", "540"));
		agents.add(new GAgent("启程", "18洞果岭/僮/车/柜", "430"));
		agents.add(new GAgent("上海狼鹰高尔夫", "18洞果岭/僮/车/柜", "800"));
		agents.add(new GAgent("黑梯高尔夫", "18洞果岭/僮/车/柜", "700"));
		agents.add(new GAgent("上海铁马高尔夫", "18洞果岭/僮/车/柜", "650"));
		agents.add(new GAgent("行天下", "18洞果岭/僮/车/柜", "720"));
		
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_court_info_brief, null);
		mSroller = (ScrollView) v.findViewById(R.id.scroller_solution);
		mAgentList = (ListView) v.findViewById(R.id.court_agents);
		mAgentsAdapter = new AgentsAdapter(this.getActivity(), agents);
		mAgentList.setAdapter(mAgentsAdapter);
		setListViewHeightBasedOnChildren(mAgentList);
		
		
		return v;
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
