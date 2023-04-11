package com.training.formbean;

import org.apache.struts.action.ActionForm;

//LoginRegisterForm 接收會員註冊 資料
public class LoginRegisterForm  extends ActionForm{
	
	private String identificationNo;//會員ID
	private String password;//會員密碼
	private String customerName;//會員名稱
	
	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}
	public String getIdentificationNo() {
		return identificationNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	

}
