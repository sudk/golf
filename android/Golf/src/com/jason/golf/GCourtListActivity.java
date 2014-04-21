package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.RunnabLogin;
import com.jason.golf.classes.GCourt;
import com.jason.golf.classes.SearchCourtListAdapter;
import com.jsaon.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GCourtListActivity extends ActionBarActivity implements OnItemClickListener, android.support.v7.app.ActionBar.TabListener {
	
	public static final String ARG_CITY = "SEARCH_CITY";
	public static final String ARG_DATE = "SEARCH_DATE";
	public static final String ARG_TIME = "SEARCH_TIME";
	public static final String ARG_KEYWORD = "SEARCH_KEYWORD";
	
	private ListView mCourt;
	private SearchCourtListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_court_list);
		
		mAdapter = new SearchCourtListAdapter(this, null);
		
		mCourt = (ListView) findViewById(R.id.court_list);
		mCourt.setAdapter(mAdapter);
		mCourt.setOnItemClickListener(this);
		
		Bundle args = getIntent().getExtras();
		
		//设定搜索城市
		String city_id = args.getString(ARG_CITY);
		if(TextUtils.isEmpty(city_id)) city_id = "370200";
		
		//设定搜索日期
		String date = args.getString(ARG_DATE);
		
		//设定搜索时间
		String time = args.getString(ARG_TIME);
		
		//设定搜索关键字
		String keyword = args.getString(ARG_KEYWORD);
		
		
		JSONObject params = new JSONObject();
		try {
			params.put("cmd",  "court/search");
			params.put("city", city_id);
			params.put("date", date);
			params.put("time", time);
			params.put("key_word", keyword);
			params.put("long_lat", "");
			params.put("order_key_word", "");
			params.put("_pg_", "");
			params.put("order_type", "");
			
			RunnabLogin request = new RunnabLogin(this, params, new HttpCallback() {

				@Override
				public void sucess(String res) {
					// TODO Auto-generated method stub
					System.out.println(res);
				}
				
			});
			
			GThreadExecutor.execute(request);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initialTabs();

	}
	
	public void initialTabs() {
        final ActionBar bar = getSupportActionBar();
        
        if (bar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
        	bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
        
        bar.addTab(bar.newTab()
                .setText("默认排序")
                .setTabListener(this));
        
        bar.addTab(bar.newTab()
                .setText("价格排序")
                .setTabListener(this));
        
        bar.addTab(bar.newTab()
                .setText("距离排序")
                .setTabListener(this));
    }
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		Adapter a = parent.getAdapter();
		GCourt court = (GCourt) a.getItem(position);
		
		Intent it = new Intent(this, GCourtInfoActivity.class);
		
		startActivity(it);
		
		
		
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
