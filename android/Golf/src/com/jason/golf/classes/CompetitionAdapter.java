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

public class CompetitionAdapter extends BaseAdapter {

	private ArrayList<GCompetition> _competitions;
	private Context _context;
	private LayoutInflater _inflater;
	private FinalBitmap _fb;

	public CompetitionAdapter(Context ctx, ArrayList<GCompetition> competition) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_competitions = new ArrayList<GCompetition>();
		
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
			v = _inflater.inflate(R.layout.competition_list_item, null);
			holder._name = (TextView) v.findViewById(R.id.competition_name);
			holder._date = (TextView) v.findViewById(R.id.competition_date);
			holder._amount = (TextView) v.findViewById(R.id.competition_amount);
			holder._payType = (TextView) v.findViewById(R.id.competition_pay_type);
			holder._courtName = (TextView)v.findViewById(R.id.competition_court);
			holder._img = (ImageView) v.findViewById(R.id.competition_img);
			v.setTag(holder);
		} else {
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}

		GCompetition competition = _competitions.get(position);
		holder._name.setText(competition.getName());
		holder._date.setText(competition.getStartDate());
		holder._amount.setText(String.format("￥%.2f",(float)competition.getFee()/100));
		holder._payType.setText(GCompetition.GetFeeTypeDes(competition.getFeeType()));
		holder._courtName.setText(competition.getCourtName());
		
		_fb.display(holder._img, competition.getImg());
		
		return v;
	}

	public void appendData(ArrayList<GCompetition> data) {
		_competitions.addAll(_competitions.size(), data);
		notifyDataSetChanged();
	}

	public void swapData(ArrayList<GCompetition> data) {
		_competitions.clear();
		_competitions.addAll(data);
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView _name;
		public TextView _date;
		public TextView _amount;
		public TextView _payType;
		public TextView _courtName;
		public ImageView _img;
	}

}
