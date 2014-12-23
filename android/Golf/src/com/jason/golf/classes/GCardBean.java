package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GCardBean {

	private String id;
	private String card_name;
	private String desc;
	private String user_id;
	private String card_no;
	private String img;

	public boolean initialize(JSONObject data) {

		try {
			id = data.getString("id");
			card_name = data.getString("card_name");
			desc = data.getString("desc");
			user_id = data.getString("user_id");
			card_no = data.getString("card_no");
			img = data.getString("img");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	public String getId() {
		return id;
	}

	public String getCardName() {
		return card_name;
	}

	public String getDesc() {
		return desc;
	}

	public String getUserId() {
		return user_id;
	}

	public String getCardNo() {
		return card_no;
	}

	public String getImg() {
		return img;
	}

}
