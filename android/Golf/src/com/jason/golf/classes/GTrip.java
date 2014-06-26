package com.jason.golf.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class GTrip {
	
	public static String GetPayTypeDes(String payType){
		
		String res = "";

		if ("0".equals(payType))
			res = "球场现付";
		if ("1".equals(payType))
			res = "全额预付";
		if ("2".equals(payType))
			res = "押金";

		return res;
		
	}

	// "id": "20140417233418367952",
	// "agent_id": "1",
	// "trip_name": "珠海金湾高尔夫1天1晚1场球",
	// "city": "440100",
	// "desc": "aaa5555",
	// "normal_price": "700",
	// "holiday_price": "1200",
	// "other_price": "1000",
	// "record_time": "2014-04-17 23:34:18",
	// "start_date": "2014-04-17 00:00:00",
	// "end_date": "2014-04-18 00:00:00",
	// "pay_type": "0",
	// "is_check": "0",
	// "creatorid": "admin",
	// "court_id": "admin20140409220421",
	// "court_name": "广州珠海金湾高尔夫444",
	// "agent_name": "高尔夫代理商001",
	// "imgs": [
	// "http://115.28.77.119/images/picture/20140426/1157365499.png"
	// ]

	private String _id;
	private String _agentId;
	private String _agentName;
	private String _tripName;
	private String _city;
	private String _desc;
	private int _normalPrice;
	private int _holidayPrice;
	private String _otherPrice;
	private String _recordTime;
	private String _startDate;
	private String _endDate;
	private String _payType;
	private String _isCheck;
	private ArrayList<String> _imgs;

	private String _courtId;
	private String _courtName;

	public boolean initialize(JSONObject obj) {

		try {
			_id = obj.getString("id");
			_agentId = obj.getString("agent_id");
			_agentName = obj.getString("agent_name");
			_tripName = obj.getString("trip_name");
			_city = obj.getString("city");
			_desc = obj.getString("desc");
			
			String p = obj.getString("normal_price");
			
			if(TextUtils.isEmpty(p) || !TextUtils.isDigitsOnly(p)){
				_normalPrice = 0;
			}else{
				_normalPrice = Integer.parseInt(p);
			}
			
			p = obj.getString("holiday_price");
			
			if(TextUtils.isEmpty(p) || !TextUtils.isDigitsOnly(p)){
				_holidayPrice = 0;
			}else{
				_holidayPrice = Integer.parseInt(p);
			}
			
			_otherPrice = obj.getString("other_price");
			_recordTime = obj.getString("record_time");
			_startDate = obj.getString("start_date");
			_endDate = obj.getString("end_date");
			_payType = obj.getString("pay_type");
			
			_courtId = obj.getString("court_id");
			_courtName = obj.getString("court_name");
			_isCheck = obj.getString("is_check");

			_imgs = new ArrayList<String>();

			if (obj.has("imgs")) {
				JSONArray imgs = obj.getJSONArray("imgs");
				for (int i = 0, length = imgs.length(); i < length; i++) {

					_imgs.add((String) imgs.get(i));

				}
			}
			
			if(obj.has("img")){
				_imgs.add(obj.getString("img"));
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	public String getCourtId() {
		return _courtId;
	}

	public String getCourtName() {
		return _courtName;
	}

	public String getId() {
		return _id;
	}

	public String getAgentId() {
		return _agentId;
	}

	public String getAgentName() {
		return _agentName;
	}

	public String getTripName() {
		return _tripName;
	}

	public String getCity() {
		return _city;
	}

	public String getDesc() {
		return _desc;
	}

	public int getNormalPrice() {
		return _normalPrice;
	}

	public int getHolidayPrice() {
		return _holidayPrice;
	}

	public String getOtherPrice() {
		return _otherPrice;
	}

	public String getRecordTime() {
		return _recordTime;
	}

	public String getStartDate() {
		return _startDate;
	}

	public String getEndDate() {
		return _endDate;
	}

	public String getPayType() {
		return _payType;
	}

	public String getIsCheck() {
		return _isCheck;
	}

	public ArrayList<String> getImgs() {
		return _imgs;
	}

}
