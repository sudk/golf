package com.jason.golf.classes;

import java.util.ArrayList;

/*
 * 
 * ����
 * 
 */
public class GCourt {

	private String _id;
	private String _name;
	private String _city;
	private String _address;
	private String _model;
	private String _createYear;
	private String _area;
	private String _greenGrass;
	private String _designer;
	private String _courtData;
	private String _fairwayLength;
	private String _fairwayGrass;
	private String _phone;
	private String _remark;
	private String _facilities;

	private ArrayList<String> _fairwayImgs;
	private ArrayList<String> _courtImgs;

	public GCourt() {
		_fairwayImgs = new ArrayList<String>();
		_courtImgs = new ArrayList<String>();
	}

	public void addFairwayImg(String url) {
		_fairwayImgs.add(url);
	}
	
	public ArrayList<String> getFairwayImgs(){
		return _fairwayImgs;
	}
	
	public ArrayList<String> getCourtImgs(){
		return _courtImgs;
	}

	public void addCourtImg(String url) {
		_courtImgs.add(url);
	}

	public String getModel() {
		return _model;
	}

	public void setModel(String model) {
		this._model = model;
	}

	public String getArea() {
		return _area;
	}

	public void setArea(String area) {
		this._area = area;
	}

	public String getCourtData() {
		return _courtData;
	}

	public void setCourtData(String courtData) {
		this._courtData = courtData;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String city) {
		this._city = city;
	}

	public String getAddress() {
		return _address;
	}

	public void setAddress(String address) {
		this._address = address;
	}

	public String getCreateYear() {
		return _createYear;
	}

	public void setCreateYear(String createYear) {
		this._createYear = createYear;
	}

	public String getGreenGrass() {
		return _greenGrass;
	}

	public void setGreenGrass(String greenGrass) {
		this._greenGrass = greenGrass;
	}

	public String getDesigner() {
		return _designer;
	}

	public void setDesigner(String designer) {
		this._designer = designer;
	}

	public String getFairwayLength() {
		return _fairwayLength;
	}

	public void setFairwayLength(String fairwayLength) {
		this._fairwayLength = fairwayLength;
	}

	public String getFairwayGrass() {
		return _fairwayGrass;
	}

	public void setFairwayGrass(String fairwayGrass) {
		this._fairwayGrass = fairwayGrass;
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		this._phone = phone;
	}

	public String getRemark() {
		return _remark;
	}

	public void setRemark(String remark) {
		this._remark = remark;
	}

	public String getFacilities() {
		return _facilities;
	}

	public void setFacilities(String facilities) {
		this._facilities = facilities;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "GCourt [_id=" + _id + ", _name=" + _name + ", _city=" + _city
				+ ", _address=" + _address + ", _model=" + _model
				+ ", _createYear=" + _createYear + ", _area=" + _area
				+ ", _greenGrass=" + _greenGrass + ", _designer=" + _designer
				+ ", _courtData=" + _courtData + ", _fairwayLength="
				+ _fairwayLength + ", _fairwayGrass=" + _fairwayGrass
				+ ", _phone=" + _phone + ", _remark=" + _remark
				+ ", _facilities=" + _facilities + ", _fairwayImgs=[" );
		
		for (String str : _fairwayImgs) {
			builder.append(str).append(",");
		}
		builder.append("] , _courtImgs=[" );
		
		for (String str : _courtImgs) {
			builder.append(str).append(",");
		}
		
		builder.append("]]");
		
		return builder.toString();
	}
}
