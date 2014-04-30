package com.jason.golf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GCourt;
import com.jason.golf.classes.SearchCourtBean;
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

public class GCourtListActivity extends ActionBarActivity implements
		OnItemClickListener, android.support.v7.app.ActionBar.TabListener {

	public static final String ARG_CITY = "SEARCH_CITY";
	public static final String ARG_DATE = "SEARCH_DATE";
	public static final String ARG_TIME = "SEARCH_TIME";
	public static final String ARG_KEYWORD = "SEARCH_KEYWORD";

	private ListView mCourt;
	private SearchCourtListAdapter mAdapter;
	private ArrayList<SearchCourtBean> _courts;
	
	private String _date ;
	private String _time ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_court_list);
		_courts = new ArrayList<SearchCourtBean>();
		
//		_courts.add(new GCourt("1001", "青之鸟高尔夫", "青岛", 34000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1002", "青之鸟高尔夫", "青岛", 35000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1003", "青之鸟高尔夫", "青岛", 36000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1004", "青之鸟高尔夫", "青岛", 37000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1005", "青之鸟高尔夫", "青岛", 58000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1006", "青之鸟高尔夫", "青岛", 39000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1007", "青之鸟高尔夫", "青岛", 40000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));
//		_courts.add(new GCourt("1008", "青之鸟高尔夫", "青岛", 41000, "0", "描述", "青岛",
//				"2010", "果岭", "老苏", "1024", "Grass", "13305311011", "remark",
//				"设施", "data"));

		mAdapter = new SearchCourtListAdapter(this, _courts);

		mCourt = (ListView) findViewById(R.id.court_list);
		mCourt.setAdapter(mAdapter);
		mCourt.setOnItemClickListener(this);

		Bundle args = getIntent().getExtras();

		// 设定搜索城市
		String city_id = args.getString(ARG_CITY);
		if (TextUtils.isEmpty(city_id))
			city_id = "370200";

		// 设定搜索日期
		String date = args.getString(ARG_DATE);
		_date = date;

		// 设定搜索时间
		String time = args.getString(ARG_TIME);
		_time = time;
		
		// 设定搜索关键字
		String keyword = args.getString(ARG_KEYWORD);

		JSONObject params = new JSONObject();
		try {
			params.put("cmd", "court/search");
			params.put("city", city_id);
			params.put("date", date);
			params.put("time", time);
			params.put("key_word", keyword);
			params.put("long_lat", "");
			params.put("order_key_word", "");
			params.put("_pg_", "");
			params.put("order_type", "");

			HttpRequest request = new HttpRequest(this, params,
					new HttpCallback() {

						@Override
						public void sucessData(String res) {
							// TODO Auto-generated method stub
							try {
								JSONArray data = new JSONArray(res);
								
								for(int i=0, length=data.length(); i<length;i++){
									
									JSONObject item = data.getJSONObject(i);
									SearchCourtBean bean = new SearchCourtBean();
									bean.setId(item.getString("court_id"));
									bean.setName(item.getString("name"));
									bean.setAddr(item.getString("addr"));
									bean.setDistance(item.getString("distance"));
									bean.setPrice(item.getInt("price"));
									bean.setIcoImgUrl(item.getString("ico_img"));
									bean.setPayType(item.getString("pay_type"));
//									bean.setIsOfficial(item.getString("is_official"));
									_courts.add(bean);
								}
								
								mAdapter.swapData(_courts);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							super.sucessData(res);
							
//							try {
//								JSONObject resObj = new JSONObject(res);
//								
//								int status = resObj.getInt("status");
//								
//								if(status == 0){
//								
//									JSONArray data = resObj.getJSONArray("data");
//									for(int i=0, length=data.length(); i<length;i++){
//										
//										JSONObject item = data.getJSONObject(i);
//										SearchCourtBean bean = new SearchCourtBean();
//										bean.setId(item.getString("court_id"));
//										bean.setName(item.getString("name"));
//										bean.setAddr(item.getString("addr"));
//										bean.setDistance(item.getString("distance"));
//										bean.setPrice(item.getInt("price"));
//										bean.setIcoImgUrl(item.getString("ico_img"));
//										bean.setPayType(item.getString("pay_type"));
////										bean.setIsOfficial(item.getString("is_official"));
//										_courts.add(bean);
//									}
//									
//									mAdapter.swapData(_courts);
//									
//								}
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							
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

		bar.addTab(bar.newTab().setText("默认排序").setTag("default")
				.setTabListener(this));

		bar.addTab(bar.newTab().setText("价格排序").setTag("price")
				.setTabListener(this));

		bar.addTab(bar.newTab().setText("距离排序").setTag("distance")
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Adapter a = parent.getAdapter();
		SearchCourtBean court = (SearchCourtBean) a.getItem(position);

		Intent it = new Intent(this, GCourtInfoActivity.class);
		Bundle param = new Bundle();
		param.putString(GCourtInfoActivity.KEY_COURT_ID, court.getId());
		param.putString(GCourtInfoActivity.KEY_DATE, _date);
		param.putString(GCourtInfoActivity.KEY_TIME, _time);
		it.putExtras(param);
		startActivity(it);

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		ArrayList<SearchCourtBean> array = new ArrayList<SearchCourtBean>(_courts);

		if ("price".equals(tab.getTag())) {
			Collections.sort(array, priceComparator);
			mAdapter.swapData(array);
		} else if ("distance".equals(tab.getTag())) {
			Collections.sort(array, distanceComparator);
			mAdapter.swapData(array);
		} else {
			mAdapter.swapData(_courts);
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	private Comparator<SearchCourtBean> priceComparator = new Comparator<SearchCourtBean>() {

		@Override
		public int compare(SearchCourtBean lhs, SearchCourtBean rhs) {
			// TODO Auto-generated method stub
			return lhs.getPrice() - rhs.getPrice();
		}
	};

	private Comparator<SearchCourtBean> distanceComparator = new Comparator<SearchCourtBean>() {

		@Override
		public int compare(SearchCourtBean lhs, SearchCourtBean rhs) {
			// TODO Auto-generated method stub
			return (int) (lhs.getDistance() - rhs.getDistance());
		}
	};

}
