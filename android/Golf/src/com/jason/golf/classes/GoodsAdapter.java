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

public class GoodsAdapter extends BaseAdapter {
	
	private ArrayList<GGood> _goods; 
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public GoodsAdapter(Context ctx, ArrayList<GGood> goods) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_goods = new ArrayList<GGood>();
		
		if(goods != null){
			_goods.addAll(goods);
		}
		

		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
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
			v = _inflater.inflate(R.layout.fragment_flee_market_list_item, null);
			holder = new ViewHolder();

			holder._title = (TextView) v.findViewById(R.id.good_title);
			holder._date = (TextView) v.findViewById(R.id.good_date);
			holder._contact = (TextView) v.findViewById(R.id.good_contact);
			holder._img = (ImageView) v.findViewById(R.id.good_img);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GGood good= _goods.get(position);
		
		holder._title.setText(good.getTitle());
		holder._date.setText(good.getRecord_time());
		holder._contact.setText(good.getContact());
		
		_fb.display(holder._img, good.getImgs().get(0));
		
		return v;
	}
	
	public void addData(ArrayList<GGood> data){
		_goods.addAll(_goods.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GGood> data){
		_goods.clear();
		_goods.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public ImageView _img;
		public TextView _title;
		public TextView _contact;
		public TextView _date;
	}

}
