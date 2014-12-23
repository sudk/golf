package com.jason.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.golf.dialog.GProgressDialog;
import com.jason.golf.dialog.ProgressDialog;
import com.jason.golf.dialog.WarnDialog;
import com.jason.network.HttpConnection;
import com.jason.network.HttpResponse;
import com.jason.network.ProtocolDefinition;
import com.jason.golf.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

public class HttpRequest implements Runnable, Callback {
	private static final String TAG = "HttpRequest";
	
	private static final int MaxReconnectTimes = 5;
	
	public static final int SUCCESS            = 200;
	public static final int IO_ERROR           = 9001;
	public static final int SAVE_SESSION       = 9004;
	public static final int BEGIN_TRANSMISSION = 10000;

	private FragmentActivity _fgAct;
	private JSONObject _params;
	private Handler _handler;
	private HttpCallback _callBack;
	private GProgressDialog _progressDialog;
	
	private int _reconnectTime = 0;

	public HttpRequest(FragmentActivity context, JSONObject params, HttpCallback callBack) {
		_reconnectTime = 0;
		_fgAct = context;
		_params = params;
		_callBack = callBack;
		_handler = new Handler(context.getMainLooper(), this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		GolfAppliaction app = (GolfAppliaction) _fgAct.getApplicationContext();
		GAccount acc = app.getAccount();
		String session = acc.getSession();
		
		HttpConnection conn = HttpConnection.CreateHttpConnection();
		System.out.println(_params.toString());
		HttpResponse r = conn.sendRequestInPost(ProtocolDefinition.COMMANDURL, _params.toString(), session, _handler);
		System.out.println(String.format("%d, %s", r.responseCode, r.content));
		
		acc = null;
	}
	
	public void showProgressDialog(){
		
		if (_progressDialog == null) {

			_progressDialog = new GProgressDialog(_fgAct);
			_progressDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					_callBack = null;
				}
			});
			_progressDialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					_callBack = null;
				}
			});

			_progressDialog.show();
		} else {
			if (!_progressDialog.isShowing()) {
				_progressDialog.show();
			}
		}
		
	}
	
	public void dissmisProgressDialog(){
		_progressDialog.dismiss();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case BEGIN_TRANSMISSION:
			
			showProgressDialog();
			
			break;
		case SUCCESS:
			
			String res = (String) msg.obj;
			
			if(!TextUtils.isEmpty(res)){
				
				try {
					JSONObject resObj = new JSONObject(res);
					
					int status = resObj.getInt("status");
					String des = resObj.getString("desc");
//					String des = "此处有错！！";
					
					switch(status){
					case 0:
						String dataStr ;
						if( resObj.has("data") ){
							Object obj = resObj.get("data");
							if (obj instanceof JSONObject) {
								dataStr = ((JSONObject) obj).toString();
							}else if(obj instanceof JSONArray){
								dataStr = ((JSONArray) obj).toString();
							}else
								dataStr = "";
						}else{
							dataStr = "";
						}
						
						if(_callBack != null) _callBack.sucessData(dataStr);
						
						break;
					case -1:
						
						
						
						
						
						Log.e(TAG, "Session Timeout");
						
						break;
					default:
						if(_callBack != null) _callBack.faildData(status, des);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "Json 解析失败");
				} finally {
					dissmisProgressDialog();
				}
				
			}
			
			break;
		case IO_ERROR:
			
			_reconnectTime ++ ;
			
			if(_reconnectTime > MaxReconnectTimes){
				
				if (_callBack != null) _callBack.timeout((String) msg.obj);
				
				if(_fgAct == null) break; 
	
				WarnDialog dialogTimeout = new WarnDialog(_fgAct);
				dialogTimeout
						.setTitle(R.string.network)
						.setMessage(R.string.network_time_out)
						.setPositiveBtn(R.string.retry,
								new DialogInterface.OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog,	int which) {
										// TODO Auto-generated method stub
										_reconnectTime = 0;
										GThreadExecutor.execute(HttpRequest.this);
									}
								})
						.setNegativeBtn(R.string.cancel,
								new DialogInterface.OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog,	int which) {
										// TODO Auto-generated method stub
										
									}
								});
				
				dissmisProgressDialog();
				
				if( !_fgAct.isFinishing()){
					
					FragmentTransaction transaction = _fgAct.getSupportFragmentManager().beginTransaction();
                    transaction.add(dialogTimeout, "TimeOut");
                    transaction.commitAllowingStateLoss();  
					
				}
				
			}else{
				
				try {
					Thread.sleep(2000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					
					GThreadExecutor.execute(this);
				}
				
				
//				(new Handler(_fgAct.getMainLooper())).postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						GThreadExecutor.execute(this);
//					}
//				}, 2000);
				
//				new Handler().postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//					}
//				}, 2000);
				
			}
			
			break;
			
//			if(_callBack != null) _callBack.malformedURL((String) msg.obj);
//			
//			break;
//		case 9002:
//			if (_callBack != null) _callBack.timeout((String) msg.obj);
//			
//			if(_fgAct == null) break; 
//
//			WarnDialog dialogTimeout = new WarnDialog(_fgAct);
//			dialogTimeout
//					.setTitle(R.string.network)
//					.setMessage(R.string.network_time_out)
//					.setPositiveBtn(R.string.retry,
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//									GThreadExecutor.execute(HttpRequest.this);
//								}
//							})
//					.setNegativeBtn(R.string.cancel,
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//
//								}
//							});
//			
//			if( !_fgAct.isFinishing())
//				dialogTimeout.show(_fgAct.getSupportFragmentManager(), "TimeOut");
//			
//			break;
//		case 9003:
//			if(_callBack != null) _callBack.ioError((String) msg.obj);
//			
//			WarnDialog dialogDisconnected = new WarnDialog(_fgAct);
//			dialogDisconnected.setTitle(R.string.network)
//			.setMessage(R.string.network_disconnect)
//			.setPositiveBtn(R.string.retry, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					GThreadExecutor.execute(HttpRequest.this);
//				}
//			}).setNegativeBtn(R.string.cancel, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//			
//			if( !_fgAct.isFinishing())
//				dialogDisconnected.show(_fgAct.getSupportFragmentManager(), "Disconnect");
//			
//			break;
			
		case SAVE_SESSION:// 保存session
		{
			String session = (String) msg.obj;
			if (!TextUtils.isEmpty(session)) {

				GolfAppliaction app = (GolfAppliaction) _fgAct.getApplicationContext();
				GAccount acc = app.getAccount();
				acc.setSession(session);
				
				System.out.println(acc.toString());
			}
			break;
		}
		
		default:
			if(_callBack != null) _callBack.other((String) msg.obj);
			dissmisProgressDialog();
		}
		
		return false;
	}
	
}
