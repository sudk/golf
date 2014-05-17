package com.jason.golf.classes;

import android.text.TextUtils;

public class GAccount {

	private String _id;
	private String _session;

	private String _phone;
	private String _city;
	private String _name;

	private String _eamil;
	private String _sex;
	private String _remark;

	private String _balance; // ���

	private String _type; // �Ƿ�VIP�ͻ�
	private String _vipCardNo; // VIP����
	private String _point; // ���

	private double _latitude;
	private double _longitude;
	private boolean _login;
	
	public GAccount(){
		_latitude = 0;
		_longitude = 0;
		_city = "";
		_balance = "0";
		_point = "0";
		_login = false;
	}

	public void initilization(String id) {
		_id = id;
		_login = true;
	}

	public String getRemark() {
		return _remark;
	}

	public void setRemark(String remark) {
		this._remark = remark;
	}

	public String getSex() {
		return _sex;
	}

	public void setSex(String sex) {
		this._sex = sex;
	}

	public String getEamil() {
		return _eamil;
	}

	public void setEamil(String eamil) {
		this._eamil = eamil;
	}

	public double getLatitude() {
		return _latitude;
	}

	public void setLatitude(double latitude) {
		this._latitude = latitude;
	}

	public double getLongitude() {
		return _longitude;
	}

	public void setLongitude(double longitude) {
		_longitude = longitude;
	}

	public String getId() {
		return _id;
	}

	public String getSession() {
		return _session;
	}

	public void setSession(String session) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(session)) {
			_session = session.substring(0, session.indexOf(";"));
		}
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		this._phone = phone;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String city) {
		this._city = city;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public int getBalance() {
		int res = 0;
		if (!TextUtils.isEmpty(_balance) && TextUtils.isDigitsOnly(_balance)) {
			res = Integer.parseInt(_balance);
		}
		return res;
	}

	public void setBalance(String balance) {
		this._balance = balance;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	public String getVipCardNo() {
		return _vipCardNo;
	}

	public void setVipCardNo(String vipCardNo) {
		this._vipCardNo = vipCardNo;
	}

	public int getPoint() {
		int res = 0;
		if (!TextUtils.isEmpty(_point) && TextUtils.isDigitsOnly(_point)) {
			res = Integer.parseInt(_point);
		}
		return res;
	}

	public void setPoint(String point) {
		this._point = point;
	}

	/*
	 * 
	 * 判断用户是否已经登录
	 */
	public boolean isLogin() {
		return _login;
	}

	public void clear() {
		// TODO Auto-generated method stub
		_id = null;
		_session = null;
		_login = false;
	}

	@Override
	public String toString() {
		return "GAccount [_id=" + _id + ", _session=" + _session + ", _phone="
				+ _phone + ", _city=" + _city + ", _name=" + _name
				+ ", _eamil=" + _eamil + ", _sex=" + _sex + ", _remark="
				+ _remark + ", _balance=" + _balance + ", _type=" + _type
				+ ", _vipCardNo=" + _vipCardNo + ", _point=" + _point
				+ ", _latitude=" + _latitude + ", _longitude=" + _longitude
				+ ", _login=" + _login + "]";
	}


}
