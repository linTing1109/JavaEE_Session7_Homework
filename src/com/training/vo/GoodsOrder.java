package com.training.vo;

public class GoodsOrder {
	private String customerID;// 顧客編號
	private int inputMoney;// 投入金額
	private String[] goodsIDs;// 購買商品編號
	private String[] buyQuantitys;// 購買商品數量

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public int getInputMoney() {
		return inputMoney;
	}

	public void setInputMoney(int inputMoney) {
		this.inputMoney = inputMoney;
	}

	public String[] getGoodsIDs() {
		return goodsIDs;
	}

	public void setGoodsIDs(String[] goodsIDs) {
		this.goodsIDs = goodsIDs;
	}

	public String[] getBuyQuantitys() {
		return buyQuantitys;
	}

	public void setBuyQuantitys(String[] buyQuantitys) {
		this.buyQuantitys = buyQuantitys;
	}
}
