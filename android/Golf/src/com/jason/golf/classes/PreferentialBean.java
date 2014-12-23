package com.jason.golf.classes;

public class PreferentialBean {

	String _id;
	String _name;
	String _addr;
	long _distance;
	int _price;
	String _icoImgUrl;
	String _payType;
	// boolean _isOfficial;

	String _startTime;
	String _endTime;

	String _date;
//	String _time;

	public String getStartTime() {
		return _startTime;
	}

	public void setStartTime(String startTime) {
		this._startTime = startTime;
	}

	public String getEndTime() {
		return _endTime;
	}

	public void setEndTime(String endTime) {
		this._endTime = endTime;
	}

	public String getDate() {
		return _date;
	}

	public void setDate(String date) {
		this._date = date;
	}

//	public String getTime() {
//		return _time;
//	}
//
//	public void setTime(String time) {
//		this._time = time;
//	}

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

		// System.out.println("distance " + distance);
		try {
			float f = Float.parseFloat(distance);
			long d = (long) f;
			this._distance = d;
		} catch (NumberFormatException e) {
			this._distance = -1;// -1 代表未知
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
	// public boolean isOfficial() {
	// return _isOfficial;
	// }
	// public void setIsOfficial(String str) {
	// this._isOfficial = "0".equals(str);
	// }

}
