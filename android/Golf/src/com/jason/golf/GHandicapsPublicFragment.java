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
import com.jason.golf.adapters.AdvertisementAdapter;
import com.jason.golf.adapters.HandicapsAdapter;
import com.jason.golf.adapters.OtherGridAdapter;
import com.jason.golf.adapters.TransrecordAdapter;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GAdver;
import com.jason.golf.classes.GHandicapBean;
import com.jason.golf.classes.GTransrecord;
import com.jason.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GHandicapsPublicFragment extends Fragment {
	
	private ArrayList<GHandicapBean> _capbeans;
	private PullToRefreshListView mHandicaps;
	private HandicapsAdapter mAdapter;
	
	private int _page = 0;

	public static Fragment Instance() {
		return new GHandicapsPublicFragment();
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
		_capbeans = new ArrayList<GHandicapBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ActionBar bar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		bar.setTitle(R.string.ranking);
		
		View v = inflater.inflate(R.layout.fragment_handicap_public, null);
		mHandicaps = (PullToRefreshListView) v.findViewById(R.id.handicaps_list);
		mHandicaps.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryHandicaps(0, true);
			}
		});

		mHandicaps.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				queryHandicaps(_page, false);
			}
		});

		mAdapter = new HandicapsAdapter(getActivity(), _capbeans);
		mHandicaps.setAdapter(mAdapter);
		
		queryHandicaps(0, true);

		return v;
	}
	

	private void queryHandicaps(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		GAccount acc = app.getAccount();
		acc.getCity();
		
		try {
			params.put("cmd", "score/handicaps");
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
						mHandicaps.onRefreshComplete();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						
						super.sucessData(res);

						try {
							JSONArray data = new JSONArray(res);

							_capbeans.clear();

							for (int i = 0, length = data.length(); i < length; i++) {

								JSONObject item = data.getJSONObject(i);
								GHandicapBean record = new GHandicapBean();
								record.initialize(item);
								_capbeans.add(record);
								
							}

							if (isRefresh) {
								mAdapter.swapData(_capbeans);
								_page = 0;
							} else {
								mAdapter.addData(_capbeans);
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

}
