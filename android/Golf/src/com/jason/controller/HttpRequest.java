package com.jason.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.golf.dialog.ProgressDialog;
import com.jason.golf.dialog.WarnDialog;
import com.jason.network.HttpConnection;
import com.jason.network.HttpResponse;
import com.jason.network.ProtocolDefinition;
import com.jason.golf.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

public class HttpRequest implements Runnable, Callback {
	private static final String TAG = "HttpRequest";

	private FragmentActivity _fgAct;
	private JSONObject _params;
	private Handler _handler;
	private HttpCallback _callBack;
	private ProgressDialog _progressDialog;

	public HttpRequest(FragmentActivity context, JSONObject params, HttpCallback callBack) {
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
		System.out.println(r.content);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case 10000:
			
			_progressDialog = new ProgressDialog(_fgAct);
			_progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					_callBack = null;
				}
			})
			.setOnDismissListener(new DialogInterface.OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					_callBack = null;
				}
			});
			
			_progressDialog.show(_fgAct.getSupportFragmentManager(), "ProgressBarDialog");
			break;
			
		case 10001:
			_progressDialog.dismissAllowingStateLoss();
			break;
		case 200:
			
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
				}
				
			}
			
			break;
		case 9001:
			
			if(_callBack != null) _callBack.malformedURL((String) msg.obj);
			
			break;
		case 9002:
			if (_callBack != null) _callBack.timeout((String) msg.obj);
			
			if(_fgAct == null) break; 

			WarnDialog dialogTimeout = new WarnDialog(_fgAct);
			dialogTimeout
					.setTitle(R.string.network)
					.setMessage(R.string.network_time_out)
					.setPositiveBtn(R.string.retry,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									GThreadExecutor.execute(HttpRequest.this);
								}
							})
					.setNegativeBtn(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
			
			if( !_fgAct.isFinishing())
				dialogTimeout.show(_fgAct.getSupportFragmentManager(), "TimeOut");
			
			break;
		case 9003:
			if(_callBack != null) _callBack.ioError((String) msg.obj);
			
			WarnDialog dialogDisconnected = new WarnDialog(_fgAct);
			dialogDisconnected.setTitle(R.string.network).setMessage(R.string.network_disconnect)
			.setPositiveBtn(R.string.retry, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					GThreadExecutor.execute(HttpRequest.this);
				}
			}).setNegativeBtn(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			if( !_fgAct.isFinishing())
				dialogDisconnected.show(_fgAct.getSupportFragmentManager(), "Disconnect");
			
			break;
		case 9004:// 保存session
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
		}
		
		
		
		
		
		
		return false;
	}
	
}
