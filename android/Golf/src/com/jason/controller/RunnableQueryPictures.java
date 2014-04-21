package com.jason.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.network.HttpConnection;
import com.jason.network.HttpResponse;
import com.jason.network.ProtocolDefinition;
import com.jason.storage.GolfStorage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

public class RunnableQueryPictures implements Runnable {
	
	private Context _context;
	private String _rb_id;
	private Handler _handler;
	
	public RunnableQueryPictures(Context c, String rb_id, Handler handler) {
		// TODO Auto-generated constructor stub
		_context = c;
		_rb_id = rb_id;
		_handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
//		/*
//		 * 
//		 * 
//		 *  查询 照片列表 
//		 * 
//		 * 
//		 */
//		
////		http://localhost/tbfinace/trunkifms/index.php?r=client/login/photo&bx_id=3000435&sid=000000
//		
//		GolfAppliaction app = (GolfAppliaction) _context.getApplicationContext();
//
//
//		HashMap<String, String> request = new HashMap<String, String>();
//		request.put("bx_id", _rb_id);
//		request.put("sid"  , app.getAccount().getSession());
//		
//		HttpConnection conn = HttpConnection.CreateHttpConnection();
////		HttpResponse r = conn.sendRequestInGet("http://192.168.4.114/tbfinance/trunkifms/index.php?r=client/login/photo", request);
//		HttpResponse r = conn.sendRequestInGet(String.format(ProtocolDefinition.BaseUrl, "photo"), request);
//		
//		request.clear();
//		request = null;
//		
//		ArrayList<String> netIdArray = new ArrayList<String>();
//		HashMap<String, String> localIdArray = new HashMap<String, String>();
//		
//		try {
//			JSONObject obj = new JSONObject(r.content);
//			
//			String result = obj.getString("result");
//			
//			/*
//			 * 
//			 *  查询没有数据，删掉本地所有图片
//			 * 
//			 */
//			
//			if(ProtocolDefinition.ResponseCode.NODATA.equals(result)){
//				
//				ContentResolver cr = _context.getContentResolver();
//				
//				Cursor c = cr.query(GolfProviderConfig.Pictures.CONTENT_URI, 
//						new String[]{ Pictures._ID, Pictures.PIC_ID, Pictures.PIC_PATH }, 
//						Pictures.RB_ID + "=? ", 
//						new String[]{ _rb_id },
//						null);
//				
//				if(c != null){
//					try{
//						
//						if(c.moveToFirst()){
//							do {
//								localIdArray.put(
//										c.getString(c.getColumnIndex(Pictures.PIC_ID)),
//										c.getString(c.getColumnIndex(Pictures.PIC_PATH)));
//							} while (c.moveToNext());
//						}
//						
//					}finally{
//						c.close();
//					}
//					
//				}
//				
//				
//				for(String id : localIdArray.keySet()){
//					
//					GolfStorage.DeleteFile(localIdArray.get(id));
//
//					cr.delete(
//							GolfProviderConfig.Pictures.CONTENT_URI,
//							Pictures.PIC_ID + "=? ", 
//							new String[] { id }
//						);
//
//					Log.w("QueryPictures", String.format("删除图片的ID是%s", id));
//					
//				}
//				
//			}
//			
//			/*
//			 * 
//			 *  查询有数据。比对删除照片
//			 * 
//			 */
//			
//			if(ProtocolDefinition.ResponseCode.SUCCESS.equals(result)){
//			
//				JSONArray picArray = obj.getJSONArray("datalist");
//				
//				ContentResolver cr = _context.getContentResolver();
//				
////				System.out.println(String.format("查询到%d张图片", picArray.length()));
//				
//				
//				/*
//				 * 
//				 * 
//				 *  获取网络照片id
//				 * 
//				 * 
//				 */
//				
//				for(int i=0; i<picArray.length(); i++){
//				
//					JSONObject picObj = picArray.getJSONObject(i);
//					netIdArray.add(picObj.getString("pic_id"));
//					
//				}
//				
//				Log.w("QueryPictures",String.format("查询到网络图片%d张",picArray.length()));
//
//				/*
//				 * 
//				 * 获取本地照片ID
//				 * 
//				 */
//				
//				Cursor c = cr.query(GolfProviderConfig.Pictures.CONTENT_URI, 
//						new String[]{ Pictures._ID, Pictures.PIC_ID, Pictures.PIC_PATH }, 
//						Pictures.RB_ID + "=? ", 
//						new String[]{ _rb_id },
//						null);
//				
//				if(c != null){
//					try{
//						
//						if(c.moveToFirst()){
//							do {
//								localIdArray.put(
//										c.getString(c.getColumnIndex(Pictures.PIC_ID)),
//										c.getString(c.getColumnIndex(Pictures.PIC_PATH)));
//							} while (c.moveToNext());
//						}
//						
//					}finally{
//						c.close();
//					}
//					
//				}
//				
//				Log.w("QueryPictures",String.format("查询到本地图片%d张",localIdArray.size()));
//				
//				/*
//				 * 
//				 *  对比删除照片
//				 * 
//				 */
//				
//				if(!localIdArray.isEmpty()){
//					
//					for (String id : localIdArray.keySet()) {
//						if(!netIdArray.contains(id)){
//							
//							GolfStorage.DeleteFile(localIdArray.get(id));
//							
//							cr.delete(
//									GolfProviderConfig.Pictures.CONTENT_URI, 
//									Pictures.PIC_ID + "=? ", 
//									new String[] {id} );
//							
//							Log.w("QueryPictures",String.format("删除图片的ID是%s",id));
//					
//						}
//					}
//					
//				}
//				
//				
//				
//				/*
//				 * 
//				 * 
//				 *  插入新的照片
//				 * 
//				 * 
//				 */
//				
//				for(int i=0; i<picArray.length(); i++){
//			
//					JSONObject picObj = picArray.getJSONObject(i);
//					
//					String picUrl = picObj.getString("url");
//					String picId = picObj.getString("pic_id");
//					
//					System.out.println("RunnableQueryPictures=>" + picUrl);
//
//					ContentValues values = new ContentValues();
//					values.put(GolfProviderConfig.Pictures.PIC_ID, picId);
//					values.put(GolfProviderConfig.Pictures.PIC_PATH, "");
//					values.put(GolfProviderConfig.Pictures.PIC_URL, picUrl);
//					values.put(GolfProviderConfig.Pictures.RB_ID, _rb_id);
//
//					cr.insert(GolfProviderConfig.Pictures.CONTENT_URI, values);
//				}
//			}
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			netIdArray.clear();
//			netIdArray = null;
//			localIdArray.clear();
//			localIdArray = null;
//			
//			if(_handler != null)
//				_handler.sendEmptyMessage(0);
//		}
		
	}

}
