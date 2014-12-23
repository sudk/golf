package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GScoreDetailBean {
	
	
	public static String GetTeeDesc(String tee){
		
		//，Tee台；1、黑Tee；2、金Tee；3、蓝Tee；4、白Tee；5、红Tee
		
		String str = "";

		if ("1".equals(tee))
			str = "黑色Tee台";
		if ("2".equals(tee))
			str = "金色Tee台";
		if ("3".equals(tee))
			str = "蓝色Tee台";
		if ("4".equals(tee))
			str = "白色Tee台";
		if ("5".equals(tee))
			str = "红色Tee台";
		
		return str;
		
	}
	

	private String id;
	private String score_id;
	private int hole_no;
	private String tee;
	private String standard_bar;
	private String lang_bar;
	private String push_bar;

	public boolean initialize(JSONObject item) {
		// TODO Auto-generated method stub

		try {
			id = item.getString("id");
			score_id = item.getString("score_id");
			hole_no = item.getInt("hole_no");
			tee = item.getString("tee");
			standard_bar = item.getString("standard_bar");
			lang_bar = item.getString("lang_bar");
			push_bar = item.getString("push_bar");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	public String getId() {
		return id;
	}

	public String getScoreId() {
		return score_id;
	}

	public int getHoleNo() {
		return hole_no;
	}

	public String getTee() {
		return tee;
	}

	public String getStandardBar() {
		return standard_bar;
	}

	public String getLangBar() {
		return lang_bar;
	}

	public String getPushBar() {
		return push_bar;
	}

	public int getTotal() {
		// TODO Auto-generated method stub
		int total;
		
		int l = Integer.parseInt(lang_bar);
		int p = Integer.parseInt(push_bar);
		
		total = l + p;
		
		return total;
	}

}
