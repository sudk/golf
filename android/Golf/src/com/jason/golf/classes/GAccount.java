package com.jason.golf.classes;

import android.text.TextUtils;

public class GAccount {
	
	private String _id;
	private String _session;
	
	private String _phone;
	private String _city;
	private String _name;
	
	private String _balance; //余额
	
	private String _type; // 是否VIP客户
	private String _vipCardNo; //VIP卡号
	private String _point; // 积分
	

	public void initilization(String id, String session){
		_id = id;
		_session = session;
	}
	
	public boolean hasLogin(){
		if(TextUtils.isEmpty(_id) || TextUtils.isEmpty(_session))
			return false;
		else
			return true;
	}
	
	public String getId(){
		return _id;
	}

	public String getSession(){
		return _session;
	}

	public String getPhone() {
		return _phone;
	}

	public void set_phone(String phone) {
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

	public void set_name(String name) {
		this._name = name;
	}

	public String getBalance() {
		return _balance;
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

	public String getPoint() {
		return _point;
	}

	public void setPoint(String point) {
		this._point = point;
	}

	public void clear() {
		// TODO Auto-generated method stub
		_id = null;
		_session = null;
	}
	

}
