package com.jason.golf.adapters;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;
import com.jason.golf.classes.GHandicapBean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HandicapsAdapter extends BaseAdapter {
	
	private ArrayList<GHandicapBean> _trans; 
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public HandicapsAdapter(Context ctx, ArrayList<GHandicapBean> goods) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_trans = new ArrayList<GHandicapBean>();
		
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
			v = _inflater.inflate(R.layout.fragment_handicap_public_item, null);
			
			holder = new ViewHolder();
			holder._court = (TextView) v.findViewById(R.id.court);
			holder._name = (TextView) v.findViewById(R.id.name);
			holder._handicap = (TextView) v.findViewById(R.id.handicap);
			holder._date = (TextView) v.findViewById(R.id.date);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GHandicapBean record= _trans.get(position);
		
		holder._handicap.setText(String.format("差点：%s",record.getHandicap()));
		
		String name = record.getUserName();
		
		if(TextUtils.isEmpty(name) || "null".equals(name.toLowerCase())){
			holder._name.setText("匿名");
		}else{
			holder._name.setText(name);
		}
		
		holder._court.setText(record.getCourt_name());
		
		String date = record.getFeeTime();
		holder._date.setText((date.split(" "))[0]);
		
		return v;
	}
	
	public void addData(ArrayList<GHandicapBean> data){
		_trans.addAll(_trans.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GHandicapBean> data){
		_trans.clear();
		_trans.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _handicap;
		public TextView _name;
		public TextView _court;
		public TextView _date;
	}

}
