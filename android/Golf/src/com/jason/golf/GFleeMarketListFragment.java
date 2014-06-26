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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GFleeMarketListFragment extends Fragment implements OnItemClickListener {
	
	private static final int REQUESTCODE = 1001;

	private ArrayList<GGood> _goods;
	private PullToRefreshListView mGoods;
	private GoodsAdapter mAdapter;
	
	private String _cityId;

	private int _page = 0;
	
	private String _title;

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
		mGoods = (PullToRefreshListView) v.findViewById(R.id.goods_list);
		mGoods.getRefreshableView().setOnItemClickListener(this);
		mGoods.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryGoods(0, true);
			}
		});

		mGoods.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				_title = "";
				queryGoods(_page, false);
			}
		});

		mAdapter = new GoodsAdapter(getActivity(), _goods);
		mGoods.setAdapter(mAdapter);
		
		queryGoods(0, true);

		return v;
	}
	
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		
		 inflater.inflate(R.menu.flee_list_menu,  menu);
	        
	        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
	        searchView.setOnQueryTextListener(mOnQueryTextListener);
	        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
				
				@Override
				public boolean onClose() {
					// TODO Auto-generated method stub
					queryGoods(0, true);
					return false;
				}
			});
	        searchView.setQueryHint("输入关键字");
	        
	        AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
	        search_text.setTextColor(Color.WHITE);
	        
	        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
	        searchButton.setImageResource(R.drawable.ic_search);
//	        ImageView searchMagButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
//	        searchMagButton.setImageResource(R.drawable.ic_search);
	        ImageView searchCloseButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
	        searchCloseButton.setImageResource(R.drawable.ic_close);
	        
			super.onCreateOptionsMenu(menu, inflater);
		
	}
	
	 // The following callbacks are called for the SearchView.OnQueryChangeListener
    // For more about using SearchView, see src/.../view/SearchView1.java and SearchView2.java
    private final SearchView.OnQueryTextListener mOnQueryTextListener =
            new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
        	_title = query;
        	queryGoods(0, true);
            return true;
        }
    };

	
	
	
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
			
			params.put("cmd", "flea/list");
			params.put("title", _title);
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

		Bundle params = new Bundle();
		params.putString(GFleeMarketInfoFragment.KEY_GOOD_ID, good.getId());
		params.putBoolean(GFleeMarketInfoFragment.KEY_ENABLE_EDIT, false);

		Fragment detailFragment = GFleeMarketInfoFragment.Instance();
		detailFragment.setArguments(params);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, detailFragment);
		transaction.addToBackStack(null);
		
		// Commit the transaction
		transaction.commit();
	}
}
