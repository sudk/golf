package com.jason.golf.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;
import com.jason.golf.classes.PreferentialBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PreferentialListAdapter extends BaseAdapter {
	
	private LayoutInflater _inflater ;
	private Context _context;
	private ArrayList<PreferentialBean> _preferentials;
	
	private FinalBitmap _fb;

	public PreferentialListAdapter(Context ctx, ArrayList<PreferentialBean> courts) {
		super();
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_preferentials = new ArrayList<PreferentialBean>();
		if(courts != null){
			_preferentials.addAll(courts);
		}

		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _preferentials.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if( position >= getCount() || position < 0)
			return null;
		PreferentialBean p = _preferentials.get(position);
		return p;
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
			v = _inflater.inflate(R.layout.activity_preferential_list_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) v.findViewById(R.id.court_list_image);
			holder.name = (TextView) v.findViewById(R.id.court_list_name);
			holder.distance = (TextView) v.findViewById(R.id.court_list_distance);
			holder.price = (TextView) v.findViewById(R.id.court_list_price);
			holder.period = (TextView) v.findViewById(R.id.court_list_period);
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		PreferentialBean p = _preferentials.get(position);
		
		holder.name.setText(p.getName());
		holder.price.setText(String.format("￥ %d", p.getPrice() / 100));
		 
		long distance = p.getDistance();
		if(distance < -0){
			holder.distance.setText("未知");
		}else{
			if(distance > 1000)
				holder.distance.setText(String.format("%.1f公里", (float)distance/1000));
			else
				holder.distance.setText(String.format("%d米", distance));
		}
		
		holder.period.setText(String.format("%s - %s", p.getStartTime(), p.getEndTime()));
		
		_fb.display(holder.image, p.getIcoImgUrl());
		
		return v;
	}
	
	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView distance;
		public TextView price;
		public TextView period;
	}

	public void swapData(ArrayList<PreferentialBean> array) {
		// TODO Auto-generated method stub
		_preferentials.clear();
		_preferentials.addAll(array);
		notifyDataSetChanged();
	}

}
