package com.jason.golf.adapters;

import java.util.ArrayList;
import net.tsz.afinal.FinalBitmap;
import com.jason.golf.R;
import com.jason.golf.classes.GCourtBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CourtBeanAdapter extends BaseAdapter {

	private ArrayList<GCourtBean> _courts; 
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public CourtBeanAdapter(Context ctx, ArrayList<GCourtBean> courts) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_courts = new ArrayList<GCourtBean>();
		
		if(courts != null){
			_courts.addAll(courts);
		}

		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _courts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _courts.get(position);
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
			v = _inflater.inflate(R.layout.activity_courtbeen_list_item, null);
			holder = new ViewHolder();
			holder._name = (TextView) v.findViewById(R.id.court_name);
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GCourtBean bean = _courts.get(position);
		holder._name.setText(bean.getName());
		
		return v;
	}
	
	public void addData(ArrayList<GCourtBean> data){
		_courts.addAll(_courts.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GCourtBean> data){
		_courts.clear();
		_courts.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _name;
	}

}
