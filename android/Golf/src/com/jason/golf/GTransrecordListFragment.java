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
import com.jason.golf.classes.GTransrecord;
import com.jason.golf.classes.TransrecordAdapter;
import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GTransrecordListFragment extends Fragment implements OnItemClickListener {
	
	private ArrayList<GTransrecord> _records;
	private PullToRefreshListView mRecords;
	private TransrecordAdapter mAdapter;
	
	private int _page = 0;

	public static GTransrecordListFragment Instance() {
		return new GTransrecordListFragment();
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
		_records = new ArrayList<GTransrecord>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_transrecord_list, null);
		mRecords = (PullToRefreshListView) v.findViewById(R.id.transrecord_list);
		mRecords.getRefreshableView().setOnItemClickListener(this);
		mRecords.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryTransrecords(0, true);
			}
		});

		mRecords.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				queryTransrecords(_page, false);
			}
		});

		mAdapter = new TransrecordAdapter(getActivity(), _records);
		mRecords.setAdapter(mAdapter);
		
		queryTransrecords(0, true);

		return v;
	}
	

	private void queryTransrecords(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
		GAccount acc = app.getAccount();
		acc.getCity();
		
		try {
			
			params.put("cmd", "transrecord/list");
			params.put("start_date","");
			params.put("end_date", "");
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
						mRecords.onRefreshComplete();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						
						super.sucessData(res);

						try {
							JSONArray data = new JSONArray(res);

							_records.clear();

							for (int i = 0, length = data.length(); i < length; i++) {

								JSONObject item = data.getJSONObject(i);
								GTransrecord record = new GTransrecord();
								record.initialize(item);
								_records.add(record);
								
							}

							if (isRefresh) {
								mAdapter.swapData(_records);
								_page = 0;
							} else {
								mAdapter.addData(_records);
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
		GTransrecord record = (GTransrecord) mAdapter.getItem(position - 1);

		Bundle params = new Bundle();
		params.putString(GFleeMarketInfoFragment.KEY_GOOD_ID, record.getId());
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
