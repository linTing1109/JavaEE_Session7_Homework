package com.training.formbean;

import org.apache.struts.action.ActionForm;

public class BackedListForm extends ActionForm{
	private String goodsId;
	private String goodsName;
	private String priceMin;
	private String priceMax;
	private String orderByItem;
	private String stockQuantity;
	private String status;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public String getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(String priceMin) {
		this.priceMin = priceMin;
	}
	public String getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}
	public String getOrderByItem() {
		return orderByItem;
	}
	public void setOrderByItem(String orderByItem) {
		this.orderByItem = orderByItem;
	}
	public String getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(String stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
