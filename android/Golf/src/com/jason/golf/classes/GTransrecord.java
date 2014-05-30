package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class GTransrecord {

	private String _id;
	private String _serialNumber;
	private String _transType;
	private String _amount;
	private String _reSerialNumber;
	private String _status;
	private String _userId;
	private String _recordTime;

	public boolean initialize(JSONObject data) {

		try {
			this._id = data.getString("id");
			this._serialNumber = data.getString("serial_number");
			this._transType = data.getString("trans_type");
			this._amount = data.getString("amount");
			this._reSerialNumber = data.getString("re_serial_number");
			this._status = data.getString("status");
			this._userId = data.getString("user_id");
			this._recordTime = data.getString("record_time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	public String getId() {
		return _id;
	}

	public String getSerialNumber() {
		return _serialNumber;
	}

	public String getTransType() {
		return _transType;
	}

	public String getAmount() {
		return _amount;
	}

	public String getRe_serial_number() {
		return _reSerialNumber;
	}

	public String getStatus() {
		return _status;
	}

	public String getUser_id() {
		return _userId;
	}

	public String getRecordTime() {
		return _recordTime;
	}

}
