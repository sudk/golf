package com.jason.golf.adapters;

import java.util.ArrayList;

import com.jason.golf.R;
import com.jason.golf.classes.GVipChoiceBean;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class VipChoiceAdapter implements SpinnerAdapter {

	private LayoutInflater _inflater;
	private Context _context;

	private ArrayList<GVipChoiceBean> _choices;

	public VipChoiceAdapter(Context _context, ArrayList<GVipChoiceBean> _choices) {
		super();
		this._context = _context;
		this._choices = _choices;
		this._inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _choices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _choices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		TextView tv = (TextView) _inflater.inflate(R.layout.vip_choice_item, null);

		GVipChoiceBean bean = _choices.get(position);

		String value = bean.getValue();
		int price = Integer.parseInt(value);

		switch (position) {
		case 0:
			tv.setText(String.format("一年VIP，￥%d", price/100));
			break;
		case 1:
			tv.setText(String.format("三年VIP，￥%d", price/100));
			break;
		}

		return tv;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		TextView tv = (TextView) _inflater.inflate(R.layout.vip_choice_dropdown_item, null);

		GVipChoiceBean bean = _choices.get(position);

		String value = bean.getValue();
		int price = Integer.parseInt(value);

		switch (position) {
		case 0:
			tv.setText(String.format("一年VIP，￥%d", price/100));
			break;
		case 1:
			tv.setText(String.format("三年VIP，￥%d", price/100));
			break;
		}

		return tv;
	}

}
