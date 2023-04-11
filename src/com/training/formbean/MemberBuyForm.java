package com.training.formbean;

import org.apache.struts.action.ActionForm;

public class MemberBuyForm extends ActionForm{
	private String goodsID;
	private String buyQuantity;
	public String getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(String goodsID) {
		this.goodsID = goodsID;
	}
	public String getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(String buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	
	
}
