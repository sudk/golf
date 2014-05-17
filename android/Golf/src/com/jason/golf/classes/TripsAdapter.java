package com.jason.golf.classes;

import java.util.ArrayList;

import com.jsaon.golf.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TripsAdapter extends BaseAdapter {

	private ArrayList<GTrip> _trips;
	private Context _context;
	private LayoutInflater _inflater;

	public TripsAdapter(Context ctx, ArrayList<GTrip> trips) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_trips = new ArrayList<GTrip>();

		if (trips != null) {
			_trips.addAll(trips);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _trips.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _trips.get(position);
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
			v = _inflater.inflate(R.layout.fragment_trip_list_item, null);
			holder = new ViewHolder();
			holder._tripName = (TextView) v.findViewById(R.id.trip_list_name);
			holder._date = (TextView) v.findViewById(R.id.trip_list_date);
			holder._amount = (TextView) v.findViewById(R.id.trip_list_price);
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GTrip trip = _trips.get(position);
		
		holder._tripName.setText(trip.getTripName());
		holder._date.setText(trip.getStartDate());
		holder._amount.setText(String.format("￥%.2f" , (float)trip.getHolidayPrice() / 100));
		
		
		return v;
	}

	public void addData(ArrayList<GTrip> data) {
		_trips.addAll(_trips.size(), data);
		notifyDataSetChanged();
	}

	public void swapData(ArrayList<GTrip> data) {
		_trips.clear();
		_trips.addAll(data);
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView _tripName;
		public TextView _date;
		public TextView _amount;
	}

}
