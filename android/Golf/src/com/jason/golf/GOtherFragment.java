package com.jason.golf;


import java.util.ArrayList;

import com.jason.golf.classes.AdvertisementAdapter;
import com.jason.golf.classes.GAdver;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GOtherFragment extends Fragment implements OnClickListener {
	
	private ViewPager mViewPager;
	private LinearLayout mDotContainer;
	private ArrayList<ImageView> mDots;
	private PagerAdapter mAdapter;

	private GridLayout mButtonsGrid;
	
	private ArrayList<GAdver> _advers;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		_advers = new ArrayList<GAdver>();
		_advers.add(new GAdver(1, 1, "http://hearthstone.nos.netease.com/1/news/201404/2_cms_news_201404_j_041601_l_cn.jpg", "http://www.baidu.com", 0, 0));
		_advers.add(new GAdver(1, 1, "http://hearthstone.nos.netease.com/1/news/201404/2_cms_news_201404_t201404181.jpg", "http://www.163.com", 0, 0));
		_advers.add(new GAdver(1, 1, "http://hearthstone.nos.netease.com/1/2014042103.jpg", "http://www.sina.com", 0, 0));
		_advers.add(new GAdver(1, 1, "http://hearthstone.nos.netease.com/1/temp/jl_test/201312/2014041802.jpg", "http://9.163.com", 0, 0));
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_other, null);
		
		//初始化导航圆点
		mDotContainer = (LinearLayout) v.findViewById(R.id.dotcontainer);
		mDots = new ArrayList<ImageView>();
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
		
		//初始化广告栏
		mViewPager = (ViewPager) v.findViewById(R.id.advertisements);
		mAdapter = new AdvertisementAdapter(getActivity(),_advers);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
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
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
		mButtonsGrid = (GridLayout) v.findViewById(R.id.grid_buttons);
		
		DisplayMetrics m = getActivity().getResources().getDisplayMetrics();
		int widthPixels = m.widthPixels;
		int paddingLeft = mButtonsGrid.getPaddingLeft();
		int paddingRight = mButtonsGrid.getPaddingRight();
		int width = (widthPixels - paddingLeft - paddingRight) / mButtonsGrid.getColumnCount() - 2 * 2; 
		
		for(int i=0, length=mButtonsGrid.getChildCount(); i < length ; i++){
			View child = mButtonsGrid.getChildAt(i);
			LayoutParams params = child.getLayoutParams(); 
			params.height = width;
            params.width = width;
            child.setLayoutParams(params);
		}
		
		v.findViewById(R.id.grid_preference).setOnClickListener(this);
		v.findViewById(R.id.grid_combo).setOnClickListener(this);
		v.findViewById(R.id.grid_sale).setOnClickListener(this);
		v.findViewById(R.id.grid_ranking).setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.grid_preference:
			break;
		case R.id.grid_combo:
			break;
		case R.id.grid_sale:
			break;
		case R.id.grid_ranking:
			break;
		
		}
	}
	
}
