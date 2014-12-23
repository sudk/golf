package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.CourtBeanAdapter;
import com.jason.golf.classes.GCourtBean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class SearchCourtNameActivity extends ActionBarActivity implements
		TextWatcher, OnClickListener, OnItemClickListener {

	private EditText mKeyWord;
	private ImageView mClear;

	private ListView mCourtList;

	private ArrayList<GCourtBean> _courts;

	private CourtBeanAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_court_name);
		_courts = new ArrayList<GCourtBean>();
		mAdapter = new CourtBeanAdapter(this, _courts);
		mKeyWord = (EditText) findViewById(R.id.key_court);
		mKeyWord.addTextChangedListener(this);
		mClear = (ImageView) findViewById(R.id.clear_courtname);
		mClear.setOnClickListener(this);
		mCourtList = (ListView) findViewById(R.id.court_list);
		mCourtList.setAdapter(mAdapter);
		mCourtList.setOnItemClickListener(this);
	}

	private void queryCourtNames(String name) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "court/list");
			params.put("_pg_", "0");
			params.put("name", name);
			params.put("city", "");
			params.put("court_id", "");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(this, params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);

				try {
					JSONArray data = new JSONArray(res);

					_courts.clear();

					for (int i = 0, length = data.length(); i < length; i++) {

						JSONObject item = data.getJSONObject(i);
						GCourtBean record = new GCourtBean();
						record.initialize(item);
						_courts.add(record);

					}

					mAdapter.swapData(_courts);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		GThreadExecutor.execute(r);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.clear_courtname:
			mKeyWord.setText("");
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		GCourtBean bean = (GCourtBean) mAdapter.getItem(position);

		Intent data = new Intent();
		data.putExtra("CourtName", bean.getName());
		data.putExtra("CourtId", bean.getCourtId());
		data.putExtra("CityId", bean.getCity());

		setResult(RESULT_OK, data);

		finish();

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (s.length() == 0) {
			return;
		} else {
			queryCourtNames(s.toString());
		}

	}

}
