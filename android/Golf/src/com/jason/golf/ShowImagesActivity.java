package com.jason.golf;

import java.util.ArrayList;

import com.jason.golf.GFleeImagesFragment.ImageAdapter;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ShowImagesActivity extends ActionBarActivity {
	
	public static final String KEY_IMG_URL = "Key_Img_Url";
	public static final String KEY_IMG_POS = "Key_Img_Pos";
	
	private ViewPager mImages;
	private PagerAdapter mAdapter;
	private ArrayList<String> _imgUrl;
	private int mImgPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_imgs);
		
		_imgUrl = new ArrayList<String>();
		
		String[] urls = getIntent().getExtras().getStringArray(KEY_IMG_URL);
		
		
		for(int i=0,length=urls.length;i<length;i++){
			_imgUrl.add(urls[i]);
		}
		
		mAdapter = new ImageAdapter(this, _imgUrl);
		
		mImages = (ViewPager) findViewById(R.id.images);
		mImages.setAdapter(mAdapter);
		
		mImgPos = getIntent().getExtras().getInt(KEY_IMG_POS, 0);
		mImages.setCurrentItem(mImgPos, true);
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
