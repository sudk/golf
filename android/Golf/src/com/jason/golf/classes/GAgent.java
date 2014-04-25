package com.jason.golf.classes;

/*
 * 代理商
 */
public class GAgent {

	private String _name;
	private String _brief;
	private String _price;

	public GAgent(String name, String brief, String price) {
		this._name = name;
		this._brief = brief;
		this._price = price;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getBrief() {
		return _brief;
	}

	public void setBrief(String brief) {
		this._brief = brief;
	}

	public String getPrice() {
		return _price;
	}

	public void setPrice(String price) {
		this._price = price;
	}

}
