package com.jason.golf.classes;

import android.location.Location;
import android.text.TextUtils;

public class GAccount {
	
	private String _id;
	private String _session;
	
	private String _phone;
	private String _city;
	private String _name;
	
	private String _balance; //���
	
	private String _type; // �Ƿ�VIP�ͻ�
	private String _vipCardNo; //VIP����
	private String _point; // ���
	
	private Location _loc;

	public void initilization(String id, String session){
		_id = id;
		_session = session;
	}
	
	/*
	 * 
	 * 判断用户是否已经登录
	 * 
	 */
	public boolean hasLogin(){
		if(TextUtils.isEmpty(_id) || TextUtils.isEmpty(_session))
			return false;
		else
			return true;
	}
	
	public Location getLoc() {
		return _loc;
	}

	public void setLoc(Location loc) {
		this._loc = loc;
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

	@Override
	public String toString() {
		return "GAccount [_id=" + _id + ", _session=" + _session + ", _phone="
				+ _phone + ", _city=" + _city + ", _name=" + _name
				+ ", _balance=" + _balance + ", _type=" + _type
				+ ", _vipCardNo=" + _vipCardNo + ", _point=" + _point + "]";
	}
	
	

}
