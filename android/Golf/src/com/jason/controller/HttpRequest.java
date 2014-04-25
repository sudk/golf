package com.jason.controller;

import org.json.JSONObject;

import com.jason.network.HttpConnection;
import com.jason.network.HttpResponse;
import com.jason.network.ProtocolDefinition;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class HttpRequest implements Runnable, Callback {

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
		HttpConnection conn = HttpConnection.CreateHttpConnection();
		System.out.println(_params.toString());
		HttpResponse r = conn.sendRequestInPost(ProtocolDefinition.COMMANDURL, _params.toString(), _handler);
		System.out.println(r.content);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case 200:
			if(_callBack != null) _callBack.sucess((String) msg.obj);
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
		default:
			if(_callBack != null) _callBack.other((String) msg.obj);
		}
		
		return false;
	}
	
}
