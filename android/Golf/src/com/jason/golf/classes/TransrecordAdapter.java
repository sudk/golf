package com.jason.golf.classes;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TransrecordAdapter extends BaseAdapter {
	
	private ArrayList<GTransrecord> _trans; 
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public TransrecordAdapter(Context ctx, ArrayList<GTransrecord> goods) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_trans = new ArrayList<GTransrecord>();
		
		if(goods != null){
			_trans.addAll(goods);
		}
		

		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _trans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _trans.get(position);
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
			v = _inflater.inflate(R.layout.fragment_transrecord_list_item, null);
			holder = new ViewHolder();

			holder._sn = (TextView) v.findViewById(R.id.record_sn);
			holder._date = (TextView) v.findViewById(R.id.record_date);
			holder._status = (TextView) v.findViewById(R.id.record_status);
			holder._type = (TextView) v.findViewById(R.id.record_tran_type);
			holder._amount = (TextView) v.findViewById(R.id.record_amount);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GTransrecord record= _trans.get(position);
		
		holder._sn.setText(record.getSerialNumber());
		holder._date.setText(record.getRecordTime());
		holder._status.setText(record.getStatus());
		holder._amount.setText(record.getAmount());
		holder._type.setText(record.getTransType());
		
		return v;
	}
	
	public void addData(ArrayList<GTransrecord> data){
		_trans.addAll(_trans.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GTransrecord> data){
		_trans.clear();
		_trans.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _sn;
		public TextView _date;
		public TextView _type;
		public TextView _amount;
		public TextView _status;
	}

}
