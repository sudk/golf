package com.jason.golf.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class GMerchant {
	
	private String _id;
	private String _courtId;
	private String _courtName;
	private String _city;
	private String _facilitieName;
	private String _type;
	private String _feature;
	private String _consumption;
	private String _favourable;
	private String _phone;
	private String _addr;
	private String _distance;
	private ArrayList<String> _imgs;
	
	public static String GetTypeDesc(String type){
		String res = "";
		
//		1、主食；2、住宿；、3、娱乐；4、购物；5、会议；6、其它

		if ("1".equals(type))
			res = "美食";
		if ("2".equals(type))
			res = "住宿";
		if ("3".equals(type))
			res = "娱乐";
		if ("4".equals(type))
			res = "购物";
		if ("5".equals(type))
			res = "会议";
		if ("6".equals(type))
			res = "其他";
		
		return res;
	}
	
	public boolean initialize(JSONObject data){
		
		try {
			_id = data.getString("id");
			_courtId = data.getString("court_id");
			_courtName = data.getString("court_name");
			_city = data.getString("city");
			_facilitieName = data.getString("facilitie_name");
			_type = data.getString("type");
			_feature = data.getString("feature");
			_consumption = data.getString("consumption");
			_favourable = data.getString("favourable");
			_phone = data.getString("phone");
			_addr = data.getString("addr");
			_distance = data.getString("distance");
			_imgs = new ArrayList<String>();
			
			JSONArray imgs = data.getJSONArray("imgs");
			
			for(int i=0,length=imgs.length(); i<length ; i++){
				_imgs.add((String) imgs.get(i));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		return true;
	}
	
	
	public String getId() {
		return _id;
	}
	public String getCourt_id() {
		return _courtId;
	}
	public String getCourtName() {
		return _courtName;
	}
	public String getCity() {
		return _city;
	}
	public String getFacilitieName() {
		return _facilitieName;
	}
	public String getType() {
		return _type;
	}
	public String getFeature() {
		return _feature;
	}
	public int getConsumption() {
		
		int p = 0;
		if( !TextUtils.isEmpty(_consumption) && TextUtils.isDigitsOnly(_consumption) ){
			
			p = Integer.parseInt(_consumption);
			
		}
		
		return p;
	}
	public String getFavourable() {
		return _favourable;
	}
	public String getPhone() {
		return _phone;
	}
	public String getAddr() {
		return _addr;
	}
	public String getDistance() {
		return _distance;
	}
	public ArrayList<String> getImgs() {
		return _imgs;
	}
	

}
