package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.PreferentialListAdapter;
import com.jason.golf.adapters.SearchCourtListAdapter;
import com.jason.golf.classes.PreferentialBean;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.SearchCourtBean;
import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GPreferentialActivity extends ActionBarActivity implements
		OnItemClickListener, TabListener {

	private static final int REQUESTCODE = 1001;
	
	private ListView mPreferential;
	private PreferentialListAdapter mAdapter;
	private ArrayList<PreferentialBean> _courts;

	private String _cityId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferential_list);

		_cityId = "";

		_courts = new ArrayList<PreferentialBean>();

		mAdapter = new PreferentialListAdapter(this, _courts);

		mPreferential = (ListView) findViewById(R.id.preferential_list);
		mPreferential.setAdapter(mAdapter);
		mPreferential.setOnItemClickListener(this);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.preferential);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
		
		initialTabs();

	}
	
	

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		finish();
		return super.onSupportNavigateUp();
	}



	@Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.preferential_menu, menu);  
        return super.onCreateOptionsMenu(menu);  
    }  
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		System.out.println("onOptionsItemSelected" + item.getItemId());
		
		switch(item.getItemId()){
		case R.id.menu_getlocation:
			startActivityForResult(new Intent(this, SelectCityActivity.class), REQUESTCODE);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void initialTabs() {
		final ActionBar bar = getSupportActionBar();

		if (bar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}

		bar.addTab(bar.newTab().setText("周一").setTag("1").setTabListener(this));

		bar.addTab(bar.newTab().setText("周二").setTag("2").setTabListener(this));

		bar.addTab(bar.newTab().setText("周三").setTag("3").setTabListener(this));

		bar.addTab(bar.newTab().setText("周四").setTag("4").setTabListener(this));

		bar.addTab(bar.newTab().setText("周五").setTag("5").setTabListener(this));

		bar.addTab(bar.newTab().setText("周六").setTag("6").setTabListener(this));

		bar.addTab(bar.newTab().setText("周日").setTag("0").setTabListener(this));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub

		Adapter a = parent.getAdapter();
		PreferentialBean court = (PreferentialBean) a.getItem(position);

		Intent it = new Intent(this, GCourtInfoActivity.class);
		Bundle param = new Bundle();
		param.putString(GCourtInfoActivity.KEY_COURT_ID, court.getId());
		param.putString(GCourtInfoActivity.KEY_DATE, court.getDate());
		param.putString(GCourtInfoActivity.KEY_TIME, court.getStartTime());
		it.putExtras(param);
		startActivity(it);
		
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		queryPreferentials((String) arg0.getTag());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode != RESULT_OK) return;
		
		if (data == null) return;

		long cityRowId = data.getLongExtra("RowID", 0);

		System.out.println(String.format("requestCode = %d, RowId = %d", requestCode, cityRowId));

		ContentResolver cr = getContentResolver();

		Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
				GolfProviderConfig.City._ID + "=? ", new String[] { ""	+ cityRowId }, null);

		if (c == null)
			return;

		try {
			if (c.moveToFirst()) {
				String city = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY));
				_cityId = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY_ID));
				getSupportActionBar().setTitle(String.format("%s - %s",getString(R.string.preferential), city));
			}
		} finally {
			c.close();
		}
		
		queryPreferentials((String) (getSupportActionBar().getSelectedTab().getTag()));
		
	}

	private void queryPreferentials(String week) {
		// TODO Auto-generated method stub
		GolfAppliaction app = (GolfAppliaction) getApplication();
		GAccount acc = app.getAccount();

		String city_id = "";

		if (TextUtils.isEmpty(_cityId)) {

			String city = acc.getCity();

			ContentResolver cr = getContentResolver();

			Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
					GolfProviderConfig.City.CITY + "=? ", new String[] { ""	+ city }, null);

			if (c == null)
				return;

			try {
				if (c.moveToFirst()) {
					city_id = c.getString(c
							.getColumnIndex(GolfProviderConfig.City.CITY_ID));
				}
			} finally {
				c.close();
			}

			if (TextUtils.isEmpty(city_id)) {
				city_id = "370200";
			}

		} else {

			city_id = _cityId;

		}

		JSONObject params = new JSONObject();
		try {
			params.put("cmd", "sale/search");
			params.put("city", city_id);
			params.put("day", week);
			params.put("court_id", "");

			String long_lat = "";
			if (!(Math.abs(acc.getLatitude() - 0) < 0.00000001)
					&& !(Math.abs(acc.getLongitude() - 0) < 0.00000001)) {
				long_lat = String.format("%f,%f", acc.getLongitude(),
						acc.getLatitude());
			}
			params.put("long_lat", long_lat);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest request = new HttpRequest(this, params, new HttpCallback() {
			
			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				
				if(code == 4){
					_courts.clear();
					mAdapter.swapData(_courts);
				}
				
			}

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				try {
					JSONArray data = new JSONArray(res);
					
					_courts.clear();

					for (int i = 0, length = data.length(); i < length; i++) {

						JSONObject item = data.getJSONObject(i);
						PreferentialBean bean = new PreferentialBean();
						
							bean.setId(item.getString("court_id"));
							bean.setName(item.getString("name"));
							bean.setAddr(item.getString("addr"));
							bean.setDistance(item.getString("distance"));
							bean.setPrice(item.getInt("price"));
							bean.setIcoImgUrl(item.getString("ico_img"));
							bean.setPayType(item.getString("pay_type"));
							bean.setDate(item.getString("date"));
//							bean.setTime(item.getString("start_time"));
							bean.setStartTime(item.getString("start_time"));
							bean.setEndTime(item.getString("end_time"));
							
							_courts.add(bean);
						
						_courts.add(bean);
					}

					mAdapter.swapData(_courts);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.sucessData(res);
			}

		});

		GThreadExecutor.execute(request);
	}

}
