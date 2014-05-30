package com.jason.golf.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.text.TextUtils;

public class GCompetition {

	private String _id;
	private String _agentId;
	private String _agentName;
	private String _name;
	private String _desc;
	private String _fee;
	private String _startDate;
	private String _endDate;
	private String _plan;
	private String _feeInclude;
	private String _feeNotInclude;
	private String _feeType;
	private String _courtName;
	private String _courtId;
	private String _img;

	private ArrayList<String> _imgs;

	public boolean init(JSONObject data) {

		// "id": "guest_one20140424154852",
		// "agent_id": "1",
		// "court_id": "admin20140409220741",
		// "name": "2014年5月21至23日邀请赛",
		// "desc": "比赛第一，友谊第二",
		// "fee": "100000",
		// "start_date": "2014-05-21",
		// "end_date": "2014-05-23",
		// "plan": "就是比赛，比赛，比赛，比赛，比赛，比赛，比赛，比赛，比赛，比赛",
		// "fee_include": "除了没包括的......",
		// "fee_not_include": "除了包括的",
		// "record_time": "2014-04-24 15:48:52",
		// "fee_type": "1",
		// "creatorid": "guest_one",
		// "court_name": "广州珠海金湾高尔夫",
		// "agent_name": "高尔夫代理商001",
		// "imgs": [
		// "http://115.28.77.119/images/picture/20140424/1748077747.png",
		// "http://115.28.77.119/images/picture/20140424/1748071998.png"
		// ]

		try {
			setId(data.getString("id"));
			setAgentId(data.getString("agent_id"));
			setCourtId(data.getString("court_id"));
			setName(data.getString("name"));
			setDesc(data.getString("desc"));
			setFee(data.getString("fee"));
			setStartDate(data.getString("start_date"));
			setEndDate(data.getString("end_date"));
			setPlan(data.getString("plan"));
			setFeeInclude(data.getString("fee_include"));
			setFeeNotInclude(data.getString("fee_not_include"));
			setFeeType(data.getString("fee_type"));
			setCourtName(data.getString("court_name"));
			setAgentName(data.getString("agent_name"));
			
			if (data.has("img")) {
				setImg(data.getString("img"));
			}

			if (data.has("imgs")) {
				JSONArray imgs = data.getJSONArray("imgs");
				setImgs(imgs);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	public String getImg() {
		return _img;
	}

	public void setImg(String img) {
		this._img = img;
	}

	public ArrayList<String> getImgs() {
		return _imgs;
	}

	public void setImgs(JSONArray imgs) throws JSONException {
		if (_img == null) {
			_imgs = new ArrayList<String>();
		}
		_imgs.clear();

		for (int i = 0, length = imgs.length(); i < length; i++) {
			_imgs.add(imgs.getString(i));
		}
	}

	public String getCourtId() {
		return _courtId;
	}

	public void setCourtId(String courtId) {
		this._courtId = courtId;
	}

	public String getCourtName() {
		return _courtName;
	}

	public void setCourtName(String courtName) {
		this._courtName = courtName;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getAgentId() {
		return _agentId;
	}

	public void setAgentId(String agentId) {
		this._agentId = agentId;
	}

	public String getAgentName() {
		return _agentName;
	}

	public void setAgentName(String agentName) {
		this._agentName = agentName;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getDesc() {
		return _desc;
	}

	public void setDesc(String desc) {
		this._desc = desc;
	}

	public int getFee() {

		int res = 0;

		if (!TextUtils.isEmpty(_fee) && TextUtils.isDigitsOnly(_fee)) {
			res = Integer.parseInt(_fee);
		}

		return res;
	}

	public void setFee(String fee) {
		this._fee = fee;
	}

	public String getStartDate() {
		return _startDate;
	}

	public void setStartDate(String startDate) {
		this._startDate = startDate;
	}

	public String getEndDate() {
		return _endDate;
	}

	public void setEndDate(String endDate) {
		this._endDate = endDate;
	}

	public String getPlan() {
		return _plan;
	}

	public void setPlan(String plan) {
		this._plan = plan;
	}

	public String getFeeInclude() {
		return _feeInclude;
	}

	public void setFeeInclude(String feeInclude) {
		this._feeInclude = feeInclude;
	}

	public String getFeeNotInclude() {
		return _feeNotInclude;
	}

	public void setFeeNotInclude(String feeNotInclude) {
		this._feeNotInclude = feeNotInclude;
	}

	public String getFeeType() {
		return _feeType;
	}

	public void setFeeType(String feeType) {
		this._feeType = feeType;
	}

	public static CharSequence GetFeeTypeDes(String feeType) {
		// TODO Auto-generated method stub
		// 0:预付，1：现付
		String res = "";

		if ("0".equals(feeType))
			res = "预付";
		if ("1".equals(feeType))
			res = "现付";

		return res;
	}

	@Override
	public String toString() {
		return "GCompetition [_id=" + _id + ", _agentId=" + _agentId
				+ ", _agentName=" + _agentName + ", _name=" + _name
				+ ", _desc=" + _desc + ", _fee=" + _fee + ", _startDate="
				+ _startDate + ", _endDate=" + _endDate + ", _plan=" + _plan
				+ ", _feeInclude=" + _feeInclude + ", _feeNotInclude="
				+ _feeNotInclude + ", _feeType=" + _feeType + ", _courtName="
				+ _courtName + ", _courtId=" + _courtId + ", _img=" + _img
				+ ", _imgs=" + _imgs + "]";
	}
	
	

}
