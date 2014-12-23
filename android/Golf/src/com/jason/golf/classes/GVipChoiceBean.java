package com.jason.golf.classes;

public class GVipChoiceBean {

	public String id;
	public String value;
	public String desc;

	public GVipChoiceBean(String id, String value, String desc) {
		super();
		this.id = id;
		this.value = value;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}