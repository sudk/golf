package com.jason.golf.adapters;

import com.jason.golf.R;
import com.jason.golf.R.drawable;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class OtherGridAdapter extends BaseAdapter {
	
	
	private int[] Drawables = new int[]{
			R.drawable.grid_court,
			R.drawable.grid_trip,
			R.drawable.grid_competition,
			
			R.drawable.grid_handicap,
			R.drawable.grid_card,
			R.drawable.grid_ranklist,
			
			R.drawable.grid_sales,
			R.drawable.grid_merchant,
			
			R.drawable.grid_grade
			
	};
	
	private Context mContext;
	private int width;
	
	public OtherGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		
		DisplayMetrics m = mContext.getResources().getDisplayMetrics();
		int widthPixels = m.widthPixels;
		width = (widthPixels ) / 3 - 2 * 2 ; 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Drawables.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Drawables[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Drawables[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView img = new ImageView(mContext);
		img.setLayoutParams(new LayoutParams(width, width));
		img.setImageResource(Drawables[position]);
		return img;
	}

}
