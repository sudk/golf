package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GGood;
import com.jason.golf.classes.GOrder;
import com.jason.golf.classes.GTrip;
import com.jason.golf.classes.GoodsAdapter;
import com.jason.golf.classes.OrderAdapter;
import com.jason.golf.classes.TripsAdapter;
import com.jason.golf.provider.GolfProviderConfig;
import com.jsaon.golf.R;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GFleeMarketListFragment extends Fragment implements OnItemClickListener {
	
	private static final int REQUESTCODE = 1001;

	private ArrayList<GGood> _goods;
	private PullToRefreshListView mOrders;
	private GoodsAdapter mAdapter;
	
	private String _cityId;

	private int _page = 0;

	public static GFleeMarketListFragment Instance() {
		return new GFleeMarketListFragment();
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
		setHasOptionsMenu(true);
		_goods = new ArrayList<GGood>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_flee_market_list, null);
		mOrders = (PullToRefreshListView) v.findViewById(R.id.flee_list);
		mOrders.getRefreshableView().setOnItemClickListener(this);
		mOrders.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryGoods(0, true);
			}
		});

		mOrders.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				queryGoods(_page, false);
			}
		});

		mAdapter = new GoodsAdapter(getActivity(), _goods);
		mOrders.setAdapter(mAdapter);
		
		queryGoods(0, true);

		return v;
	}
	
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		inflater.inflate(R.menu.trip_list_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		System.out.println("onOptionsItemSelected" + item.getItemId());
		
		switch(item.getItemId()){
		case R.id.menu_getlocation:
			startActivityForResult(new Intent(getActivity(), SelectCityActivity.class), REQUESTCODE);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode != Activity.RESULT_OK) return;
		
		if (data == null) return;

		long cityRowId = data.getLongExtra("RowID", 0);

		System.out.println(String.format("requestCode = %d, RowId = %d", requestCode, cityRowId));

		ContentResolver cr = getActivity().getContentResolver();

		Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
				GolfProviderConfig.City._ID + "=? ", new String[] { ""	+ cityRowId }, null);

		if (c == null)
			return;

		try {
			if (c.moveToFirst()) {
				String city = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY));
				_cityId = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY_ID));
				ActionBarActivity activity = (ActionBarActivity) getActivity();
				ActionBar bar = activity.getSupportActionBar();
				bar.setTitle(R.string.trip);
				bar.setTitle(String.format("%s - %s",getString(R.string.trip), city));
			}
		} finally {
			c.close();
		}
		
		queryGoods(0, true);
		
	}

	private void queryGoods(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		GAccount acc = app.getAccount();
		acc.getCity();
		
		String city_id = "";

		if (TextUtils.isEmpty(_cityId)) {

			String city = acc.getCity();

			ContentResolver cr = getActivity().getContentResolver();

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
		

		try {
			
			params.put("cmd", "trip/list");
			params.put("start_date", "");
			params.put("end_date", "");
			params.put("trip_name", "");
			params.put("city", city_id);
			params.put("_pg_", String.format("%d", page));

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
						mOrders.onRefreshComplete();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						
						super.sucessData(res);

						try {
							JSONArray data = new JSONArray(res);

							_goods.clear();

							for (int i = 0, length = data.length(); i < length; i++) {

								JSONObject item = data.getJSONObject(i);
								GGood good = new GGood();
								good.initialize(item);
								_goods.add(good);
								
							}

							if (isRefresh) {
								mAdapter.swapData(_goods);
								_page = 0;
							} else {
								mAdapter.addData(_goods);
							}
							_page++;

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						super.faildData(code, res);

						if (code == 4) {
							// 没有数据

						}

					}

				});

		GThreadExecutor.execute(r);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub
//		System.out.println(String.format("postion is %d", position));
//		GOrder order = (GOrder) mAdapter.getItem(position - 1);
//
//		Bundle params = new Bundle();
//		params.putString(GOrderDetailsFragment.KEY_ORDER_ID, order.getOrderId());
//
//		Fragment detailFragment = GOrderDetailsFragment.Instance();
//		detailFragment.setArguments(params);
//
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.container, detailFragment);
//		transaction.addToBackStack(null);
//		// Commit the transaction
//		transaction.commit();
	}
}
