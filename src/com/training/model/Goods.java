package com.training.model;

import java.util.Objects;

public class Goods {
	private Long goodsID;//商品編號
	private String goodsName;//商品名稱
	private String description;//商品描述
	private int goodsPrice;//商品價格
	private int goodsQuantity;//商品庫存
	private String goodsImageName;//商品圖片名稱
	private String status;//商品狀態 1:上架 0:下架
	
	public Goods() {
		super();
	}
	
	public Goods(Long goodsID) {
		super();
		this.goodsID = goodsID;
	}


	public Long getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(Long goodsID) {
		this.goodsID = goodsID;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsQuantity() {
		return goodsQuantity;
	}
	public void setGoodsQuantity(int goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}
	public String getGoodsImageName() {
		return goodsImageName;
	}
	public void setGoodsImageName(String goodsImageName) {
		this.goodsImageName = goodsImageName;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Goods [goodsID=" + goodsID + ", goodsName=" + goodsName + ", goodsPrice=" + goodsPrice
				+ ", goodsQuantity=" + goodsQuantity + ", goodsImageName=" + goodsImageName + ", status=" + status
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(goodsID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goods other = (Goods) obj;
		return Objects.equals(goodsID, other.goodsID);
	}
	
	
}
