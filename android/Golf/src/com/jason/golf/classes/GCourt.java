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
	private String _price;
	private String _point; //���
	private String _description;
	private String _address;
	private String _createYear;
	private String _greenGrass;
	private String _designer;
	private String _fairwayLength;
	private String _fairwayGrass;
	private String _phone;
	private String _remark;
	private String _facilities;
	private String _data;
	
	private ArrayList<GPhoto> _photos;

	
	
	public GCourt(String id, String name, String city, String price, String point,
			String description, String address, String createYear,
			String greenGrass, String designer, String fairwayLength,
			String fairwayGrass, String phone, String remark,
			String facilities, String data) {
		super();
		this._id = id;
		this._name = name;
		this._city = city;
		this._price = price;
		this._point = point;
		this._description = description;
		this._address = address;
		this._createYear = createYear;
		this._greenGrass = greenGrass;
		this._designer = designer;
		this._fairwayLength = fairwayLength;
		this._fairwayGrass = fairwayGrass;
		this._phone = phone;
		this._remark = remark;
		this._facilities = facilities;
		this._data = data;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}
	
	public String getName(){
		return this._name;
	}
	
	public void setName(String name){
		this._name = name ;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String city) {
		this._city = city;
	}

	public String getPrice() {
		return _price;
	}

	public void setPrice(String price) {
		this._price = price;
	}

	public String getPoint() {
		return _point;
	}

	public void setPoint(String point) {
		this._point = point;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		this._description = description;
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

	public String getData() {
		return _data;
	}

	public void setData(String data) {
		this._data = data;
	}

}
