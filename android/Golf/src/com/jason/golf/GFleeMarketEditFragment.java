package com.jason.golf;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GGood;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.provider.GolfProviderConfig;
import com.jason.golf.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GFleeMarketEditFragment extends Fragment implements OnClickListener {
	
	public static final String KEY_GOOD_ID = "key_good_id";

	private static final int REQUESTCODE_CTIY = 1001;
	private static final int REQUESTCODE_PHOTO = 1002;
	
	
	private Button mAddImg, mSubmit;

	private String _cityId, _goodId;

	private String mCurrentPhotoPath;
	
	private EditText mTitle, mDesc, mPrice, mContact, mPhone;
	
	private LinearLayout mFleeImages;
	
	private TextView mCity;
	
	private TextWatcher _watcher;
	
	private ArrayList<String> _imgs;
	
	FinalBitmap _fb;
	
//	private String _googId = "";

	public static GFleeMarketEditFragment Instance() {
		return new GFleeMarketEditFragment();
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
		_goodId = getArguments().getString(KEY_GOOD_ID, "");
		_imgs = new ArrayList<String>();
		
		_fb = FinalBitmap.create(getActivity());
		_fb.configLoadingImage(R.drawable.test_golf);
		
		queryGood();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_flee_market_new, null);
		mAddImg = (Button) v.findViewById(R.id.flee_add_img);
		mAddImg.setOnClickListener(this);
		
		_watcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				checkSubmitButtonState();
			}
		};
		
		mTitle = (EditText) v.findViewById(R.id.flee_title);
		mTitle.addTextChangedListener(_watcher);
		mDesc = (EditText) v.findViewById(R.id.flee_desc);
		mDesc.addTextChangedListener(_watcher);
		mPrice = (EditText) v.findViewById(R.id.flee_price);
		mPrice.addTextChangedListener(_watcher);
		mContact = (EditText) v.findViewById(R.id.flee_contact);
		mContact.addTextChangedListener(_watcher);
		mPhone = (EditText) v.findViewById(R.id.flee_phone);
		mPhone.addTextChangedListener(_watcher);
		
		mFleeImages = (LinearLayout) v.findViewById(R.id.flee_images);
		
		mCity = (TextView) v.findViewById(R.id.flee_city);
		mCity.setOnClickListener(this);
		
		mSubmit = (Button) v.findViewById(R.id.flee_submit);
		mSubmit.setOnClickListener(this);
		
		checkSubmitButtonState();
		
		return v;
	}
	
	private void checkSubmitButtonState(){
		
		if( mTitle.length() == 0 || mDesc.length() == 0 || mPrice.length() == 0 || mContact.length() == 0 || mPhone.length() == 0 ){
			mSubmit.setEnabled(false);
		}else{
			mSubmit.setEnabled(true);
		}
		
	}
	
	
	/*
	 * 
	 * 
	 * 
	 *      获取 新建寄卖ID 
	 *  
	 * 
	 * 
	 */
	private void queryGood() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();

		try {
			
			params.put("cmd", "flea/info");
			params.put("id", _goodId);

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
							g.initialize(data);
							
							
							mTitle.setText(g.getTitle());
							
							int p = Integer.parseInt(g.getPrice());
							
							mPrice.setText(String.format("￥%.2f",(float)p/100));
							
							mDesc.setText(g.getDesc());
							mContact.setText(g.getContact());
							mPhone.setText(g.getPhone());
							
							_cityId = g.getCity();
							
							ContentResolver cr = getActivity().getContentResolver();

							Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
									GolfProviderConfig.City.CITY_ID + "=? ", new String[] { "" + _cityId }, null);

							if (c == null)
								return;

							try {
								if (c.moveToFirst()) {
									String city = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY));
									_cityId = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY_ID));
									mCity.setText(city);
								}
							} finally {
								c.close();
							}
							
							_imgs.clear();
							_imgs.addAll(g.getImgs());
							
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
								mFleeImages.addView(img);
								
								if(imgUrl.size() > i)
									_fb.display(img, imgUrl.get(i));
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.flee_add_img:
			
			Intent intent = new Intent();  
		    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);  
		    
		    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
		        
		        
		        File photoFile = null;
		        try {
		            photoFile = createImageFile();
		            
		            // Continue only if the File was successfully created
			        if (photoFile != null) {
			        	
			        	mCurrentPhotoPath = photoFile.getAbsolutePath();//保存文件地址
				        System.out.println(photoFile.getAbsolutePath());
			        	
			        	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			            startActivityForResult(intent, REQUESTCODE_PHOTO);
			        }
		            
		        } catch (IOException ex) {
		            // Error occurred while creating the File
		        	System.out.println("生成图片缓存文件失败");
		        	
		        }
		        
		    }
			
			break;
		case R.id.flee_submit:
			submitFleeGoods();
			break;
			
		case R.id.flee_city:
			startActivityForResult(new Intent(getActivity(), SelectCityActivity.class), REQUESTCODE_CTIY);
			break;
		}
	}
	
	private void submitFleeGoods() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		
		try {
			
			params.put("cmd", "flea/create");
			params.put("id", _goodId);
			params.put("title",mTitle.getText().toString());
			params.put("city",_cityId);
			params.put("desc",mDesc.getText().toString());
			
			String p = mPrice.getText().toString();
			params.put("price", Integer.parseInt(p) * 100);
			
			params.put("contact",mContact.getText().toString());
			params.put("phone",mPhone.getText().toString());
			
			JSONArray imgs = new JSONArray();
			
			for(String str : _imgs){
				imgs.put(str);
			}
			
			params.put("imgs", imgs);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void finalWork() {
				// TODO Auto-generated method stub
				super.finalWork();
			}

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				WarnDialog dialog = new WarnDialog(getActivity());
				dialog.setTitle(R.string.sale).setMessage("寄卖物品已经提交，正在等待等待审核。")
				.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((ActionBarActivity)getActivity()).onSupportNavigateUp();
					}
				});
				
				dialog.show(getFragmentManager(), "NewGoodsSuccess");
				
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
			}
			
		});
		
		GThreadExecutor.execute(r);
		
	}

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + ".jpg";
	    
	    File f = null;
		File appDir = null;

		if (!TextUtils.isEmpty(imageFileName)) {

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// »ñÈ¡SD¿¨µÄÄ¿Â¼
				File sdCardDir = Environment.getExternalStorageDirectory();

				appDir = new File(sdCardDir.toString(), "YunGaoGolf");
				if (!appDir.exists()) {
					System.out.println("图片目录 : " + appDir.getAbsolutePath());
					appDir.mkdirs();
				}

			} else {
				System.out.println("没有SD卡");
			}

			f = new File(appDir.getAbsolutePath(), imageFileName);
		}

	    return f;
	    
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUESTCODE_CTIY:

			if (resultCode != Activity.RESULT_OK)
				return;

			if (data == null)
				return;

			long cityRowId = data.getLongExtra("RowID", 0);

			System.out.println(String.format("requestCode = %d, RowId = %d", requestCode, cityRowId));

			ContentResolver cr = getActivity().getContentResolver();

			Cursor c = cr.query(GolfProviderConfig.City.CONTENT_URI, null,
					GolfProviderConfig.City._ID + "=? ", new String[] { "" + cityRowId }, null);

			if (c == null)
				return;

			try {
				if (c.moveToFirst()) {
					String city = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY));
					_cityId = c.getString(c.getColumnIndex(GolfProviderConfig.City.CITY_ID));
					mCity.setText(city);
				}
			} finally {
				c.close();
			}
			
			break;

		case REQUESTCODE_PHOTO:

			if (resultCode == Activity.RESULT_OK) {
				UploadPictureTask task = new UploadPictureTask();
				task.execute(new String[] { mCurrentPhotoPath, _goodId});
				
//				File f = new File(mCurrentPhotoPath);
//				if (f.exists()) {
//					
//					
//					System.out.println(String.format("图片文件大小： %d", f.length()));
//
//					try {
//						AjaxParams params = new AjaxParams();
//						params.put("id", _fleeGoodId);
//						params.put("my_file", f);
////						params.put("profile_picture", f); 
//						
//						FinalHttp fh = new FinalHttp();
//						fh.post("http://115.28.77.119/?r=cmd/flea/upload",
//								params, new AjaxCallBack() {
//									@Override
//									public void onLoading(long count, long current) {
//										System.out.println(String.format("count : %d; current : %d", count, current));
//									}
//
//									public void onSuccess(String t) {
//										System.out.println(String.format("onSuccess : %s", t));
//									}
//								});
//
//					} catch (FileNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} // 上传文件
//
//				}else{
//					System.out.println("文件不存在！！");
//					
//				}

			}

			break;
		}

	}

	
	/*
	 * 
	 * 
	 * 上传 寄卖物品图片。
	 *  
	 *  
	 * 
	 * 
	 */
	private class UploadPictureTask extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			dialog.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(getActivity());
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMax(100);
			dialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String path = params[0];
			File file = new File(path);
			
			if(file.exists())
				System.out.println("文件存在！！");
			else{
				System.out.println("文件不存在！！");
			}
			
			
			System.out.println("good id is "+params[1]);
			
			return uploadFile(file, params[1]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (TextUtils.isEmpty(result)) {

				Log.v("UpLoadfile", "无信息");
				
				WarnDialog dialog = new WarnDialog(getActivity());
				dialog.setTitle(R.string.sale)
						.setMessage("上传图片失败")
						.setPositiveBtn(R.string.confirm,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,	int arg1) {
										// TODO Auto-generated method stub

									}
								});
				
				dialog.show(getFragmentManager(), "UpLoadFileFaild");

			} else {

				try {
					System.out.println(result);
					
					JSONObject resObject = new JSONObject(result);
					
//					{"status":0,"msg":"\u6210\u529f","data":[{"url":"20140518\/2204296352.jpg"}]}
					int status = resObject.getInt("status");
					if (status == 0) {
						
						_imgs.clear();
						
						JSONArray data = resObject.getJSONArray("data");
						for (int i = 0, length = data.length(); i < length; i++) {
							JSONObject item = data.getJSONObject(i);
							String url = item.getString("url");
							_imgs.add(url);
						}
						
						View v = getView();
						DisplayMetrics m = getActivity().getResources().getDisplayMetrics();
						int widthPixels = m.widthPixels;
						
						int width = (widthPixels - v.getPaddingLeft() - v.getPaddingRight()) * 3 / 4;
						int imgMargin = 2;
						
						int imgWidth = width / 3 - imgMargin * 2;
						ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
						
						int length = _imgs.size() <= 3 ? _imgs.size() : 3 ;
						
						mFleeImages.removeAllViews();
						
						for(int i=0; i<length; i++){
							ImageView img = new ImageView(getActivity());
							img.setLayoutParams(param);
							img.setPadding(2, 2, 2, 2);
							Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
							img.setImageBitmap(bmp);
							mFleeImages.addView(img);
							
						}
						

					} else {

						WarnDialog dialog = new WarnDialog(getActivity());
						dialog.setTitle(R.string.sale)
								.setMessage("上传图片失败")
								.setPositiveBtn(R.string.confirm,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0, int arg1) {
												// TODO Auto-generated method
												// stub

											}
										});
						dialog.show(getFragmentManager(), "UpLoadFileFaild");

					}
					
					System.out.println(resObject.getString("msg"));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.v("UpLoadfile", String.format("json 数据无法解析"));
				}

			}

			dialog.dismiss();

		}

		private String uploadFile(File file, String goodId) {
			// String end = "\r\n";
			// String twoHyphens = "--";
			
			HttpURLConnection conn = null;
			
			try {

				GolfAppliaction app = (GolfAppliaction) getActivity().getApplication();
				GAccount acc = app.getAccount();

				String BOUNDARY = java.util.UUID.randomUUID ( ).toString ( ) ;  
                String PREFIX = "--" , LINEND = "\r\n" ;  
                String MULTIPART_FROM_DATA = "multipart/form-data" ;  
                String CHARSET = "UTF-8" ;  
  
                URL url = new URL("http://115.28.77.119/?r=cmd/flea/upload");
                conn = (HttpURLConnection) url.openConnection ( ) ;  
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout ( 5 * 1000 ) ; // 缓存的最长时间  
                conn.setDoInput ( true ) ;// 允许输入  
                conn.setDoOutput ( true ) ;// 允许输出  
                conn.setUseCaches ( false ) ; // 不允许使用缓存  
                conn.setRequestMethod ( "POST" ) ;  
                if(!TextUtils.isEmpty(acc.getSession())){
    				conn.setRequestProperty("Cookie", acc.getSession());
    			}
                conn.setRequestProperty ( "Connection" , "keep-alive" ) ;  
                conn.setRequestProperty ( "Charsert" , "UTF-8" ) ;  
                conn.setRequestProperty ( "Content-Type" , MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY ) ;  
				
				Map < String , String > params = new HashMap<String, String>();
				params.put("id", goodId);
//				params.put("my_file", file.getName());
				
				// 首先组拼文本类型的参数  
                StringBuilder sb = new StringBuilder ( ) ;  
                for ( Map.Entry < String , String > entry : params.entrySet ( ) )  
                    {  
                        sb.append ( PREFIX ) ;  
                        sb.append ( BOUNDARY ) ;  
                        sb.append ( LINEND ) ;  
                        sb.append ( "Content-Disposition: form-data; name=\""  + entry.getKey ( ) + "\"" + LINEND ) ;  
                        sb.append ( "Content-Type: text/plain; charset=" + CHARSET + LINEND ) ;  
                        sb.append ( "Content-Transfer-Encoding: 8bit" + LINEND ) ;  
                        sb.append ( LINEND ) ;  
                        sb.append ( entry.getValue ( ) ) ;  
                        sb.append ( LINEND ) ;  
                    }  
  
                DataOutputStream outStream = new DataOutputStream ( conn.getOutputStream ( ) ) ;  
                outStream.write ( sb.toString ( ).getBytes ( ) ) ;  
                
                Map<String, File> files = new HashMap<String, File>();
                files.put("my_file", file);
                
				if (files != null)
					for (Map.Entry<String, File> f : files.entrySet()) {
						StringBuilder sb1 = new StringBuilder();
						sb1.append(PREFIX);
						sb1.append(BOUNDARY);
						sb1.append(LINEND);
						sb1.append("Content-Disposition: form-data; name=\"my_file\"; filename=\""	+ f.getValue().getName() + "\"" + LINEND);
						sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
						sb1.append(LINEND);
						
						outStream.write(sb1.toString().getBytes());

						long fileLength = file.length();
						int total = 0;

						Log.d("UpLoad", String.format("file's length is %.2f", (float) fileLength / 1024 ));
						
						
						InputStream is = new FileInputStream(f.getValue());
						byte[] buffer = new byte[1024 * 5];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);

							total += len;
							this.publishProgress((int) (100 * total / fileLength));

//							Log.d("UpLoadFile",	String.format("%d/%d", total, fileLength));

						}

						is.close();
						outStream.write(LINEND.getBytes());
					}
				
                // 请求结束标志  
                byte [ ] end_data = ( PREFIX + BOUNDARY + PREFIX + LINEND ).getBytes ( ) ;  
                outStream.write ( end_data ) ;  
                outStream.flush ( ) ;  
				
                // 得到响应码  
				int res = conn.getResponseCode();
				InputStream in = conn.getInputStream();
				StringBuilder sb2 = new StringBuilder();
				if (res == 200) {
					int ch;
					while ((ch = in.read()) != -1) {
						sb2.append((char) ch);
					}
				}

				outStream.close();
                
                return sb2.toString ( ) ; 

			} catch (ConnectTimeoutException e) {
				
				Log.d("Upload", "ConnectTimeOut");

			} catch (Exception e) {
				// showDialog(""+e);
				Log.d("Upload", e.getMessage());
				
			}finally {
				
				if (conn != null) {
					conn.disconnect();
				}
				
			}

			return null;
		}

	}

}
