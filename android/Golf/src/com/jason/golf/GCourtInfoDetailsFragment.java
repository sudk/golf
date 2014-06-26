package com.jason.golf;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.jason.golf.classes.GCourt;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
	
	TextView mModel, mCreatYear, mCourtArea, mGreenGrass, mData, mDesigner, mFairwayLength, mFairwayGrass, mPhone, mBrief, mFacilities, mComment;
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
		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		getActivity().getActionBar().setTitle(R.string.court_info);
		
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
		mPhone.setOnClickListener(this);
		mBrief = (TextView) v.findViewById(R.id.court_brief);
		mFacilities = (TextView) v.findViewById(R.id.court_facilities);
		
		mComment = (TextView) v.findViewById(R.id.court_comment);
		mComment.setOnClickListener(this);
		
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
		
		int length = imgUrl.size() <= 3 ? imgUrl.size() : 3 ;
		
		for(int i=0; i<length; i++){
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
			
			WarnDialog dialog = new WarnDialog(getActivity());
			
			dialog.setTitle(R.string.phone)
			.setMessage(String.format("是否拨打客服电话%s?", mPhone.getText()))
			.setPositiveBtn("拨打", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Uri uri=Uri.parse("tel:"+ mPhone.getText());   //拨打电话号码的URI格式
					Intent it=new Intent();   //实例化Intent
					it.setAction(Intent.ACTION_CALL);   //指定Action
					it.setData(uri);   //设置数据
					startActivity(it);//启动Acitivity
				}
			}).setNegativeBtn(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			}).show(getFragmentManager(), "DialTel");
			
			break;
		case R.id.court_comment:
			
			GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
			GCourt court = a.getCourt();
			
			Bundle params = new Bundle();
			params.putString(GCourtInfoCommentsFragment.KEY_COURT_ID, court.getId());
			
			Fragment commentFragment = GCourtInfoCommentsFragment.Instance();
			commentFragment.setArguments(params);
			
			FragmentTransaction commTransaction = getFragmentManager().beginTransaction();
			commTransaction.replace(R.id.container, commentFragment);
			commTransaction.addToBackStack(null);
			commTransaction.commit();
			
			break;
		}
	}
	

}
