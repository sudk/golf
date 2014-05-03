package com.jason.golf;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.classes.GCourt;
import com.jsaon.golf.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class GCourtInfoImagesFragment extends Fragment {
	
	private ViewPager mImages;
	private PagerAdapter mAdapter;
	

	public static GCourtInfoImagesFragment Instance() {
		// TODO Auto-generated method stub
		return new GCourtInfoImagesFragment();
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
		hasOptionsMenu();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
		GCourt court = a.getCourt();
		
		mAdapter = new ImageAdapter(getActivity(), court.getFairwayImgs());
		
		View v = inflater.inflate(R.layout.fragment_court_info_imgs, null);
		mImages = (ViewPager) v.findViewById(R.id.fairway_imgs);
		mImages.setAdapter(mAdapter);

//		ActionBarActivity activity = (ActionBarActivity) getActivity();
//		ActionBar bar = activity.getSupportActionBar();
//		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
//	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
	    
		return v;
	}
	
	static class ImageAdapter extends PagerAdapter{
		
		Context _context;
		FinalBitmap _fb;
		ArrayList<View> _ListViews;
		
		public ImageAdapter(Context context, ArrayList<String> urls){
			_context = context;
			_fb = FinalBitmap.create(_context);
			_fb.configLoadingImage(R.drawable.test_golf);
			
			_ListViews = new ArrayList<View>();
			
			if(urls != null ){
				
				for (String url : urls) {
					
					ImageView image = new ImageView(_context);
					ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					image.setLayoutParams(params);
					_ListViews.add(image);
					_fb.display(image, url);
					
				}
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _ListViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}
		
		public void destroyItem(ViewGroup container, int position, Object object){
			container.removeView(_ListViews.get(position));
		}
		
		public Object instantiateItem (ViewGroup container, int position){
			container.addView(_ListViews.get(position)); 
			return _ListViews.get(position);
		}
	}


}
