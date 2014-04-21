package com.jason.golf.classes;

public class GAdver {
	
	private int _order;
	private int _type;
	private int _state;
	
	private String _url;
	private long _startDate;
	private long _endDate;
	
	public GAdver(int order, int type, String url, long startDate, long endDate) {
		super();
		this._order = order;
		this._type = type;
		this._url = url;
		this._startDate = startDate;
		this._endDate = endDate;
	}

	public int getOrder() {
		return _order;
	}

	public void setOrder(int order) {
		this._order = order;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		this._type = type;
	}

	public int getState() {
		return _state;
	}

	public void setState(int state) {
		this._state = state;
	}

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		this._url = url;
	}

	public long getStartDate() {
		return _startDate;
	}

	public void setStartDate(long startDate) {
		this._startDate = startDate;
	}

	public long getEndDate() {
		return _endDate;
	}

	public void setEndDate(long endDate) {
		this._endDate = endDate;
	}
	
	
	

}
