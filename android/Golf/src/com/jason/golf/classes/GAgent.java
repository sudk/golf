package com.jason.golf.classes;

/*
 * 代理商
 */
public class GAgent {

	private String _id;
	private String _name;
	private String _courtid;
	private String _courtname;
	private String _priceremark; // 价格说明
	private boolean _green;
	private boolean _caddie;
	private boolean _car;
	private boolean _wardrobe;
	private boolean _meal;
	private boolean _insurance; // 保险柜
	private boolean _tips; // 小费
	private int _payType; // 支付方式 ：0为现付，1为全额预付，2为押金
	private String _price;
	private String _bargainPriceDes; // 特价说明

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getCourtid() {
		return _courtid;
	}

	public void setCourtid(String courtid) {
		this._courtid = courtid;
	}

	public String getCourtname() {
		return _courtname;
	}

	public void setCourtname(String courtname) {
		this._courtname = courtname;
	}

	public String getPriceremark() {
		return _priceremark;
	}

	public void setPriceremark(String priceremark) {
		this._priceremark = priceremark;
	}

	public boolean isGreen() {
		return _green;
	}

	public void setGreen(boolean green) {
		this._green = green;
	}

	public boolean isCaddie() {
		return _caddie;
	}

	public void setCaddie(boolean caddie) {
		this._caddie = caddie;
	}

	public boolean isCar() {
		return _car;
	}

	public void setCar(boolean car) {
		this._car = car;
	}

	public boolean isWardrobe() {
		return _wardrobe;
	}

	public void setWardrobe(boolean wardrobe) {
		this._wardrobe = wardrobe;
	}

	public boolean isMeal() {
		return _meal;
	}

	public void setMeal(boolean meal) {
		this._meal = meal;
	}

	public boolean isInsurance() {
		return _insurance;
	}

	public void setInsurance(boolean insurance) {
		this._insurance = insurance;
	}

	public boolean isTips() {
		return _tips;
	}

	public void setTips(boolean tips) {
		this._tips = tips;
	}

	public int getPayType() {
		return _payType;
	}

	public void setPayType(int payType) {
		this._payType = payType;
	}

	public String getPrice() {
		return _price;
	}

	public void setPrice(String price) {
		this._price = price;
	}

	public String getBargainPriceDes() {
		return _bargainPriceDes;
	}

	public void setBargainPriceDes(String bargainPriceDes) {
		this._bargainPriceDes = bargainPriceDes;
	}

}
