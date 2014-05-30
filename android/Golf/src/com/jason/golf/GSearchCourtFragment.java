package com.jason.golf;

import java.util.Calendar;

import mirko.android.datetimepicker.date.DatePickerDialog;
import mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import mirko.android.datetimepicker.time.RadialPickerLayout;
import mirko.android.datetimepicker.time.TimePickerDialog;
import mirko.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import net.tsz.afinal.FinalBitmap;

import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

public class GSearchCourtFragment extends Fragment implements OnClickListener {

	private static final int requestcode = 1001;

	private final Calendar mCalendar = Calendar.getInstance();

	private TextView mSelectDate, mSelectTime, mSelectCity;

	private String _cityId, _date, _time;

	private DatePickerDialog mDatePickerDialog;

	private TimePickerDialog mTimePickerDialog12h;
	
	private EditText mKeywords;

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private static String pad2(int c) {
		if (c == 12)
			return String.valueOf(c);
		if (c == 00)
			return String.valueOf(c + 12);
		if (c > 12)
			return String.valueOf(c - 12);
		else
			return String.valueOf(c);
	}

	private static String pad3(int c) {
		if (c == 12)
			return " PM";
		if (c == 00)
			return " AM";
		if (c > 12)
			return " PM";
		else
			return " AM";
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_search_court, container, false);
		v.findViewById(R.id.select_date).setOnClickListener(this);
		v.findViewById(R.id.search_court).setOnClickListener(this);
		
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		ActionBar bar = activity.getSupportActionBar();
		bar.setTitle(R.string.search_court);
		

		mSelectDate = (TextView) v.findViewById(R.id.select_date);
		mSelectDate.setOnClickListener(this);
		mSelectTime = (TextView) v.findViewById(R.id.select_time);
		mSelectTime.setOnClickListener(this);
		mSelectCity = (TextView) v.findViewById(R.id.select_city);
		mSelectCity.setOnClickListener(this);
		
		mKeywords = (EditText) v.findViewById(R.id.select_keywords);

		mCalendar.add(Calendar.DAY_OF_MONTH, 1);

		String date = String.format("%d月%d日",
				mCalendar.get(Calendar.MONTH) + 1,
				mCalendar.get(Calendar.DAY_OF_MONTH));
		mSelectDate.setText(date);
		_date = String.format("%d-%02d-%02d", mCalendar.get(Calendar.YEAR),
				mCalendar.get(Calendar.MONTH) + 1,
				mCalendar.get(Calendar.DAY_OF_MONTH));

		mSelectTime.setText(String.format("9点30分"));
		_time = "09:30";

		mDatePickerDialog = DatePickerDialog.newInstance(
				new OnDateSetListener() {

					public void onDateSet(DatePickerDialog datePickerDialog,
							int year, int month, int day) {

						// mSelectDate.setText(new
						// StringBuilder().append(pad(day))
						// .append(" ").append(pad(month + 1)).append(" ")
						// .append(pad(year)));

						mSelectDate.setText(String.format("%d月%d日", month + 1,
								day));
						_date = String.format("%d-%02d-%02d", year, month + 1,
								day);
					}

				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH));

		mTimePickerDialog12h = TimePickerDialog.newInstance(
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(RadialPickerLayout view,
							int hourOfDay, int minute) {

						// Object c = pad3(hourOfDay);
						// mSelectTime.setText(
						// new StringBuilder().append(pad2(hourOfDay))
						// .append(":").append(pad(minute)).append(c));

						mSelectTime.setText(String.format("%d点%d分", hourOfDay,
								minute));
						_time = String.format("%02d:%02d", hourOfDay, minute);

					}
				}, 9, 30, false);

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.select_date:
			mDatePickerDialog.show(getFragmentManager(), "SelsectDate");
			break;
		case R.id.select_time:
			mTimePickerDialog12h.show(getFragmentManager(), "SelectTime");
			break;
		case R.id.select_city:
			startActivityForResult(new Intent(getActivity(),
					SelectCityActivity.class), requestcode);
			break;
		case R.id.search_court:

			Intent it = new Intent(getActivity(), GCourtListActivity.class);

			Bundle args = new Bundle();
			args.putString(GCourtListActivity.ARG_CITY, _cityId);
			args.putString(GCourtListActivity.ARG_DATE, _date);
			args.putString(GCourtListActivity.ARG_TIME, _time);
			args.putString(GCourtListActivity.ARG_KEYWORD, mKeywords.getText().toString());
			it.putExtras(args);

			startActivity(it);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null)
			return;

		long cityRowId = data.getLongExtra("RowID", 0);

		System.out.println(String.format("requestCode = %d, RowId = %d",
				requestCode, cityRowId));

		ContentResolver cr = getActivity().getContentResolver();

		Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
				GolfProviderConfig.City._ID + "=? ", new String[] { ""
						+ cityRowId }, null);

		if (c == null)
			return;

		try {
			if (c.moveToFirst()) {
				String city = c.getString(c
						.getColumnIndex(GolfProviderConfig.City.CITY));
				_cityId = c.getString(c
						.getColumnIndex(GolfProviderConfig.City.CITY_ID));
				mSelectCity.setText(city);
			}
		} finally {
			c.close();
		}
	}

}
