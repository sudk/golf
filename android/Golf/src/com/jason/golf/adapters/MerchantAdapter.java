package com.jason.golf.adapters;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;
import com.jason.golf.classes.GMerchant;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantAdapter extends BaseAdapter {

	private ArrayList<GMerchant> _competitions;
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public MerchantAdapter(Context ctx, ArrayList<GMerchant> competition) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_competitions = new ArrayList<GMerchant>();

		if (competition != null) {
			_competitions.addAll(competition);
		}
		
		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _competitions.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _competitions.get(position);
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
		if (convertView == null) {
			holder = new ViewHolder();
			v = _inflater.inflate(R.layout.merchant_list_item, null);
			holder._name = (TextView) v.findViewById(R.id.merchant_name);
			holder._type = (TextView) v.findViewById(R.id.merchant_type);
			holder._price = (TextView) v.findViewById(R.id.merchant_price);
			holder._addr = (TextView) v.findViewById(R.id.merchant_addr);
			holder._img = (ImageView) v.findViewById(R.id.merchant_img);
			v.setTag(holder);
		} else {
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}

		GMerchant m = _competitions.get(position);
		holder._name.setText(m.getFacilitieName());
		holder._type.setText(GMerchant.GetTypeDesc(m.getType()));
		holder._price.setText(String.format("人均消费 ￥%.2f",(float)m.getConsumption()/100));
		holder._addr.setText(m.getAddr());
//		holder._payType.setText(GCompetition.GetFeeTypeDes(competition.getFeeType()));
//		holder._courtName.setText(competition.getCourtName());
		
		if(m.getImgs().size() > 0)
			_fb.display(holder._img, m.getImgs().get(0));
		
		return v;
	}

	public void appendData(ArrayList<GMerchant> data) {
		_competitions.addAll(_competitions.size(), data);
		notifyDataSetChanged();
	}

	public void swapData(ArrayList<GMerchant> data) {
		_competitions.clear();
		_competitions.addAll(data);
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView _name;
		public TextView _type;
		public TextView _price;
		public TextView _addr;
		public ImageView _img;
	}

}
