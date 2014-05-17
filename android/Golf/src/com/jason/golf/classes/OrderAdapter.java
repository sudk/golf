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

public class OrderAdapter extends BaseAdapter {
	
	private ArrayList<GOrder> _orders; 
	private Context _context;
	private LayoutInflater _inflater;

	public OrderAdapter(Context ctx, ArrayList<GOrder> agents) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_orders = new ArrayList<GOrder>();
		
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
			v = _inflater.inflate(R.layout.order_list_item, null);
			holder = new ViewHolder();
			holder._courtName = (TextView) v.findViewById(R.id.list_order_court_name);
			holder._date = (TextView) v.findViewById(R.id.list_order_date);
			holder._amount = (TextView) v.findViewById(R.id.list_order_amount);
			holder._payType = (TextView) v.findViewById(R.id.list_order_pay_type);
			holder._orderId = (TextView) v.findViewById(R.id.list_order_id);
			holder._status = (TextView) v.findViewById(R.id.list_order_status);
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GOrder order = _orders.get(position);
		
		holder._courtName.setText(order.getRelationName());
		
		String teetime = order.getTeeTime();
		String date = "";
		if(!TextUtils.isEmpty(teetime))
			date = teetime.substring(0, teetime.indexOf(" "));
		holder._date.setText(date);
		holder._amount.setText(String.format("￥%.2f",(float)order.getAmount()/100));
		holder._payType.setText(GOrder.GetPayTypeDes(order.getPayType()));
		
		String status = order.getStatus();
		holder._status.setText(GOrder.GetStatusDes(status));
		if("1".equals(status)){
			holder._status.setTextColor(_context.getResources().getColor(R.color.markedness));
		}else{
			holder._status.setTextColor(_context.getResources().getColor(android.R.color.black));
		}
		
		holder._orderId.setText(order.getOrderId());
		return v;
	}
	
	public void addData(ArrayList<GOrder> data){
		_orders.addAll(_orders.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GOrder> data){
		_orders.clear();
		_orders.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _courtName;
		public TextView _date;
		public TextView _amount;
		public TextView _payType;
		public TextView _orderId;
		public TextView _status;
	}

}
