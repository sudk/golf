package com.jason.golf.adapters;

import java.util.ArrayList;

import com.jason.golf.GAccountActivity;
import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GAgent;
import com.jason.golf.dialog.BookingOrderDialog;
import com.jason.golf.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AgentsAdapter extends BaseAdapter {
	
	private ArrayList<GAgent> _agents; 
	private Context _context;
	private LayoutInflater _inflater;
	private boolean _isVip;

	public AgentsAdapter(Context ctx, ArrayList<GAgent> agents, boolean isVip) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_isVip = isVip;
		
		_agents = new ArrayList<GAgent>();
		
		if(agents != null){
			_agents.addAll(agents);
		}
		
	}
	
	public void setIsVip(boolean isVip){
		this._isVip = isVip;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _agents.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _agents.get(position);
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
			v = _inflater.inflate(R.layout.agent_list_item, null);
			holder = new ViewHolder();
			holder._name = (TextView) v.findViewById(R.id.agent_name);
			holder._service = (TextView) v.findViewById(R.id.agent_service);
			holder._price = (TextView) v.findViewById(R.id.agent_price);
			holder._vipPrice = (TextView) v.findViewById(R.id.agent_vip_price);
			holder._payType = (TextView) v.findViewById(R.id.agent_paytype);
			holder._reserve = (Button) v.findViewById(R.id.agent_reserve);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		final GAgent agent = _agents.get(position);
		holder._name.setText(agent.getName());
		holder._service.setText(agent.getService());
		holder._price.setText(String.format("￥%d", agent.getPrice()/100));
		holder._vipPrice.setText(String.format("￥%d", agent.getVipPrice()/100));
		
		if(_isVip){
			holder._vipPrice.getPaint().setFlags(Paint.LINEAR_TEXT_FLAG );
			holder._price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			holder._vipPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			holder._price.getPaint().setFlags(Paint.LINEAR_TEXT_FLAG);
		}
		
		holder._payType.setText(GAgent.GetPayTypeDes(agent.getPayType()));
		
		holder._reserve.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GolfAppliaction app = (GolfAppliaction) _context.getApplicationContext();
				GAccount acc = app.getAccount();
				
				if(acc.isLogin()){
					BookingOrderDialog dialog = new BookingOrderDialog(_context, agent, acc.isVip());
					dialog.show();
				}else{
					Intent itLogin = new Intent(_context, GAccountActivity.class);
					itLogin.putExtra(GAccountActivity.FRAGMENT, GAccountActivity.LOGIN);
					_context.startActivity(itLogin);
					
				}
				
			}
			
		});
		
		
		
		return v;
	}
	
	public void swapData(ArrayList<GAgent> data){
		_agents.clear();
		_agents.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _name;
		public TextView _service;
		public TextView _price;
		public TextView _vipPrice;
		public TextView _payType;
		public Button _reserve;
	}

}
