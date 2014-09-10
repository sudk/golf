package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.HandicapsAdapter;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GHandicapBean;
import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class GHandicapsPersonalFragment extends Fragment {
	
	private ArrayList<GHandicapBean> _capbeans;
	private ListView mHandicaps;
	private HandicapsAdapter mAdapter;
	
	public static Fragment Instance() {
		return new GHandicapsPersonalFragment();
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
		bar.setTitle(R.string.handicaps);
		
		View v = inflater.inflate(R.layout.fragment_handicap_personal, null);
		mHandicaps = (ListView) v.findViewById(R.id.handicaps_list);
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
			
			params.put("cmd", "score/phandicaps");

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

								mAdapter.swapData(_capbeans);

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
