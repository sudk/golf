package com.jason.controller;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.network.HttpConnection;
import com.jason.network.HttpResponse;
import com.jason.network.ProtocolDefinition;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;


public class RunnableQueryReimburse implements Runnable {

	Context _context;
	Handler _handler;
	public RunnableQueryReimburse(Context ctx , Handler handler) {
		_context = ctx;
		_handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

//		GolfAppliaction app = (GolfAppliaction) _context.getApplicationContext();
//		GAccount acc = app.getAccount();
//
//		HashMap<String, String> request = new HashMap<String, String>();
//		request.put("uid", acc.getId());
//		request.put("sid", acc.getSession());
//
//		HttpConnection conn = HttpConnection.CreateHttpConnection();
//		HttpResponse r = conn.sendRequestInGet(String.format(
//				ProtocolDefinition.BaseUrl, ProtocolDefinition.QueryReimburse),
//				request);
//
//		// {"datalist":[{"money":"1000","id":"3000435","name":"钱栋亮-彩印运营-2014-01-22222"},{"money":0,"id":"3000436","name":"钱栋亮-彩印运营-2014-01-报销2"},{"money":0,"id":"3000437","name":"钱栋亮-彩印运营-2014-01-报销3333"}],"result":"0000"}
//
//		try {
//			
//			ContentResolver cr = _context.getContentResolver();
//			
//			JSONObject obj = new JSONObject(r.content);
//			String result = obj.getString("result");
//			if ("0000".equals(result)) {
//				
//				JSONArray array = obj.getJSONArray("datalist");
//				for(int index=0; index<array.length(); index++){
//					
//					JSONObject rb = array.getJSONObject(index);
//					
//					ContentValues values = new ContentValues();
//					values.put(GolfProviderConfig.Court.RB_ID, rb.getString("id"));
//					values.put(GolfProviderConfig.Court.RB_NAME, rb.getString("name"));
//					values.put(GolfProviderConfig.Court.RB_AMOUNT, rb.getLong("money"));
//					cr.insert(GolfProviderConfig.Court.CONTENT_URI, values);
//					
//				}
//				
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			if(_handler != null){
//				_handler.sendEmptyMessage(0);
//			}
//		}

	}

}
