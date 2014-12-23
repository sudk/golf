package com.jason.golf.adapters;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;
import com.jason.golf.classes.GCardBean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CardsAdapter extends BaseAdapter {
	
	private ArrayList<GCardBean> _cards; 
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public CardsAdapter(Context ctx, ArrayList<GCardBean> cards) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_cards = new ArrayList<GCardBean>();
		
		if(cards != null){
			_cards.addAll(cards);
		}

		_fb = FinalBitmap.create(_context);
		_fb.configLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _cards.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _cards.get(position);
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
			v = _inflater.inflate(R.layout.fragment_card_list_item, null);
			holder = new ViewHolder();

			holder._cardName = (TextView) v.findViewById(R.id.card_name);
			holder._cardNo = (TextView) v.findViewById(R.id.card_no);
			holder._img = (ImageView) v.findViewById(R.id.card_img);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		GCardBean card = _cards.get(position);
		
		holder._cardName.setText(card.getCardName());
		holder._cardNo.setText(String.format("卡号：%s", card.getCardNo()));
		
		if(!TextUtils.isEmpty(card.getImg())){
			_fb.display(holder._img, card.getImg());
		}
		
		return v;
	}
	
	public void addData(ArrayList<GCardBean> data){
		_cards.addAll(_cards.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GCardBean> data){
		_cards.clear();
		_cards.addAll(data);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public ImageView _img;
		public TextView _cardName;
		public TextView _cardNo;
	}

}
