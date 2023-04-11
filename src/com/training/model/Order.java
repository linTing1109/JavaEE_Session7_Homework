package com.training.model;

import java.util.Date;

public class Order {
	private int orderID;//訂單編號
	private String customerName;//顧客姓名
	private Date orderDate;//訂單日期
	private String goodsName;//商品名稱
	private int goodsByPrice;//買的價錢
	private int buyQuantity;//買的數量
	private int buyAmount;//買的總金額
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsByPrice() {
		return goodsByPrice;
	}
	public void setGoodsByPrice(int goodsByPrice) {
		this.goodsByPrice = goodsByPrice;
	}
	public int getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(int buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	public int getBuyAmount() {
		return buyAmount;
	}
	public void setBuyAmount(int buyAmount) {
		this.buyAmount = buyQuantity*goodsByPrice;
	}
	@Override
	public String toString() {
		return "GoodsOrderForm [orderID=" + orderID + ", customerName=" + customerName + ", orderDate=" + orderDate
				+ ", goodsName=" + goodsName + ", goodsByPrice=" + goodsByPrice + ", buyQuantity=" + buyQuantity
				+ ", buyAmount=" + buyAmount + "]";
	}
}
