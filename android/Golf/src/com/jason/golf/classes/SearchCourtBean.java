package com.jason.golf.classes;

import android.text.TextUtils;

public class SearchCourtBean {
	
	String _id; 
	String _name;
	String _addr;
	long   _distance;
	int      _price;
	String   _icoImgUrl;
	String   _payType;
//	boolean  _isOfficial;
	
	
	
	public String getId() {
		return _id;
	}
	public void setId(String _id) {
		this._id = _id;
	}
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	public String getAddr() {
		return _addr;
	}
	public void setAddr(String addr) {
		this._addr = addr;
	}
	public long getDistance() {
		return _distance;
	}
	public void setDistance(String distance) {
		
		if(TextUtils.isDigitsOnly(distance)){
			float f = Float.parseFloat(distance);
			long d = (long) f;
			this._distance = d;
		}else{
			this._distance = -1; // -1 代表未知
		}
		
	}
	public int getPrice() {
		return _price;
	}
	public void setPrice(int price) {
		this._price = price;
	}
	public String getIcoImgUrl() {
		return _icoImgUrl;
	}
	public void setIcoImgUrl(String icoImgUrl) {
		this._icoImgUrl = icoImgUrl;
	}
	public String getPayType() {
		return _payType;
	}
	public void setPayType(String payType) {
		this._payType = payType;
	}
//	public boolean isOfficial() {
//		return _isOfficial;
//	}
//	public void setIsOfficial(String str) {
//		this._isOfficial = "0".equals(str);
//	}

}