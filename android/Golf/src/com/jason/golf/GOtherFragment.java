package com.jason.golf;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.AdvertisementAdapter;
import com.jason.golf.adapters.OtherGridAdapter;
import com.jason.golf.classes.GAdver;
import com.jason.golf.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

public class GOtherFragment extends Fragment implements OnItemClickListener {
	
	private ViewPager mViewPager;
	private LinearLayout mDotContainer;
	private ArrayList<ImageView> mDots;
	private AdvertisementAdapter mAdapter;

	private ArrayList<GAdver> _advers;
	
	private Handler mHandler;
	
	private int mViewPagerCurrentPosition;
	
	private GridView mGrid;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mViewPagerCurrentPosition = 0;
		
		_advers = new ArrayList<GAdver>();
		
		queryAders();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_other, null);
		
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		ActionBar bar = activity.getSupportActionBar();
		bar.setTitle(R.string.find);
		
		//初始化导航圆点
		mDotContainer = (LinearLayout) v.findViewById(R.id.dotcontainer);
		initilizeNavigationDots();
		
		//初始化广告栏
		mViewPager = (ViewPager) v.findViewById(R.id.advertisements);
		mAdapter = new AdvertisementAdapter(getActivity(),_advers);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mViewPagerCurrentPosition = position;
				for (int i = 0; i < mDots.size(); i++) {
					ImageView iv = mDots.get(i);
					if ( i == position) {
						iv.setImageResource(R.drawable.page_indicator_focused);
					}else{
						iv.setImageResource(R.drawable.page_indicator_unfocused);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
		mHandler = new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					int count = mAdapter.getCount();
					if (count > 1){
						mViewPager.setCurrentItem(( ++ mViewPagerCurrentPosition ) % count, true);
					}
				}
				mHandler.removeMessages(1);
				mHandler.sendEmptyMessageDelayed(1,	5000);
				return false;
			}
		});
		mHandler.sendEmptyMessageDelayed(1, 5000);
		
		
		mGrid = (GridView) v.findViewById(R.id.other_grid);
		mGrid.setAdapter(new OtherGridAdapter(getActivity()));
		mGrid.setOnItemClickListener(this);
		
		return v;
	}
	
	private void queryAders() {
		// TODO Auto-generated method stub
		
		JSONObject p = new JSONObject();
		try {
			p.put("type", "1");
			p.put("cmd",  "adv/list");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		Runnable r = new HttpRequest(getActivity(), p, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				try {
					JSONArray data = new JSONArray(res);
					
					_advers.clear();
					
					for(int i=0, length=data.length(); i<length; i++){
						
						JSONObject adver = data.getJSONObject(i);
						
						GAdver a = new GAdver();
						
						if(a.initialize(adver)){
							_advers.add(a);
						}
						
					}
					
					mAdapter.swapData(_advers);
					
					initilizeNavigationDots();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
			}
			
		});
		
		GThreadExecutor.execute(r);
	}

	private void initilizeNavigationDots(){
		
		if(mDots == null) 
			mDots = new ArrayList<ImageView>();
		else
			mDots.clear();
		
		for (int i = 0; i < _advers.size(); i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			//设置每个小圆点距离左边的间距
			params.setMargins(10, 10, 10, 10);
			ImageView imageView = new ImageView(getActivity());
			imageView.setLayoutParams(params);
			
			if (i == 0) {
				// 默认选中第一张图片
				imageView.setImageResource(R.drawable.page_indicator_focused);
			} else {
				//其他图片都设置未选中状态
				imageView.setImageResource(R.drawable.page_indicator_unfocused);
			}
			mDots.add(imageView);
			mDotContainer.addView(imageView);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub
		switch(position){
		case 0:
			Intent preferentialIntent = new Intent(getActivity(), GPreferentialActivity.class);
			startActivity(preferentialIntent);
			break;
		case 1:
			Intent tripIntent = new Intent(getActivity(), GTripsActivity.class);
			Bundle tripParams = new Bundle();
			tripParams.putInt(GTripsActivity.FRAGMENT_MARK, GTripsActivity.FRAGMENT_MARK_TRIP_LIST);
			tripIntent.putExtras(tripParams);
			startActivity(tripIntent);
			break;
		case 2:
			Intent fleeIntent = new Intent(getActivity(), GFleeMarketActivity.class);
			Bundle fleeParams = new Bundle();
			fleeParams.putInt(GFleeMarketActivity.FRAGMENT_MARK, GFleeMarketActivity.FRAGMENT_MARK_FLEE_LIST);
			fleeIntent.putExtras(fleeParams);
			startActivity(fleeIntent);
			break;
		case 3:
			break;
		case 4:
			Intent competitionIntent = new Intent(getActivity(), GCompetitionActivity.class);
			Bundle params = new Bundle();
			params.putInt(GCompetitionActivity.FRAGMENT_MARK, GCompetitionActivity.FRAGMENT_MARK_LIST_COMPETITION);
			competitionIntent.putExtras(params);
			startActivity(competitionIntent);
			break;
		case 5:

			Intent merchantIntent = new Intent(getActivity(), GMerchantActivity.class);
			Bundle merParams = new Bundle();
			merParams.putInt(GMerchantActivity.FRAGMENT_MARK, GMerchantActivity.MERCHANT_LIST);
			merchantIntent.putExtras(merParams);
			startActivity(merchantIntent);
			break;
		case 6:
			MainActivity a = (MainActivity) getActivity();
			a.selectCourtSearch();
			
			break;
		}
	}
	
}
