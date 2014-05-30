package com.jason.golf.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GGood {

	private String _id;
	private String _title;
	private String _city;
	private String _desc;
	private String _price;
	private String _contact;
	private String _phone;
	private String _record_time;
	private String _status;
	private ArrayList<String> _imgs;
	
	public boolean initialize(JSONObject item) {
		// TODO Auto-generated method stub
		
		try {
			_id = item.getString("id");
			_title = item.getString("title");
			_city = item.getString("city");
			_desc = item.getString("desc");
			_price = item.getString("price");
			_contact = item.getString("contact");
			_phone = item.getString("phone");
			_record_time = item.getString("record_time");
			
			JSONArray imgs = item.getJSONArray("imgs");
			_imgs = new ArrayList<String>();
			for(int i=0,length=imgs.length(); i<length; i++){
				_imgs.add((String) imgs.get(i));
			}
			
			_status = item.getString("status");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		
		return true;
	}


	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		this._title = title;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String city) {
		this._city = city;
	}

	public String getDesc() {
		return _desc;
	}

	public void setDesc(String desc) {
		this._desc = desc;
	}

	public String getPrice() {
		return _price;
	}

	public void setPrice(String price) {
		this._price = price;
	}

	public String getContact() {
		return _contact;
	}

	public void setContact(String contact) {
		this._contact = contact;
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		this._phone = phone;
	}

	public String getRecord_time() {
		return _record_time;
	}

	public void setRecord_time(String record_time) {
		this._record_time = record_time;
	}

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		this._status = status;
	}

	public ArrayList<String> getImgs() {
		return _imgs;
	}
	
}
