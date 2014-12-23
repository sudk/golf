package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GScore {
	
	private String id;
	private String courtId;
	private String holes;
	private String feeTime;
	private String isShow;
	private String teamMenbers;
	private String courtName;
	private String recordTime;

	public boolean initialize(JSONObject item) {
		// TODO Auto-generated method stub
		
		try {
			id = item.getString("id");
			courtId = item.getString("court_id");
			holes = item.getString("holes");
			feeTime = item.getString("fee_time");
			isShow = item.getString("is_show");
			teamMenbers = item.getString("team_menbers");
			
			if(item.has("court_name")){
				courtName = item.getString("court_name");
			}
			
			recordTime = item.getString("record_time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		return true;
	}

	public String getId() {
		return id;
	}

	public String getCourtId() {
		return courtId;
	}

	public String getHoles() {
		return holes;
	}

	public String getFeeTime() {
		return feeTime;
	}

	public String getIsShow() {
		return isShow;
	}

	public String getTeamMembers() {
		return teamMenbers;
	}

	public String getCourtName() {
		return courtName;
	}

	public String getRecord_time() {
		return recordTime;
	}

	public void setCourtId(String cid) {
		// TODO Auto-generated method stub
		courtId = cid; 
	}
	
	

}
