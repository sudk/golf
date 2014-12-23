package com.jason.golf;


import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GCardBean;
import com.jason.golf.classes.GGood;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GCardInfoFragment extends Fragment {
	
	public static final String KEY_CARD_ID = "key_card_id";
	
	private TextView mCardName, mCardDesc, mCardNo;
	
	private LinearLayout mCardImages;
	
	private String _cardId = "";
	
	private FinalBitmap _fb;
	
	public static GCardInfoFragment Instance() {
		return new GCardInfoFragment();
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
		_cardId = getArguments().getString(KEY_CARD_ID, "");
		
		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_card_info, null);
		mCardName = (TextView) v.findViewById(R.id.card_name);
		mCardDesc = (TextView) v.findViewById(R.id.card_desc);
		mCardNo = (TextView) v.findViewById(R.id.card_no);
		
		mCardImages = (LinearLayout) v.findViewById(R.id.card_img);
		
		queryCradInfo();
		return v;
	}
	
	private void queryCradInfo() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			
			params.put("cmd", "card/info");
			params.put("id", _cardId);

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
							
							GCardBean g = new GCardBean();
							if(g.initialize(data)){
								
								mCardName.setText(g.getCardName());
								mCardNo.setText(g.getCardNo());
								mCardDesc.setText(g.getDesc());
								
								View v = getView();
								DisplayMetrics m = getActivity().getResources().getDisplayMetrics();
								int widthPixels = m.widthPixels;
								
								int width = (widthPixels - v.getPaddingLeft() - v.getPaddingRight()) * 3 / 4;
								int imgMargin = 2;
								
								int imgWidth = width  - imgMargin * 2;
								ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
								
								final String imgUrl = g.getImg();
								
									
								if(!TextUtils.isEmpty(imgUrl)){
									
									ImageView img = new ImageView(getActivity());
									img.setLayoutParams(param);
									img.setPadding(2, 2, 2, 2);
									mCardImages.addView(img);
									
									img.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											
											Bundle params = new Bundle();
											params.putString(GCardImageFragment.KEY_IMG_URL, imgUrl);
											
											Fragment infoFragment = GCardImageFragment.Instance();
											infoFragment.setArguments(new Bundle(params));
											
											FragmentTransaction transaction = ((ActionBarActivity)getActivity()).getSupportFragmentManager().beginTransaction();

											transaction.replace(R.id.container, infoFragment);
											transaction.addToBackStack(null);

											// Commit the transaction
											transaction.commit();
											
										}
									});

									_fb.display(img, imgUrl);
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
