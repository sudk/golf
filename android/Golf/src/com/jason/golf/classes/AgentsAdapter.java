package com.jason.golf.classes;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.jsaon.golf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AgentsAdapter extends BaseAdapter {
	
	private ArrayList<GAgent> _agents; 
	private Context _context;
	private LayoutInflater _inflater;

	public AgentsAdapter(Context ctx, ArrayList<GAgent> agents) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(agents == null){
			_agents = new ArrayList<GAgent>();
		}else{
			_agents = agents;
		}
		
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
			holder._brief = (TextView) v.findViewById(R.id.agent_brief);
			holder._price = (TextView) v.findViewById(R.id.agent_price);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GAgent agent = _agents.get(position);
		holder._name.setText(agent.getName());
		holder._brief.setText(agent.getBrief());
		holder._price.setText(String.format("￥ %s", agent.getPrice()));
		
		return v;
	}
	
	private class ViewHolder{
		public TextView _name;
		public TextView _brief;
		public TextView _price;
	}

}
