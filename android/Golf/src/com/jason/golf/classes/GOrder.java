package com.jason.golf.classes;

public class GOrder {
	
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
	
	public static String GetOrderTypeDes(String payType){
		String res = "";
		
//		订单类型，0、订场；1、行程；2、赛事报名；3、充值

		if ("0".equals(payType))
			res = "预定球场";
		if ("1".equals(payType))
			res = "预定行程";
		if ("2".equals(payType))
			res = "赛事报名";
		if ("3".equals(payType))
			res = "充值";

		return res;
		
	}
	
	public static String GetStatusDes(String status){

		/**
	     * 状态流程
	     * 0、等待确认；1、等待付款；2、付款完成；3、交易关闭 ，4-未到场  5-交易成功，6-等待退款 7-拒绝退款 8-退款中
	     * 1）需要预付款的订单，需要完成上面的流程；无需预付款的，“等待确认”  到  “交易成功”
	     * 2）在“等待确认”，“等待付款”时，客户可以随时 关闭交易。
	     * 3）“付款完成”，客户想取消订单，只能【申请退款】，填写退款原因。
	     * 4）客户未到场，则有 代理商 修改状态 “未到场”，可能需要执行一个返还部分款项的操作。订单结束
	     * 5）客户打球完，代理商 修改状态 “交易成功”。订单结束
	     * 6）客户申请退款，状态为“等待退款”。代理商审核通过，则执行退款流程，状态为“退款中”。退款完成，状态为“交易成功”。订单结束
	     * 7）客户申请退款，代理商审核不通过，状态为“拒绝退款”。 订单结束。
	     * 8)代理商 可以在任何时候 关闭订单。“交易关闭”，已经付款的，执行退款流程
	     */
		
		String res = "";

		if ("0".equals(status))
			res = "等待确认";
		if ("1".equals(status))
			res = "等待付款";
		if ("2".equals(status))
			res = "付款完成";
		if ("3".equals(status))
			res = "交易关闭";
		if ("4".equals(status))
			res = "未到场";
		if ("5".equals(status))
			res = "交易成功";
		if ("6".equals(status))
			res = "等待退款";
		if ("7".equals(status))
			res = "拒绝退款";
		if ("8".equals(status))
			res = "退款中";

		return res;
		
	}

	/*
	 * 
	 * order_id 订单编号 type 类型 订单类型，0、订场；1、行程；3、赛事报名； relation_id 关联ID
	 * relation_name 关联商品名称 tee_time 打球时间 count 数量 unitprice 单价 amount 订单金额
	 * had_pay 实际支付金额 pay_type 支付类型 支付方式：0为现付，1为全额预付，2为押金 status 状态
	 * 0、待确认；1、待付款；2、完成；3、撤销 record_time 记录时间 desc 描述 contact 联系人 phone 电话
	 */

	private String _orderId;
	private int _type;
	private String _relationId;
	private String _relationName;
	private String _teeTime;
	private int _count;
	private int _unitprice;
	private int _amount;
	private int _hadPay;
	private String _payType;
	private String _status;
	private String _recordTime;
	private String _desc;
	private String _contact;
	private String _phone;
	private String _agentId;

	public String getAgentId() {
		return _agentId;
	}

	public void setAgentId(String agentId) {
		this._agentId = agentId;
	}

	public String getOrderId() {
		return _orderId;
	}

	public void setOrderId(String order_id) {
		this._orderId = order_id;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		this._type = type;
	}

	public String getRelation_id() {
		return _relationId;
	}

	public void setRelationId(String relation_id) {
		this._relationId = relation_id;
	}

	public String getRelationName() {
		return _relationName;
	}

	public void setRelationName(String relation_name) {
		this._relationName = relation_name;
	}

	public String getTeeTime() {
		return _teeTime;
	}

	public void setTeeTime(String tee_time) {
		this._teeTime = tee_time;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int count) {
		this._count = count;
	}

	public int getUnitprice() {
		return _unitprice;
	}

	public void setUnitprice(int unitprice) {
		this._unitprice = unitprice;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		this._amount = amount;
	}

	public int getHadPay() {
		return _hadPay;
	}

	public void setHadPay(int had_pay) {
		this._hadPay = had_pay;
	}

	public String getPayType() {
		return _payType;
	}

	public void setPayType(String pay_type) {
		this._payType = pay_type;
	}

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		this._status = status;
	}

	public String getRecordTime() {
		return _recordTime;
	}

	public void setRecordTime(String record_time) {
		this._recordTime = record_time;
	}

	public String getDesc() {
		return _desc;
	}

	public void setDesc(String desc) {
		this._desc = desc;
	}

	public String getContact() {
		return _contact;
	}

	public void setContact(String contact) {
		this._contact = contact;
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		this._phone = phone;
	}

}
