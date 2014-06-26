package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GCourtBean {

	private String court_id;
	private String name;
	private String city;

	public boolean initialize(JSONObject obj) {

		try {
			court_id = obj.getString("court_id");
			name = obj.getString("name");
			city = obj.getString("city");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public String getCity() {
		return city;
	}
	
	
}
