package com.jason.golf.adapters;

import java.util.ArrayList;
import net.tsz.afinal.FinalBitmap;
import com.jason.golf.R;
import com.jason.golf.classes.GTrip;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TripsAdapter extends BaseAdapter {

	private ArrayList<GTrip> _trips;
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public TripsAdapter(Context ctx, ArrayList<GTrip> trips) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_trips = new ArrayList<GTrip>();

		if (trips != null) {
			_trips.addAll(trips);
		}
		
		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
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
			holder._comboImg = (ImageView) v.findViewById(R.id.combo_img);
			holder._comboName = (TextView) v.findViewById(R.id.combo_name);
			holder._comboCourt = (TextView) v.findViewById(R.id.combo_court);
//			holder._comboDate = (TextView) v.findViewById(R.id.combo_date);
			holder._comboAmount = (TextView) v.findViewById(R.id.combo_amount);
			holder._comboPayType = (TextView) v.findViewById(R.id.combo_pay_type);
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GTrip trip = _trips.get(position);
		
		holder._comboName.setText(trip.getTripName());
		holder._comboCourt.setText(trip.getCourtName());
//		holder._comboDate.setText(trip.getStartDate());
		holder._comboAmount.setText(String.format("￥%.0f" , (float)trip.getHolidayPrice() / 100));
		holder._comboPayType.setText(GTrip.GetPayTypeDes(trip.getPayType()));
		
		if(trip.getImgs().size() > 0){
			_fb.display(holder._comboImg, trip.getImgs().get(0));
		}
		
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
		public ImageView _comboImg;
		public TextView _comboName;
		public TextView _comboCourt;
//		public TextView _comboDate;
		public TextView _comboAmount;
		public TextView _comboPayType;
	}

}
