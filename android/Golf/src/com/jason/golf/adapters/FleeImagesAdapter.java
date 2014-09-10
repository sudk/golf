package com.jason.golf.adapters;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.R;
import com.jason.golf.classes.GMerchant;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FleeImagesAdapter extends BaseAdapter {

	private ArrayList<String> _images;
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;
	private int widthPixels, heightPixels;

	public FleeImagesAdapter(Context ctx, ArrayList<String> imgs) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(imgs == null){
			_images = new ArrayList<String>();
		}else{
			_images = imgs;
		}

		DisplayMetrics m = _context.getResources().getDisplayMetrics();
		widthPixels = m.widthPixels / 5;
		heightPixels = widthPixels * 3 / 2 ; 
		
		_fb = FinalBitmap.create(_context);
		_fb.configBitmapMaxWidth(widthPixels);
		_fb.configBitmapMaxHeight(heightPixels);
		_fb.configLoadingImage(R.drawable.ic_launcher);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _images.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _images.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewGroup.LayoutParams params = new LayoutParams(widthPixels + 5*2, heightPixels + 2*2);
		
		ImageView v = new ImageView(_context);
		v.setLayoutParams(params);
		v.setPadding(5, 2, 5, 2);
		v.setTag(position);
		
		String url = _images.get(position);
		_fb.display(v, url);
		return v;
	}

}
