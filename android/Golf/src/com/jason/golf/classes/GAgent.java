package com.jason.golf.classes;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
/*
 * 代理商
 */
public class GAgent {
	
	public static String GetPayTypeDes(String payType){
		
		String res = "";

		if ("0".equals(payType))
			res = "球场现付";
		if ("1".equals(payType))
			res = "全额预付";
		if ("2".equals(payType))
			res = "押金";

		return res;
		
	}

	private String _id;
	private String _name;
	private String _courtid;
	private String _courtname;
	private String _priceremark; // 价格说明
	private String _cancelRemark; // 预约取消说明
	private boolean _green;
	private boolean _caddie;
	private boolean _car;
	private boolean _wardrobe;
	private boolean _meal;
	private boolean _insurance; // 保险柜
	private boolean _tips; // 小费
	private String _payType; // 支付方式 ：0为现付，1为全额预付，2为押金
	private String _price;
	private String _vipprice;
	private String _pledgePrice;
	private String _bargainPriceDes; // 特价说明
	
	private String _teeTime;

	public String getService() {
		StringBuilder builder = new StringBuilder();

		if (_green)
			builder.append("果岭").append("\\");
		if (_caddie)
			builder.append("僮").append("\\");
		if (_car)
			builder.append("球车").append("\\");
		if (_wardrobe)
			builder.append("柜").append("\\");
		if (_meal)
			builder.append("餐").append("\\");
		if (_insurance)
			builder.append("保险").append("\\");
		if (_tips)
			builder.append("小费").append("\\");

		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}
	
	
	public boolean initialize(JSONObject item){
		
		
		try {
			_id = item.getString("agent_id");
			this._name = item.getString("agent_name");
			this._courtid = item.getString("court_id");
			_courtname = item.getString("court_name");
			_priceremark = item.getString("remark");
			_priceremark = item.getString("cancel_remark");
			_green = "1".equals(item.getString("is_green"));
			_caddie = "1".equals(item.getString("is_caddie"));
			_car = "1".equals(item.getString("is_car"));
			_wardrobe = "1".equals(item.getString("is_wardrobe"));
			_meal = "1".equals(item.getString("is_meal"));
			_insurance = "1".equals(item.getString("is_insurance"));
			_tips = "1".equals(item.getString("is_tip"));
			_payType = item.getString("pay_type");
			_price = item.getString("price");
			_vipprice = item.getString("vip_price");
			_pledgePrice = item.getString("pledge_price");
//			_bargainPriceDes = item.getString("special_desc");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		
		return true;
	}


	public String getTeeTime() {
		return _teeTime;
	}

	public void setTeeTime(String teeTime) {
		this._teeTime = teeTime;
	}

	public String getCancelRemark() {
		return _cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this._cancelRemark = cancelRemark;
	}

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

	public void setGreen(String green) {
		this._green = "1".equals(green);
	}

	public boolean isCaddie() {
		return _caddie;
	}

	public void setCaddie(String caddie) {
		this._caddie = "1".equals(caddie);
	}

	public boolean isCar() {
		return _car;
	}

	public void setCar(String car) {
		this._car = "1".equals(car);
	}

	public boolean isWardrobe() {
		return _wardrobe;
	}

	public void setWardrobe(String wardrobe) {
		this._wardrobe = "1".equals(wardrobe);
	}

	public boolean isMeal() {
		return _meal;
	}

	public void setMeal(String meal) {
		this._meal = "1".equals(meal);
	}

	public boolean isInsurance() {
		return _insurance;
	}

	public void setInsurance(String insurance) {
		this._insurance = "1".equals(insurance);
	}

	public boolean isTips() {
		return _tips;
	}

	public void setTips(String tips) {
		this._tips = "1".equals(tips);
	}

	public String getPayType() {

		return _payType;
	}

	public void setPayType(String payType) {
		this._payType = payType;
	}

	public int getPrice() {

		int p = 0;

		if (!TextUtils.isEmpty(_price) && TextUtils.isDigitsOnly(_price)) {
			p = Integer.parseInt(_price);
		}

		return p;
	}
	
	public int getVipPrice(){
		int p = 0;

		if (!TextUtils.isEmpty(_vipprice) && TextUtils.isDigitsOnly(_vipprice)) {
			p = Integer.parseInt(_vipprice);
		}

		return p;
		
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
