package com.training.vo;

import java.util.Map;

import com.training.model.Goods;

public class BuyGoodsRtn {
	public int inputMoney;//投入金額
	public int sumOrderAmount;//購買總金額
	public int changeDollars;//找零金額
	public Map<Goods, Integer> goodsOrders;//建立訂單資料(key:購買商品、value:購買數量)
	public String buyGoodMsg;// 購買中訊息
	public boolean checkSuccess;// 購買狀態(成功:true,失敗false)
	
	public BuyGoodsRtn() {
		super();
	}
	public int getInputMoney() {
		return inputMoney;
	}
	public void setInputMoney(int inputMoney) {
		this.inputMoney = inputMoney;
	}
	public int getSumOrderAmount() {
		return sumOrderAmount;
	}
	public void setSumOrderAmount(int sumOrderAmount) {
		this.sumOrderAmount = sumOrderAmount;
	}
	public int getChangeDollars() {
		return changeDollars;
	}
	public void setChangeDollars(int changeDollars) {
		this.changeDollars = changeDollars;
	}
	public Map<Goods, Integer> getGoodsOrders() {
		return goodsOrders;
	}
	public void setGoodsOrders(Map<Goods, Integer> goodsOrders) {
		this.goodsOrders = goodsOrders;
	}
	public String getBuyGoodMsg() {
		return buyGoodMsg;
	}
	public void setBuyGoodMsg(String buyGoodMsg) {
		this.buyGoodMsg = buyGoodMsg;
	}
	public boolean isCheckSuccess() {
		return checkSuccess;
	}
	public void setCheckSuccess(boolean checkSuccess) {
		this.checkSuccess = checkSuccess;
	}
	
	
}
