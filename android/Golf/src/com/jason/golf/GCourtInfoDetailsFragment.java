package com.jason.golf;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.classes.GCourt;
import com.jsaon.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GCourtInfoDetailsFragment extends Fragment implements OnClickListener {
	
	TextView mModel, mCreatYear, mCourtArea, mGreenGrass, mData, mDesigner, mFairwayLength, mFairwayGrass, mPhone, mBrief, mFacilities;
	LinearLayout mImages;
	FinalBitmap _fb;

	public static Fragment Instance() {
		// TODO Auto-generated method stub
		
		return new GCourtInfoDetailsFragment();
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
		hasOptionsMenu();
		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_court_info_details, null);
		
		mModel = (TextView) v.findViewById(R.id.court_model);
		mCreatYear = (TextView) v.findViewById(R.id.court_create_date);
		mCourtArea = (TextView) v.findViewById(R.id.court_area);
		mGreenGrass = (TextView) v.findViewById(R.id.court_greengrass);
		mData = (TextView) v.findViewById(R.id.court_data);
		mDesigner = (TextView) v.findViewById(R.id.court_designer);
		mFairwayLength = (TextView) v.findViewById(R.id.court_fairway_length);
		mFairwayGrass = (TextView) v.findViewById(R.id.court_fairway_grass);
		mPhone = (TextView) v.findViewById(R.id.court_phone);
		mBrief = (TextView) v.findViewById(R.id.court_brief);
		mFacilities = (TextView) v.findViewById(R.id.court_facilities);
		
		GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
		GCourt court = a.getCourt();
		
		mModel.setText(court.getModel());
		mCreatYear.setText(court.getCreateYear());
		mCourtArea.setText(court.getArea());
		mGreenGrass.setText(court.getGreenGrass());
		mData.setText(court.getCourtData());
		mDesigner.setText(court.getDesigner());
		mFairwayLength.setText(court.getFairwayLength());
		mFairwayGrass.setText(court.getFairwayGrass());
		mPhone.setText(court.getPhone());
		mBrief.setText(court.getRemark());
		mFacilities.setText(court.getFacilities());
		
		mImages = (LinearLayout) v.findViewById(R.id.images);
		mImages.setOnClickListener(this);
		
		DisplayMetrics m = getActivity().getResources().getDisplayMetrics();
		int widthPixels = m.widthPixels;
		ImageView triange = (ImageView) v.findViewById(R.id.triange);
		triange.measure(0, 0);
		
		int width = (widthPixels - v.getPaddingLeft() - v.getPaddingRight() - triange.getMeasuredWidth()) * 3 / 4;
		int imgMargin = 2;
		
		int imgWidth = width / 3 - imgMargin * 2;
		ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
		
		ArrayList<String> imgUrl = court.getFairwayImgs();
		
		for(int i=0, length=3; i<length; i++){
			ImageView img = new ImageView(getActivity());
			img.setLayoutParams(param);
			img.setPadding(2, 2, 2, 2);
			mImages.addView(img);
			
			if(imgUrl.size() > i)
				_fb.display(img, imgUrl.get(i));
		}
		
//		ActionBarActivity activity = (ActionBarActivity) getActivity();
//		ActionBar bar = activity.getSupportActionBar();
//		bar.setTitle(R.string.court_info);
//		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
//	    bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);
	    
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.images:
			
			Fragment newFragment = GCourtInfoImagesFragment.Instance();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			
			break;
		case R.id.court_phone:
			
			break;
		}
	}
	

}
