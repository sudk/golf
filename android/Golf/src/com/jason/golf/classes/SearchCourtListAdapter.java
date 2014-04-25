package com.jason.golf.classes;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.jsaon.golf.R;

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
	private ArrayList<GCourt> _courts;

	public SearchCourtListAdapter(Context ctx, ArrayList<GCourt> courts) {
		super();
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(courts == null){
			_courts = new ArrayList<GCourt>();
		}else{
			_courts = courts;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _courts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if( position > getCount() || position < 0)
			return null;
		GCourt court = _courts.get(position);
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
		
		GCourt court = _courts.get(position);
//		holder.image
		
		holder.name.setText(court.getName());
		holder.price.setText(String.format("￥ %s", court.getPrice()));
		
		
		
		return v;
	}
	
	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView distance;
		public TextView price;
	}

}
