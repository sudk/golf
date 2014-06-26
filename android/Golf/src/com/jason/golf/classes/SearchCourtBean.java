package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.text.TextUtils;

public class SearchCourtBean {

	private String court_id;
	private String name;
	private String addr;
	private long _distance;
	private String price;
	private String vip_price;
	private String pledge_price;
	private String ico_img;
	private String pay_type;

	public boolean initialize(JSONObject obj) {

		try {
			court_id = obj.getString("court_id");
			name = obj.getString("name");
			addr = obj.getString("addr");
			setDistance(obj.getString("distance"));
			price = obj.getString("price");
			vip_price = obj.getString("vip_price");
			pledge_price = obj.getString("pledge_price");
			ico_img = obj.getString("ico_img");
			pay_type = obj.getString("pay_type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		return true;
	}

	public String getCourtId() {
		return court_id;
	}

	public String getName() {
		return name;
	}

	public String getAddr() {
		return addr;
	}

	public int getPrice() {
		
		if(TextUtils.isDigitsOnly(price)){
			int p = Integer.parseInt(price);
			return p;
		}else{
			
			return 0;
		}
	}

	public int getVipPrice() {
		if(TextUtils.isDigitsOnly(vip_price)){
			int p = Integer.parseInt(vip_price);
			return p;
		}else{
			
			return 0;
		}
	}

	public String getPledgePrice() {
		return pledge_price;
	}

	public String getIcoImg() {
		return ico_img;
	}

	public String getPayType() {
		return pay_type;
	}

	public long getDistance() {
		return _distance;
	}

	public void setDistance(String distance) {

		// System.out.println("distance " + distance);
		try {
			float f = Float.parseFloat(distance);
			long d = (long) f;
			this._distance = d;
		} catch (NumberFormatException e) {
			this._distance = -1;// -1 代表未知
		}

	}

}
