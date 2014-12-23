package com.jason.golf.adapters;

import java.util.ArrayList;

import com.jason.golf.R;
import com.jason.golf.classes.GComment;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CommentAdapter extends BaseAdapter {

	private ArrayList<GComment> _goods; 
	private Context _context;
	private LayoutInflater _inflater;

	public CommentAdapter(Context ctx, ArrayList<GComment> goods) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_goods = new ArrayList<GComment>();
		
		if(goods != null){
			_goods.addAll(goods);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _goods.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _goods.get(position);
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
			v = _inflater.inflate(R.layout.fragment_court_info_comments_item, null);
			holder = new ViewHolder();

			holder._desc = (TextView) v.findViewById(R.id.comment_desc);
			holder._date = (TextView) v.findViewById(R.id.commentDate);
			holder._service = (TextView) v.findViewById(R.id.service);
			holder._design = (TextView) v.findViewById(R.id.design);
			holder._facilities = (TextView) v.findViewById(R.id.facilitie);
			holder._lawn = (TextView) v.findViewById(R.id.lawn);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GComment comment= _goods.get(position);
		
		holder._desc.setText(comment.getDesc());
		holder._date.setText(comment.getRecordTime());
		holder._service.setText(String.format("服务:%s", comment.getService()));
		holder._design.setText(String.format("设计:%s", comment.getDesign()));
		holder._facilities.setText(String.format("设施:%s", comment.getFacilitie()));
		holder._lawn.setText(String.format("草场:%s", comment.getLawn()));
		
		return v;
	}
	
	public void addData(ArrayList<GComment> data){
		_goods.addAll(_goods.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GComment> data){
		_goods.clear();
		_goods.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView _desc;
		public TextView _date;
		public TextView _lawn;
		public TextView _service;
		public TextView _facilities;
		public TextView _design;
	}

}
