package com.jason.golf.classes;

public class GPhoto {
	
	public static final int MARK_COURT = 0x1001;//Çò³¡
	public static final int MARK_ADVER = 0x1002;//¹ã¸æ
	
	private String _subnailUrl;
	private String _submailPath;
	private String _originalUrl;
	private String _originalPath;
	private int _belong;
	private int _fatherId;
	
	public GPhoto(String subnailUrl, String originalUrl, int belong, int fatherid) {
		super();
		this._subnailUrl = subnailUrl;
		this._originalUrl = originalUrl;
		this._belong = belong;
		this._fatherId = fatherid;
	}

	public String getSubnailUrl() {
		return _subnailUrl;
	}

	public void setSubnailUrl(String subnailUrl) {
		this._subnailUrl = subnailUrl;
	}

	public String getSubmailPath() {
		return _submailPath;
	}

	public void setSubmailPath(String submailPath) {
		this._submailPath = submailPath;
	}

	public String getOriginalUrl() {
		return _originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this._originalUrl = originalUrl;
	}

	public String getOriginalPath() {
		return _originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this._originalPath = originalPath;
	}

	public int getBelong() {
		return _belong;
	}

	public void setBelong(int belong) {
		this._belong = belong;
	}

	public int getFatherId() {
		return _fatherId;
	}

	public void setFatherId(int fatherId) {
		this._fatherId = fatherId;
	}

	
	
}
