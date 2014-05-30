package com.jason.golf.classes;

import java.util.ArrayList;
import java.util.zip.Inflater;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchCourtListAdapter extends BaseAdapter {
	
	private LayoutInflater _inflater ;
	private Context _context;
	private ArrayList<SearchCourtBean> _courts;
	
	private FinalBitmap _fb;

	public SearchCourtListAdapter(Context ctx, ArrayList<SearchCourtBean> courts) {
		super();
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_courts = new ArrayList<SearchCourtBean>();
		if(courts != null){
			_courts.addAll(courts);
		}
		
		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _courts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if( position >= getCount() || position < 0)
			return null;
		SearchCourtBean court = _courts.get(position);
		return court;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ViewHolder holder;
		if(convertView == null){
			v = _inflater.inflate(R.layout.activity_search_court_list_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) v.findViewById(R.id.court_list_image);
			holder.name = (TextView) v.findViewById(R.id.court_list_name);
			holder.distance = (TextView) v.findViewById(R.id.court_list_distance);
			holder.price = (TextView) v.findViewById(R.id.court_list_price);
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		SearchCourtBean court = _courts.get(position);
		
		holder.name.setText(court.getName());
		holder.price.setText(String.format("￥ %d", court.getPrice() / 100));
		 
		long distance = court.getDistance();
		if(distance < -0){
			holder.distance.setText("未知");
		}else{
			if(distance > 1000)
				holder.distance.setText(String.format("%.1f公里", (float)distance/1000));
			else
				holder.distance.setText(String.format("%d米", distance));
		}
		_fb.display(holder.image, court.getIcoImgUrl());
		
		return v;
	}
	
	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView distance;
		public TextView price;
	}

	public void swapData(ArrayList<SearchCourtBean> array) {
		// TODO Auto-generated method stub
		_courts.clear();
		_courts.addAll(array);
		notifyDataSetChanged();
	}

}
