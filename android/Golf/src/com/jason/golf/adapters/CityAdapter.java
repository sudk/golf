package com.jason.golf.adapters;

import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

public class CityAdapter extends CursorTreeAdapter {
	
	private Context _context;
	private LayoutInflater _inflater;
	
	public CityAdapter(Cursor cursor, Context context) {
		super(cursor, context, true);
		// TODO Auto-generated constructor stub
		_context = context;
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor,	boolean isLastChild) {
		// TODO Auto-generated method stub
		String city = cursor.getString(cursor.getColumnIndex(GolfProviderConfig.City.CITY));
		CityViewHolder holder = (CityViewHolder) view.getTag();
		holder.city.setText(city);
	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor,	boolean isExpanded) {
		// TODO Auto-generated method stub
		String province = cursor.getString(cursor.getColumnIndex(GolfProviderConfig.Province.PROVINCE));
		ProvinceViewHolder holder = (ProvinceViewHolder) view.getTag();
		holder.province.setText(province);
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		// TODO Auto-generated method stub
		String provinceId = groupCursor.getString(groupCursor.getColumnIndex(GolfProviderConfig.Province.PROVINCE_ID));
		ContentResolver cr = _context.getContentResolver();
		Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null, GolfProviderConfig.City.FATHERID + "=? ", new String[] { provinceId }, null);
		return c;
	}

	@Override
	protected View newChildView(Context context, Cursor cursor,	boolean isLastChild, ViewGroup parent) {
		// TODO Auto-generated method stub
		CityViewHolder holder = new CityViewHolder();
		View v = _inflater.inflate(R.layout.city_item, null);
		holder.city = (TextView) v.findViewById(R.id.city);
		v.setTag(holder);
		return v;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,	boolean isExpanded, ViewGroup parent) {
		// TODO Auto-generated method stub
		ProvinceViewHolder holder = new ProvinceViewHolder();
		View v = _inflater.inflate(R.layout.province_item, null);
		holder.province = (TextView) v.findViewById(R.id.province);
		v.setTag(holder);
		return v;
	}

	private class CityViewHolder{
		public TextView city;
	}
	
	private class ProvinceViewHolder{
		public TextView province;
	}
}
