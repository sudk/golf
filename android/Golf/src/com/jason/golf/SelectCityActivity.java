package com.jason.golf;

import com.jason.golf.adapters.CityAdapter;
import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class SelectCityActivity extends ActionBarActivity implements OnChildClickListener {
	
	ExpandableListView mCitys;
	
	CityAdapter _adapter;
	
	int lastExpandGroupPosition;
	int currentExpandGroupPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		lastExpandGroupPosition = -1;
		
		setContentView(R.layout.activity_selectcity);
		
		mCitys = (ExpandableListView) findViewById(R.id.citys);
		
		Cursor provinceCursor = getContentResolver().query(GolfProviderConfig.Province.CONTENT_URI, null, null, null, null);
		
		_adapter = new CityAdapter(provinceCursor, getApplication());
		
		mCitys.setAdapter(_adapter);
		
		mCitys.setOnChildClickListener(this);
//		mCitys.setOnGroupExpandListener(new OnGroupExpandListener() {
//			
//			@Override
//			public void onGroupExpand(int groupPosition) {
//				// TODO Auto-generated method stub
//				
//				for(int i=0, length=_adapter.getGroupCount();i<length;i++){
//					
//					if(i!=groupPosition){
//						mCitys.collapseGroup(i);
//					}
//					
//				}
//				
//				mCitys.setSelectedGroup(groupPosition);
//			}
//		});
		
		ActionBar bar = getSupportActionBar();
		bar.setIcon(R.drawable.actionbar_icon);
		
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		System.out.println(String.format("%d  %d  %d", groupPosition, childPosition, id));
		
		Intent data = new Intent();
		data.putExtra("RowID", id);
		setResult(RESULT_OK, data);
		finish();
		
		return true;
	}
//	 listView.setSelectedGroup(groupPosition);   
	
	
	
}
