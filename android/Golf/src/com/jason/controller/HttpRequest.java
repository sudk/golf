package com.jason.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.network.HttpConnection;
import com.jason.network.HttpResponse;
import com.jason.network.ProtocolDefinition;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class HttpRequest implements Runnable, Callback {
	private static final String TAG = "HttpRequest";

	private Context _context;
	private JSONObject _params;
	private Handler _handler;
	private HttpCallback _callBack;

	public HttpRequest(Context context, JSONObject params, HttpCallback callBack) {
		_context = context;
		_params = params;
		_callBack = callBack;
		_handler = new Handler(context.getMainLooper(), this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		GolfAppliaction app = (GolfAppliaction) _context.getApplicationContext();
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
		case 200:
			
			String res = (String) msg.obj;
			
			if(!TextUtils.isEmpty(res)){
				
				try {
					JSONObject resObj = new JSONObject(res);
					
					int status = resObj.getInt("status");
					String des = resObj.getString("desc");
					
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
			if(_callBack != null) _callBack.timeout((String) msg.obj);
			break;
		case 9002:
			if(_callBack != null) _callBack.malformedURL((String) msg.obj);
			break;
		case 9003:
			if(_callBack != null) _callBack.ioError((String) msg.obj);
			break;
		case 9004:// 保存session
		{
			String session = (String) msg.obj;
			if (!TextUtils.isEmpty(session)) {

				GolfAppliaction app = (GolfAppliaction) _context.getApplicationContext();
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
