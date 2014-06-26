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
import com.jason.golf.adapters.GoodsAdapter;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GGood;
import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GFleeMarketMyGoodsFragment extends Fragment implements OnItemClickListener {
	
	private static final int REQUESTCODE = 1001;

	private ArrayList<GGood> _goods;
	private PullToRefreshListView mGoods;
	private GoodsAdapter mAdapter;
	
	private String _cityId;

	private int _page = 0;

	public static GFleeMarketMyGoodsFragment Instance() {
		return new GFleeMarketMyGoodsFragment();
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
		mGoods = (PullToRefreshListView) v.findViewById(R.id.goods_list);
		mGoods.getRefreshableView().setOnItemClickListener(this);
		mGoods.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryMyGoods(0, true);
			}
		});

		mGoods.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				queryMyGoods(_page, false);
			}
		});

		mAdapter = new GoodsAdapter(getActivity(), _goods);
		mGoods.setAdapter(mAdapter);
		
		queryMyGoods(0, true);

		return v;
	}
	
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		inflater.inflate(R.menu.my_flea_list_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		System.out.println("onOptionsItemSelected" + item.getItemId());
		
		switch(item.getItemId()){
		case R.id.menu_new_good:
			
			GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
			GAccount acc = app.getAccount();
			
			if(acc.isLogin()){
				
				Fragment newFragment = GFleeMarketNewFragment.Instance();
				
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				
				transaction.replace(R.id.container, newFragment);
				transaction.addToBackStack(null);
				
				// Commit the transaction
				transaction.commit();
				
			}else {
				
				Intent itLogin = new Intent(getActivity(), GAccountActivity.class);
				itLogin.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.LOGIN);
				startActivity(itLogin);
				
			}
			
			
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
		
		queryMyGoods(0, true);
		
	}

	private void queryMyGoods(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		GAccount acc = app.getAccount();
		acc.getCity();
		

		try {
			
			params.put("cmd", "flea/mylist");
			params.put("city", "");
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
						mGoods.onRefreshComplete();
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
		System.out.println(String.format("postion is %d", position));
		GGood good = (GGood) mAdapter.getItem(position - 1);
//
		Bundle params = new Bundle();
		params.putString(GFleeMarketInfoFragment.KEY_GOOD_ID, good.getId());
		params.putBoolean(GFleeMarketInfoFragment.KEY_ENABLE_EDIT, true);
//
		Fragment detailFragment = GFleeMarketInfoFragment.Instance();
		detailFragment.setArguments(params);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, detailFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}
}
