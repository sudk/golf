package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GHandicapBean {

	private String fee_time;
	private String court_name;
	private String user_name;
	private String handicap;

	public boolean initialize(JSONObject data) {

		try {
			fee_time = data.getString("fee_time");
			court_name = data.getString("court_name");
			user_name = data.getString("user_name");
			handicap = data.getString("handicap");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public String getFeeTime() {
		return fee_time;
	}

	public String getCourt_name() {
		return court_name;
	}

	public String getUserName() {
		return user_name;
	}

	public String getHandicap() {
		return handicap;
	}

}
