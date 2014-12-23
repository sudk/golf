package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GComment {

	private String courtId;
	private String service;
	private String design;
	private String facilitie;
	private String lawn;
	private String desc;
	private String userId;
	private String recordTime;

	public boolean initialize(JSONObject data) {

		try {

			this.courtId = data.getString("court_id");
			this.service = data.getString("service");
			this.design = data.getString("design");
			this.facilitie = data.getString("facilitie");
			this.lawn = data.getString("lawn");
			this.desc = data.getString("desc");
			this.userId = data.getString("user_id");
			this.recordTime = data.getString("record_time");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	public String getCourtId() {
		return courtId;
	}

	public String getService() {
		return service;
	}

	public String getDesign() {
		return design;
	}

	public String getFacilitie() {
		return facilitie;
	}

	public String getLawn() {
		return lawn;
	}

	public String getDesc() {
		return desc;
	}

	public String getUserId() {
		return userId;
	}

	public String getRecordTime() {
		return recordTime;
	}

}
