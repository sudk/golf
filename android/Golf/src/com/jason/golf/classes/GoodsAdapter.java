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

public class GoodsAdapter extends BaseAdapter {
	
	private ArrayList<GGood> _orders; 
	private Context _context;
	private LayoutInflater _inflater;

	public GoodsAdapter(Context ctx, ArrayList<GGood> agents) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_orders = new ArrayList<GGood>();
		
		if(agents != null){
			_orders.addAll(agents);
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _orders.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _orders.get(position);
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
			v = _inflater.inflate(R.layout.goods_list_item, null);
			holder = new ViewHolder();

			
			
			
			
			
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GGood order = _orders.get(position);
		

		
		
		
		
		return v;
	}
	
	public void addData(ArrayList<GGood> data){
		_orders.addAll(_orders.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GGood> data){
		_orders.clear();
		_orders.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _name;
		public TextView _date;
		public TextView _image;
		public TextView _payType;
	}

}
