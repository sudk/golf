package com.jason.golf;


import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GMerchant;
import com.jason.golf.classes.MerchantAdapter;
import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GMerchantInfoFragment extends Fragment {
	
	private static final int REQUESTCODE_CTIY = 1001;
	
	public static final String KEY_MERCHANT_ID = "key_merchant_id";
	
	private TextView mName, mAddr, mComsumption, mPhone, mFeature, mCourtName;
	
	private LinearLayout mMerchantImages;
	
	private String _merchantId = "";
	
	FinalBitmap _fb;

	public static Fragment Instance() {
		return new GMerchantInfoFragment();
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
		_merchantId = getArguments().getString(KEY_MERCHANT_ID, "");
		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_merchant_info, null);
		
		mName = (TextView) v.findViewById(R.id.merchant_name);
		mFeature = (TextView) v.findViewById(R.id.merchant_desc);
		mAddr = (TextView) v.findViewById(R.id.merchant_addr);
		mComsumption = (TextView) v.findViewById(R.id.merchant_consumption);
		mPhone = (TextView) v.findViewById(R.id.merchant_phone);
		mCourtName = (TextView) v.findViewById(R.id.merchant_court);
		
		mMerchantImages = (LinearLayout) v.findViewById(R.id.merchant_images);
		
		queryMerchant();
		return v;
	}
	

	private void queryMerchant() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			
			params.put("cmd", "facilities/info");
			params.put("id", _merchantId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params,
				new HttpCallback() {

					@Override
					public void finalWork() {
						// TODO Auto-generated method stub
						super.finalWork();
						
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						
						super.sucessData(res);

						try {
							JSONObject data = new JSONObject(res);
							
							final GMerchant mc = new GMerchant();
							if(mc.initialize(data)){
								
								
								mName.setText(mc.getFacilitieName());
								mAddr.setText(mc.getAddr());
								mFeature.setText(mc.getFeature());
								mPhone.setText(mc.getPhone());
								mCourtName.setText(mc.getCourtName());
								mComsumption.setText(String.format("人均 ￥%.2f", (float)mc.getConsumption()/100));
								
								mMerchantImages.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										
										if(mc.getImgs().size() > 0){
											
											Bundle params = new Bundle();
											
											String[] urls = new String[mc.getImgs().size()];
											for(int i=0,length=mc.getImgs().size(); i<length;i++){
												urls[i] = mc.getImgs().get(i);
											}
											
											params.putStringArray(GMerchantImagesFragment.KEY_IMG_URL, urls);
											
											Fragment imgFragment = GMerchantImagesFragment.Instance();
											imgFragment.setArguments(params);

											FragmentTransaction transaction = getFragmentManager().beginTransaction();
											transaction.replace(R.id.container, imgFragment);
											transaction.addToBackStack(null);

											// Commit the transaction
											transaction.commit();
											
										}
										
									}
								});
								
								View v = getView();
								DisplayMetrics m = getActivity().getResources().getDisplayMetrics();
								int widthPixels = m.widthPixels;
								
								int width = (widthPixels - v.getPaddingLeft() - v.getPaddingRight()) * 3 / 4;
								int imgMargin = 2;
								
								int imgWidth = width / 3 - imgMargin * 2;
								ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
								
								ArrayList<String> imgUrl = mc.getImgs();
								
								int length = imgUrl.size() <= 3 ? imgUrl.size() : 3 ;
								
								for(int i=0; i<length; i++){
									ImageView img = new ImageView(getActivity());
									img.setLayoutParams(param);
									img.setPadding(2, 2, 2, 2);
									mMerchantImages.addView(img);
									
									if(imgUrl.size() > i)
										_fb.display(img, imgUrl.get(i));
								}
								
							}

							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						super.faildData(code, res);

						if (code == 4) {
							// 没有数据

							
							
						}

					}

				});

		GThreadExecutor.execute(r);

	}

}
