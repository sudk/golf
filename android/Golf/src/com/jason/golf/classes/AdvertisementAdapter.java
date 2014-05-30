package com.jason.golf.classes;

import java.util.ArrayList;

import com.jason.golf.R;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class AdvertisementAdapter extends PagerAdapter {

	private ArrayList<View> _ListViews;

	private Context _context;
	
	private FinalBitmap _fb;


	public AdvertisementAdapter(Context ctx, ArrayList<GAdver> advers) {
		// TODO Auto-generated constructor stub
		_context = ctx;
//		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_ListViews = new ArrayList<View>();
		_fb = FinalBitmap.create(ctx);// 初始化FinalBitmap模块
		_fb.configLoadingImage(R.drawable.ic_launcher);
		// 这里可以进行其他十几项的配置，也可以不用配置，配置之后必须调用init()函数,才生效

		if (advers != null) {

			for (GAdver gAdver : advers) {
				ImageView iv = new ImageView(_context);
				final String url = gAdver.getLinkUrl();
				iv.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(Intent.ACTION_VIEW);
						it.setData(Uri.parse(url));
						_context.startActivity(it);
					}
				});
				
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				iv.setLayoutParams(param);
				iv.setScaleType(ScaleType.CENTER_CROP);
				_fb.display(iv, gAdver.getImgUrl());
				_ListViews.add(iv);
			}

		}

	}
	
	public void swapData(ArrayList<GAdver> advers){
		
		_ListViews.clear();
		
		if (advers != null) {

			for (GAdver gAdver : advers) {
				ImageView iv = new ImageView(_context);
				final String url = gAdver.getLinkUrl();
				iv.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(Intent.ACTION_VIEW);
						it.setData(Uri.parse(url));
						_context.startActivity(it);
					}
				});
				
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				iv.setLayoutParams(param);
				iv.setScaleType(ScaleType.CENTER_CROP);
				_fb.display(iv, gAdver.getImgUrl());
				_ListViews.add(iv);
			}

		}
		
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _ListViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(_ListViews.get(position));  
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(_ListViews.get(position));  
//        weibo_button=(Button) findViewById(R.id.button1);//这个需要注意，我们是在重写adapter里面实例化button组件的，如果你在onCreate()方法里这样做会报错的。  
//        weibo_button.setOnClickListener(new OnClickListener() {  
//              
//            public void onClick(View v) {  
//                intent=new Intent(ViewPagerDemo.this,WeiBoActivity.class);  
//                startActivity(intent);  
//            }  
//        });  
        return _ListViews.get(position); 
	}

}
