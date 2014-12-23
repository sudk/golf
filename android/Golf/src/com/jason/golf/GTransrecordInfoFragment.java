package com.jason.golf;


import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GTransrecordInfoFragment extends Fragment {
	
	private static final int REQUESTCODE_CTIY = 1001;
	
	public static final String KEY_GOOD_ID = "key_good_id";
	public static final String KEY_ENABLE_EDIT = "key_enable_edit";
	
	private TextView mTitle, mDesc, mPrice, mContact, mPhone, mCity;
	
	private LinearLayout mGoodImages;
	
	private String _googId = "";
	
	private boolean _enableEdit;
	
	private FinalBitmap _fb;
	
	

	public static GTransrecordInfoFragment Instance() {
		return new GTransrecordInfoFragment();
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
		_googId = getArguments().getString(KEY_GOOD_ID, "");
		
		_enableEdit = getArguments().getBoolean(KEY_ENABLE_EDIT, false);
		setHasOptionsMenu(_enableEdit);
		
		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_flee_market_info, null);
		mTitle = (TextView) v.findViewById(R.id.flee_good_title);
		mDesc = (TextView) v.findViewById(R.id.flee_good_desc);
		mContact = (TextView) v.findViewById(R.id.flee_good_contact);
		mPhone = (TextView) v.findViewById(R.id.flee_good_phone);
		mPrice = (TextView) v.findViewById(R.id.flee_good_price);
		mCity = (TextView) v.findViewById(R.id.flee_good_city);
		
		mGoodImages = (LinearLayout) v.findViewById(R.id.flee_good_images);
		
		queryGood();
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.flee_info_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case R.id.menu_edit_good:
			
			Bundle params = new Bundle();
			params.putString(KEY_GOOD_ID, _googId);
			Fragment editFragment = GFleeMarketEditFragment.Instance();
			editFragment.setArguments(params);
			
			getFragmentManager().popBackStack();
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, editFragment);
			transaction.addToBackStack(null);
			
			// Commit the transaction
			transaction.commit();
			
			break;
		case R.id.menu_del_good:
			
			WarnDialog dialog = WarnDialog.newInstance(getActivity());
			dialog.setTitle(R.string.flee_cancel_good).setMessage(R.string.flee_cancel_good_sucess)
			.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					deleteGood();
				}
			}).setNegativeBtn(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			dialog.show(getFragmentManager(), "AskForDel");
			
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void deleteGood() {
		// TODO Auto-generated method stub
		
		
		
		JSONObject params = new JSONObject();

		try {
			
			params.put("cmd", "flea/del");
			params.put("id", _googId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				WarnDialog dialog = WarnDialog.newInstance(getActivity());
				dialog.setTitle(R.string.flee_cancel_good).setMessage(R.string.flee_cancel_good_sucess)
				.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						((ActionBarActivity)getActivity()).onSupportNavigateUp();
					}
				});
				
				dialog.show(getFragmentManager(), "FleeDelSuccess");
				super.sucessData(res);
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				WarnDialog dialog = WarnDialog.newInstance(getActivity());
				dialog.setTitle(R.string.flee_cancel_good).setMessage(res)
				.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				
				dialog.show(getFragmentManager(), "FleeDelFaild");
				
				super.faildData(code, res);
			}
			
			
			
		});
		
		GThreadExecutor.execute(r);
		
		
	}

	private void queryGood() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			
			params.put("cmd", "flea/info");
			params.put("id", _googId);

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
							
							GGood g = new GGood();
							if(g.initialize(data)){
								
								mTitle.setText(g.getTitle());
								mContact.setText(g.getContact());
								mPhone.setText(g.getPhone());
								mDesc.setText(g.getDesc());
								
								int p = Integer.parseInt(g.getPrice());
								mPrice.setText(String.format("￥%.2f",(float)p/100));
								
								String cityId = g.getCity();
								ContentResolver cr = getActivity().getContentResolver();
								Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
										GolfProviderConfig.City.CITY_ID + "=? ", new String[] { "" + cityId }, null);
								if (c == null)
									return;
								try {
									if (c.moveToFirst()) {
										String city = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY));
										mCity.setText(city);
									}
								} finally {
									c.close();
								}
								
								View v = getView();
								DisplayMetrics m = getActivity().getResources().getDisplayMetrics();
								int widthPixels = m.widthPixels;
								
								int width = (widthPixels - v.getPaddingLeft() - v.getPaddingRight()) * 3 / 4;
								int imgMargin = 2;
								
								int imgWidth = width / 3 - imgMargin * 2;
								ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
								
								ArrayList<String> imgUrl = g.getImgs();
								
								int length = imgUrl.size() <= 3 ? imgUrl.size() : 3 ;
								
								for(int i=0; i<length; i++){
									ImageView img = new ImageView(getActivity());
									img.setLayoutParams(param);
									img.setPadding(2, 2, 2, 2);
									mGoodImages.addView(img);
									
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
