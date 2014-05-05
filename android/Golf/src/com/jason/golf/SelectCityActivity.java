package com.jason.golf;

import com.jason.golf.classes.CityAdapter;
import com.jason.golf.provider.GolfProviderConfig;
import com.jsaon.golf.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class SelectCityActivity extends ActionBarActivity implements OnChildClickListener {
	
	ExpandableListView mCitys;
	
	CityAdapter _adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_selectcity);
		
		mCitys = (ExpandableListView) findViewById(R.id.citys);
		
		Cursor provinceCursor = getContentResolver().query(GolfProviderConfig.Province.CONTENT_URI, null, null, null, null);
		
		_adapter = new CityAdapter(provinceCursor, getApplication());
		
		mCitys.setAdapter(_adapter);
		
		mCitys.setOnChildClickListener(this);
		
		ActionBar bar = getSupportActionBar();
		bar.setIcon(R.drawable.actionbar_icon);
		
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		System.out.println(String.format("%d�� %d��%d", groupPosition, childPosition, id));
		
		Intent data = new Intent();
		data.putExtra("RowID", id);
		setResult(RESULT_OK, data);
		finish();
		
		return true;
	}
	
}
